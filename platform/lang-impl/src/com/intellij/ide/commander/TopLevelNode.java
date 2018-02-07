/*
 * Copyright 2000-2009 JetBrains s.r.o.
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
package com.intellij.ide.commander;

import com.intellij.icons.AllIcons;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Eugene Zhuravlev
 */
public class TopLevelNode extends AbstractTreeNode {

  public TopLevelNode(Project project, Object value) {
    super(project, value);
    myName = "[ .. ]";
    setIcon(AllIcons.Nodes.UpLevel);
  }

  @Override
  @NotNull
  public Collection<AbstractTreeNode> getChildren() {
    return Collections.emptyList();
  }

  @Override
  protected void update(PresentationData presentation) {
  }

}
