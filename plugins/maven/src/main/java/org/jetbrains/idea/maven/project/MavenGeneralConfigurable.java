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
package org.jetbrains.idea.maven.project;

import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Sergey Evdokimov
 */
public abstract class MavenGeneralConfigurable extends MavenGeneralPanel implements SearchableConfigurable {
  protected abstract MavenGeneralSettings getState();

  @Override
  public boolean isModified() {
    MavenGeneralSettings formData = new MavenGeneralSettings();
    setData(formData);
    return !formData.equals(getState());
  }

  @Override
  public void apply() {
    setData(getState());
  }

  @Override
  public void reset() {
    getData(getState());
  }

  @Override
  @Nullable
  @NonNls
  public String getHelpTopic() {
    return "reference.settings.dialog.project.maven";
  }

  @Override
  @NotNull
  public String getId() {
    return getHelpTopic();
  }
}
