/*
 * Copyright 2002-2007 Sascha Weinreuter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.intellij.plugins.xsltDebugger;

import com.intellij.diagnostic.logging.AdditionalTabComponent;
import com.intellij.diagnostic.logging.LogConsoleManagerBase;
import com.intellij.execution.CantRunException;
import com.intellij.execution.configurations.AdditionalTabComponentManager;
import com.intellij.execution.configurations.SimpleJavaParameters;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessListener;
import com.intellij.execution.runners.RunTab;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.PathsList;
import com.intellij.util.PlatformIcons;
import com.intellij.util.net.NetUtils;
import org.intellij.lang.xpath.xslt.XsltSupport;
import org.intellij.lang.xpath.xslt.impl.XsltChecker;
import org.intellij.lang.xpath.xslt.run.XsltRunConfiguration;
import org.intellij.lang.xpath.xslt.run.XsltRunnerExtension;
import org.intellij.plugins.xsltDebugger.ui.OutputTabComponent;
import org.intellij.plugins.xsltDebugger.ui.StructureTabComponent;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.platform.loader.PlatformLoader;
import org.jetbrains.platform.loader.repository.PlatformRepository;
import org.jetbrains.platform.loader.repository.RuntimeModuleId;

import java.io.IOException;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Extension for XPathView that hooks into the execution of XSLT-scripts. Manages the creation of the XSLT-Debugger UI
 * and ensures the debugged process is started with the required JVM properties.
 */
public class XsltDebuggerExtension extends XsltRunnerExtension {
  private static final Logger LOG = Logger.getInstance(XsltDebuggerExtension.class.getName());

  public static final Key<XsltChecker.LanguageLevel> VERSION = Key.create("VERSION");
  private static final Key<Integer> PORT = Key.create("PORT");
  private static final Key<Manifest> MANIFEST = Key.create("MANIFEST");

  protected boolean supports(XsltRunConfiguration config, boolean debugger) {
    return (debugger || XsltDebuggerRunner.ACTIVE.get() == Boolean.TRUE) &&
           config.getOutputType() == XsltRunConfiguration.OutputType.CONSOLE; // ?
  }

  public ProcessListener createProcessListener(Project project, UserDataHolder extensionData) {
    final Integer port = extensionData.getUserData(PORT);
    assert port != null;
    return new DebugProcessListener(project, port);
  }

  public boolean createTabs(Project project,
                            AdditionalTabComponentManager manager,
                            AdditionalTabComponent outputConsole,
                            ProcessHandler process) {
    if (manager instanceof RunTab) {
      LogConsoleManagerBase runTab = ((RunTab)manager).getLogConsoleManager();
      runTab.addAdditionalTabComponent(new OutputTabComponent(outputConsole), "XSLT-Output", AllIcons.Debugger.Console);
      runTab.addAdditionalTabComponent(StructureTabComponent.create(process, outputConsole), "XSLT-Structure", PlatformIcons.FLATTEN_PACKAGES_ICON);
    }
    else {
      manager.addAdditionalTabComponent(new OutputTabComponent(outputConsole), "XSLT-Output");
      manager.addAdditionalTabComponent(StructureTabComponent.create(process, outputConsole), "XSLT-Structure");
    }
    return true;
  }

  public void patchParameters(final SimpleJavaParameters parameters, XsltRunConfiguration configuration, UserDataHolder extensionData)
    throws CantRunException {
    final XsltRunConfiguration.OutputType outputType = configuration.getOutputType();

    final Sdk jdk = configuration.getEffectiveJDK();
    assert jdk != null;

    final String ver = jdk.getVersionString();
    if (ver == null || (ver.contains("1.0") || ver.contains("1.1") || ver.contains("1.2") || ver.contains("1.3") || ver.contains("1.4"))) {
      throw new CantRunException("The XSLT Debugger can only be used with JDK 1.5+");
    }

    // TODO: fix and remove
    if (outputType != XsltRunConfiguration.OutputType.CONSOLE) {
      throw new CantRunException("XSLT Debugger requires Output Type == CONSOLE");
    }

    try {
      final int port = NetUtils.findAvailableSocketPort();
      parameters.getVMParametersList().defineProperty("xslt.debugger.port", String.valueOf(port));
      extensionData.putUserData(PORT, port);
    } catch (IOException e) {
      LOG.info(e);
      throw new CantRunException("Unable to find a free network port");
    }

    PlatformRepository repository = PlatformLoader.getInstance().getRepository();
    PathsList classPath = parameters.getClassPath();
    classPath.addAll(repository.getModuleRootPaths(RuntimeModuleId.module("xslt-debugger-engine-impl")));
    classPath.addAll(repository.getModuleRootPaths(RuntimeModuleId.module("xslt-debugger-engine")));
    classPath.addAll(repository.getModuleRootPaths(RuntimeModuleId.moduleLibrary("xslt-debugger-engine", "RMI Stubs")));
    classPath.addAll(repository.getModuleRootPaths(RuntimeModuleId.projectLibrary("Trove4j")));

    final String type = parameters.getVMParametersList().getPropertyValue("xslt.transformer.type");
    RuntimeModuleId saxonId = RuntimeModuleId.moduleLibrary("xslt-debugger-engine-impl", "Saxon-6.5.5");
    RuntimeModuleId saxon9Id = RuntimeModuleId.moduleLibrary("xslt-debugger-engine-impl", "Saxon-9HE");
    if ("saxon".equalsIgnoreCase(type)) {
      classPath.addAll(repository.getModuleRootPaths(saxonId));
    } else if ("saxon9".equalsIgnoreCase(type)) {
      classPath.addAll(repository.getModuleRootPaths(saxon9Id));
    } else if ("xalan".equalsIgnoreCase(type)) {
      final Boolean xalanPresent = isValidXalanPresent(parameters);
      if (xalanPresent == null) {
        classPath.addAll(repository.getModuleRootPaths(RuntimeModuleId.moduleLibrary("xslt-debugger-engine-impl", "Xalan-2.7.1")));
      } else if (!xalanPresent) {
        throw new CantRunException("Unsupported Xalan version is present in classpath.");
      }
    } else if (type != null) {
      throw new CantRunException("Unsupported Transformer type '" + type + "'");
    } else if (classPath.getPathsString().toLowerCase().contains("xalan")) {
      if (isValidXalanPresent(parameters) == Boolean.TRUE) {
        parameters.getVMParametersList().defineProperty("xslt.transformer.type", "xalan");
      }
    }

    final VirtualFile xsltFile = configuration.findXsltFile();
    final PsiManager psiManager = PsiManager.getInstance(configuration.getProject());
    final XsltChecker.LanguageLevel level;
    if (xsltFile != null) {
      level = XsltSupport.getXsltLanguageLevel(psiManager.findFile(xsltFile));
    } else {
      level = XsltChecker.LanguageLevel.V1;
    }
    extensionData.putUserData(VERSION, level);

    if (!parameters.getVMParametersList().hasProperty("xslt.transformer.type")) {
      // add saxon for backward-compatibility
      if (level == XsltChecker.LanguageLevel.V2) {
        parameters.getVMParametersList().defineProperty("xslt.transformer.type", "saxon9");
        classPath.addAll(repository.getModuleRootPaths(saxon9Id));
      } else {
        parameters.getVMParametersList().defineProperty("xslt.transformer.type", "saxon");
        classPath.addAll(repository.getModuleRootPaths(saxonId));
      }
    }

    parameters.getVMParametersList().defineProperty("xslt.main", "org.intellij.plugins.xsltDebugger.rt.XSLTDebuggerMain");
  }

  @Nullable
  private static Boolean isValidXalanPresent(SimpleJavaParameters parameters) {
    final List<VirtualFile> files = parameters.getClassPath().getVirtualFiles();
    for (VirtualFile file : files) {
      if (file.getName().matches(".*xalan.*\\.jar")) {
        final VirtualFile root = JarFileSystem.getInstance().getJarRootForLocalFile(file);
        final VirtualFile manifestFile = root != null ? root.findFileByRelativePath("META-INF/MANIFEST.MF") : null;
        if (manifestFile != null) {
          try {
            Manifest manifest = manifestFile.getUserData(MANIFEST);
            if (manifest == null) {
              manifest = new Manifest(manifestFile.getInputStream());
              manifestFile.putUserData(MANIFEST, manifest);
            }
            Attributes attributes = manifest.getAttributes("org/apache/xalan/");
            if (attributes == null) {
              attributes = manifest.getAttributes("org/apache/xalan");
            }
            if (attributes == null) {
              LOG.info("No manifest attributes for 'org/apache/xalan/' in " + manifestFile.getPresentableUrl());
              continue;
            }
            final String version = attributes.getValue("Implementation-Version");
            if (version != null) {
              final String[] parts = version.split("\\.");
              if (parts.length >= 2) {
                if (Integer.parseInt(parts[0]) >= 2 && Integer.parseInt(parts[1]) >= 6) {
                  return true;
                }
              }
              LOG.info("Unsupported Xalan version: " + version);
            } else {
              LOG.info("No Xalan version information in " + file.getPath());
            }
          } catch (IOException e) {
            LOG.warn("Unable to read manifest from " + file.getName(), e);
          }
        } else {
          LOG.info("No manifest file in " + file.getPath());
        }
        return false;
      }
    }

    return null;
  }
}
