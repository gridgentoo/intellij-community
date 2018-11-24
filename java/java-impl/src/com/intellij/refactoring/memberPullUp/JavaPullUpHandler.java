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

package com.intellij.refactoring.memberPullUp;

import com.intellij.lang.ContextAwareActionHandler;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.classMembers.MemberInfoBase;
import com.intellij.refactoring.lang.ElementsHandler;
import com.intellij.refactoring.ui.ConflictsDialog;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.refactoring.util.RefactoringHierarchyUtil;
import com.intellij.refactoring.util.classMembers.MemberInfo;
import com.intellij.refactoring.util.classMembers.MemberInfoStorage;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.MultiMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JavaPullUpHandler implements RefactoringActionHandler, PullUpDialog.Callback, ElementsHandler, ContextAwareActionHandler {
  public static final String REFACTORING_NAME = RefactoringBundle.message("pull.members.up.title");

  private PsiClass mySubclass;
  private Project myProject;

  @Override
  public boolean isAvailableForQuickList(@NotNull Editor editor, @NotNull PsiFile file, @NotNull DataContext dataContext) {
    return !getElements(editor, file).isEmpty();
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file, DataContext dataContext) {
    editor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);

    List<PsiElement> elements = getElements(editor, file);
    if (elements.isEmpty()) {
      String message = RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("the.caret.should.be.positioned.inside.a.class.to.pull.members.from"));
      CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.MEMBERS_PULL_UP);
    }
    else {
      invoke(project, elements.toArray(PsiElement.EMPTY_ARRAY), dataContext);
    }
  }

  private static List<PsiElement> getElements(Editor editor, PsiFile file) {
    List<PsiElement> elements = ContainerUtil.newSmartList();
    for (Caret caret : editor.getCaretModel().getAllCarets()) {
      int offset = caret.getOffset();
      PsiElement element = file.findElementAt(offset);
      while (element != null && !(element instanceof PsiFile)) {
        if (element instanceof PsiClass || element instanceof PsiField || element instanceof PsiMethod) {
          elements.add(element);
          break;
        }
        element = element.getParent();
      }
    }
    return elements;
  }

  @Override
  public void invoke(@NotNull final Project project, @NotNull PsiElement[] elements, DataContext dataContext) {
    myProject = project;
    PsiClass aClass = PsiTreeUtil.getParentOfType(PsiTreeUtil.findCommonParent(elements), PsiClass.class, false);
    invoke(project, dataContext, aClass, elements);
  }

  private void invoke(Project project, DataContext dataContext, PsiClass aClass, PsiElement... selectedMembers) {
    final Editor editor = dataContext != null ? CommonDataKeys.EDITOR.getData(dataContext) : null;
    if (aClass == null) {
      String message =
        RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("is.not.supported.in.the.current.context", REFACTORING_NAME));
      CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.MEMBERS_PULL_UP);
      return;
    }

    List<PsiClass> bases = RefactoringHierarchyUtil.createBasesList(aClass, false, true);
    if (bases.isEmpty()) {
      final PsiClass containingClass = aClass.getContainingClass();
      if (containingClass != null) {
        invoke(project, dataContext, containingClass, aClass);
        return;
      }
      String message = RefactoringBundle.getCannotRefactorMessage(
        RefactoringBundle.message("class.does.not.have.base.classes.interfaces.in.current.project", aClass.getQualifiedName()));
      CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.MEMBERS_PULL_UP);
      return;
    }

    if (!CommonRefactoringUtil.checkReadOnlyStatus(project, aClass)) return;
    mySubclass = aClass;
    MemberInfoStorage memberInfoStorage = new MemberInfoStorage(mySubclass, element -> true);
    List<MemberInfo> members = memberInfoStorage.getClassMemberInfos(mySubclass);

    for (MemberInfoBase<PsiMember> member : members) {
      for (PsiElement aMember : selectedMembers) {
        if (PsiTreeUtil.isAncestor(member.getMember(), aMember, false)) {
          member.setChecked(true);
          break;
        }
      }
    }

    new PullUpDialog(project, aClass, bases, memberInfoStorage, this).show();
  }

  @Override
  public boolean checkConflicts(final PullUpDialog dialog) {
    final List<MemberInfo> infos = dialog.getSelectedMemberInfos();
    final MemberInfo[] memberInfos = infos.toArray(new MemberInfo[infos.size()]);
    final PsiClass superClass = dialog.getSuperClass();
    if (superClass == null || !checkWritable(superClass, memberInfos)) return false;
    final MultiMap<PsiElement, String> conflicts = new MultiMap<>();
    if (!ProgressManager.getInstance().runProcessWithProgressSynchronously(() -> ApplicationManager.getApplication().runReadAction(() -> {
      final PsiDirectory targetDirectory = superClass.getContainingFile().getContainingDirectory();
      final PsiPackage targetPackage = targetDirectory != null ? JavaDirectoryService.getInstance().getPackage(targetDirectory) : null;
      if (targetDirectory != null && targetPackage != null) {
        conflicts.putAllValues(PullUpConflictsUtil.checkConflicts(memberInfos, mySubclass, superClass, targetPackage, targetDirectory, dialog.getContainmentVerifier()));
      }
    }), RefactoringBundle.message("detecting.possible.conflicts"), true, myProject)) return false;
    if (!conflicts.isEmpty()) {
      ConflictsDialog conflictsDialog = new ConflictsDialog(myProject, conflicts);
      boolean ok = conflictsDialog.showAndGet();
      if (!ok && conflictsDialog.isShowConflicts()) dialog.close(DialogWrapper.CANCEL_EXIT_CODE);
      return ok;
    }
    return true;
  }

  private boolean checkWritable(PsiClass superClass, MemberInfo[] infos) {
    if (!CommonRefactoringUtil.checkReadOnlyStatus(myProject, superClass)) return false;
    for (MemberInfo info : infos) {
      if (info.getMember() instanceof PsiClass && info.getOverrides() != null) continue;
      if (!CommonRefactoringUtil.checkReadOnlyStatus(myProject, info.getMember())) return false;
    }
    return true;
  }

  @Override
  public boolean isEnabledOnElements(PsiElement[] elements) {
    // todo: multiple selection etc
    return elements.length == 1 && elements[0] instanceof PsiClass;
  }
}