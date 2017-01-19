/*
 * Copyright 2000-2016 JetBrains s.r.o.
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
package com.intellij.debugger.memory.ui;

import com.intellij.debugger.DebuggerManager;
import com.intellij.debugger.memory.component.InstancesTracker;
import com.intellij.debugger.memory.component.MemoryViewDebugProcessData;
import com.intellij.debugger.memory.event.InstancesTrackerListener;
import com.intellij.debugger.memory.tracking.TrackingType;
import com.intellij.debugger.memory.utils.StackFrameItem;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.labels.ActionLink;
import com.intellij.xdebugger.XDebugSession;
import com.sun.jdi.ObjectReference;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class InstancesWithStackFrameView {
  private static final float DEFAULT_SPLITTER_PROPORTION = 0.7f;
  private static final String EMPTY_TEXT_WHEN_ITEM_NOT_SELECTED = "Select instance to see stack frame";
  private static final String EMPTY_TEXT_WHEN_STACK_NOT_FOUND = "No stack frame for this instance";
  private static final String TEXT_FOR_ARRAYS = "Arrays could not be tracked";
  private static final List<StackFrameItem> EMPTY_FRAME = Collections.emptyList();

  private float myHidedProportion;

  private final JBSplitter mySplitter = new JBSplitter(false, DEFAULT_SPLITTER_PROPORTION);
  private boolean myIsHided = false;

  InstancesWithStackFrameView(@NotNull XDebugSession debugSession, @NotNull InstancesTree tree,
                              @NotNull StackFrameList list, String className) {
    mySplitter.setFirstComponent(new JBScrollPane(tree));

    list.setEmptyText(EMPTY_TEXT_WHEN_ITEM_NOT_SELECTED);
    JLabel stackTraceLabel;
    if (isArrayType(className)) {
      stackTraceLabel = new JBLabel(TEXT_FOR_ARRAYS, SwingConstants.CENTER);
    }
    else {
      ActionLink actionLink = new ActionLink("Enable tracking for new instances",
                                             AllIcons.Debugger.MemoryView.ClassTracked,
                                             new AnAction() {
                                               @Override
                                               public void actionPerformed(AnActionEvent e) {
                                                 InstancesTracker.getInstance(debugSession.getProject())
                                                   .add(className, TrackingType.CREATION);
                                               }
                                             });

      actionLink.setHorizontalAlignment(SwingConstants.CENTER);
      actionLink.setPaintUnderline(false);
      stackTraceLabel = actionLink;
    }

    mySplitter.setSplitterProportionKey("InstancesWithStackFrameView.SplitterKey");

    JComponent stackComponent = new JBScrollPane(list);

    InstancesTracker instancesTracker = InstancesTracker.getInstance(debugSession.getProject());
    instancesTracker.addTrackerListener(new InstancesTrackerListener() {
      @Override
      public void classChanged(@NotNull String name, @NotNull TrackingType type) {
        if (Objects.equals(className, name) && type == TrackingType.CREATION) {
          mySplitter.setSecondComponent(stackComponent);
        }
      }

      @Override
      public void classRemoved(@NotNull String name) {
        if (Objects.equals(name, className)) {
          mySplitter.setSecondComponent(stackTraceLabel);
        }
      }
    }, tree);

    mySplitter.setSecondComponent(instancesTracker.isTracked(className) ? stackComponent : stackTraceLabel);

    mySplitter.setHonorComponentsMinimumSize(false);
    myHidedProportion = DEFAULT_SPLITTER_PROPORTION;

    final MemoryViewDebugProcessData data =
      DebuggerManager.getInstance(debugSession.getProject()).getDebugProcess(debugSession.getDebugProcess().getProcessHandler())
        .getUserData(MemoryViewDebugProcessData.KEY);
    tree.addTreeSelectionListener(e -> {
      ObjectReference ref = tree.getSelectedReference();
      if (ref != null && data != null) {
        List<StackFrameItem> stack = data.getTrackedStacks().getStack(ref);
        if (stack != null) {
          list.setFrame(stack);
          if (mySplitter.getProportion() == 1.f) {
            mySplitter.setProportion(DEFAULT_SPLITTER_PROPORTION);
          }
          return;
        }
        list.setEmptyText(EMPTY_TEXT_WHEN_STACK_NOT_FOUND);
      }
      else {
        list.setEmptyText(EMPTY_TEXT_WHEN_ITEM_NOT_SELECTED);
      }

      list.setFrame(EMPTY_FRAME);
    });
  }

  JComponent getComponent() {
    return mySplitter;
  }

  private static boolean isArrayType(@NotNull String className) {
    return className.contains("[]");
  }

  @SuppressWarnings("unused")
  private void hideStackFrame() {
    if (!myIsHided) {
      myHidedProportion = mySplitter.getProportion();
      mySplitter.getSecondComponent().setVisible(false);
      mySplitter.setProportion(1.f);
      myIsHided = true;
    }
  }

  @SuppressWarnings("unused")
  private void showStackFrame() {
    if (myIsHided) {
      mySplitter.getSecondComponent().setVisible(true);
      mySplitter.setProportion(myHidedProportion);
      myIsHided = false;
    }
  }
}
