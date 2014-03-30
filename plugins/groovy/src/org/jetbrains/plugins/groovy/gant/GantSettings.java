/*
 * Copyright 2000-2012 JetBrains s.r.o.
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
package org.jetbrains.plugins.groovy.gant;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.groovy.util.SdkHomeSettings;

/**
 * @author peter
 */
@State(
    name = "GantSettings",
    storages = {
      @Storage(file = StoragePathMacros.PROJECT_FILE),
      @Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/gant_config.xml", scheme = StorageScheme.DIRECTORY_BASED)
    }
)
public class GantSettings extends SdkHomeSettings {
  public GantSettings(Project project) {
    super(project);
  }

  public static GantSettings getInstance(Project project) {
    return ServiceManager.getService(project, GantSettings.class);
  }
}
