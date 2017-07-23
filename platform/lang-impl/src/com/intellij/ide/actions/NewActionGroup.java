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
package com.intellij.ide.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Dmitry Avdeev
 */
public class NewActionGroup extends ActionGroup {

  @NotNull
  @Override
  public AnAction[] getChildren(@Nullable AnActionEvent e) {
    AnAction[] actions = ((ActionGroup)ActionManager.getInstance().getAction(IdeActions.GROUP_WEIGHING_NEW)).getChildren(e);
    if (e == null || ActionPlaces.isMainMenuOrActionSearch(e.getPlace())) {
      AnAction newGroup = ActionManager.getInstance().getAction("NewProjectOrModuleGroup");
      AnAction[] newProjectActions = newGroup == null ? EMPTY_ARRAY : ((ActionGroup)newGroup).getChildren(e);
      return ArrayUtil.mergeArrays(newProjectActions, actions);
    }
    else {
      return actions;
    }
  }
}
