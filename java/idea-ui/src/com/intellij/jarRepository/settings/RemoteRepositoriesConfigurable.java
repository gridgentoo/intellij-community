/*
 * Copyright 2000-2017 JetBrains s.r.o.
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
package com.intellij.jarRepository.settings;

import com.intellij.jarRepository.JarRepositoryManager;
import com.intellij.jarRepository.RemoteRepositoriesConfiguration;
import com.intellij.jarRepository.RemoteRepositoryDescription;
import com.intellij.jarRepository.services.MavenRepositoryServicesManager;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.ListUtil;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RemoteRepositoriesConfigurable extends BaseConfigurable implements SearchableConfigurable, Configurable.NoScroll {

  private JPanel myMainPanel;

  private JBList<String> myServiceList;
  private JButton myAddServiceButton;
  private JButton myEditServiceButton;
  private JButton myRemoveServiceButton;
  private JButton myTestServiceButton;

  private JBList<RemoteRepositoryDescription> myJarRepositoryList;
  private JButton myAddRepoButton;
  private JButton myEditRepoButton;
  private JButton myRemoveRepoButton;
  private JButton myResetToDefaultReposButton;
  private JButton myResetToDefaultServicesButton;

  private final Project myProject;
  private final CollectionListModel<String> myServicesModel = new CollectionListModel<>();
  private final CollectionListModel<RemoteRepositoryDescription> myReposModel = new CollectionListModel<>();

  public RemoteRepositoriesConfigurable(Project project) {
    myProject = project;
    configControls();
  }

  @Override
  public boolean isModified() { 
    return isServiceListModified() || isRepoListModified();
  }

  private boolean isServiceListModified() {
    return !myServicesModel.getItems().equals(MavenRepositoryServicesManager.getInstance(myProject).getUrls());
  }

  private boolean isRepoListModified() {
    final List<RemoteRepositoryDescription> repos = RemoteRepositoriesConfiguration.getInstance(myProject).getRepositories();
    return !myReposModel.getItems().equals(repos);
  }

  private void configControls() {
    setupListControls(
      myServiceList, myServicesModel, myAddServiceButton, myEditServiceButton, myRemoveServiceButton,
      "Artifactory, Nexus or Bintray Service URLs", "Service URL", "No services", DataAdapter.STRING_ADAPTER
    );
    setupListControls(
      myJarRepositoryList, myReposModel, myAddRepoButton, myEditRepoButton, myRemoveRepoButton,
      "Maven Repository URL", "maven Repository URL", "No remote repositories", DataAdapter.REPOSITORY_DESCRIPTION_ADAPTER
    );

    ListUtil.disableWhenNoSelection(myTestServiceButton, myServiceList);
    myTestServiceButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final String value = myServiceList.getSelectedValue();
        if (!StringUtil.isEmpty(value)) {
          myTestServiceButton.setEnabled(false);
          JarRepositoryManager.searchRepositories(myProject, Collections.singletonList(value), infos -> {
            myTestServiceButton.setEnabled(true);
            if (infos.isEmpty()) {
              Messages.showMessageDialog("No repositories found", "Service Connection Failed", Messages.getWarningIcon());
            }
            else {
              final StringBuilder sb = new StringBuilder();
              sb.append(infos.size()).append(infos.size() == 1 ? "repository" : " repositories").append(" found");
              //for (MavenRepositoryInfo info : infos) {
              //  sb.append("\n  ");
              //  sb.append(info.getId()).append(" (").append(info.getName()).append(")").append(": ").append(info.getUrl());
              //}
              Messages.showMessageDialog(sb.toString(), "Service Connection Successful", Messages.getInformationIcon());
            }
            return true;
          });
        }
      }
    });
    myResetToDefaultReposButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        resetReposModel(RemoteRepositoryDescription.DEFAULT_REPOSITORIES);
      }
    });
    myResetToDefaultServicesButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        resetServicesModel(MavenRepositoryServicesManager.DEFAULT_SERVICES);
      }
    });
  }

  private interface DataAdapter<Data, Presentation> {
    DataAdapter<String, String> STRING_ADAPTER = new DataAdapter<String, String>() {
      @Override
      public String toPresentation(String s) {
        return s;
      }

      @Override
      public String create(String s) {
        return s;
      }

      @Override
      public String change(String current, String changes) {
        return changes;
      }
    };

    DataAdapter<RemoteRepositoryDescription, String> REPOSITORY_DESCRIPTION_ADAPTER = new DataAdapter<RemoteRepositoryDescription, String>() {
      @Override
      public String toPresentation(RemoteRepositoryDescription description) {
        return description.getUrl();
      }

      @Override
      public RemoteRepositoryDescription create(String url) {
        final UUID uuid = UUID.randomUUID();
        return new RemoteRepositoryDescription(uuid.toString(), uuid.toString(), url);
      }

      @Override
      public RemoteRepositoryDescription change(RemoteRepositoryDescription current, String url) {
        return new RemoteRepositoryDescription(current.getId(), current.getName(), url);
      }
    };
    Presentation toPresentation(Data data);
    Data create(Presentation presentation);
    Data change(Data current, Presentation changes);
  }

  private static <T> void setupListControls(final JBList<T> list,
                                            final CollectionListModel<T> model,
                                            final JButton addButton,
                                            final JButton editButton,
                                            final JButton removeButton,
                                            final String modificationDialogTitle,
                                            final String modificationDialogHint,
                                            final String emptyListHint, DataAdapter<T, String> adapter) {
    list.setModel(model);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setCellRenderer(new ListCellRendererWrapper<T>() {
      @Override
      public void customize(JList list, T value, int index, boolean selected, boolean hasFocus) {
        setText(adapter.toPresentation(value));
      }
    });
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final T value = list.getSelectedValue();
        final String text = Messages.showInputDialog(
          modificationDialogTitle, "Add" + " " + modificationDialogHint, Messages.getQuestionIcon(), value == null ? "https://" : adapter.toPresentation(value), new URLInputVaslidator()
        );
        if (StringUtil.isNotEmpty(text)) {
          model.add(adapter.create(text));
          list.setSelectedValue(text, true);
        }
      }
    });
    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final int index = list.getSelectedIndex();
        final T element = model.getElementAt(index);
        final String text = Messages.showInputDialog(
          modificationDialogTitle, "Edit" + " " + modificationDialogHint, Messages.getQuestionIcon(), adapter.toPresentation(element), new URLInputVaslidator()
        );
        if (StringUtil.isNotEmpty(text)) {
          model.setElementAt(adapter.change(element, text), index);
        }
      }
    });
    ListUtil.addRemoveListener(removeButton, list);
    ListUtil.disableWhenNoSelection(editButton, list);
    list.getEmptyText().setText(emptyListHint);
  }


  public String getDisplayName() {
    return "Remote Jar Repositories";
  }

  @Override
  public String getHelpTopic() {
    return "reference.jar.repositories"; 
  }

  @NotNull
  public String getId() {
    return getClass().getName();
  }

  public JComponent createComponent() {
    return myMainPanel;
  }

  public void apply() throws ConfigurationException {
    MavenRepositoryServicesManager.getInstance(myProject).setUrls(myServicesModel.getItems());
    RemoteRepositoriesConfiguration.getInstance(myProject).setRepositories(myReposModel.getItems());
  }

  public void reset() {
    resetServicesModel(MavenRepositoryServicesManager.getInstance(myProject).getUrls());
    resetReposModel(RemoteRepositoriesConfiguration.getInstance(myProject).getRepositories());
  }

  private void resetServicesModel(final List<String> urls) {
    myServicesModel.removeAll();
    myServicesModel.add(urls);
  }

  private void resetReposModel(final List<RemoteRepositoryDescription> repositories) {
    myReposModel.replaceAll(repositories);
  }

  private static final class URLInputVaslidator implements InputValidator {
    @Override
    public boolean checkInput(String inputString) {
      try {
        return StringUtil.isNotEmpty(new URL(inputString).getHost());
      }
      catch (MalformedURLException e) {
        return false;
      }
    }

    @Override
    public boolean canClose(String inputString) {
      return checkInput(inputString);
    }
  }
}
