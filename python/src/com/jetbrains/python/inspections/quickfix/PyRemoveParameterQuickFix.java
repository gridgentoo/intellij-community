/*
 * Copyright 2000-2013 JetBrains s.r.o.
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
package com.jetbrains.python.inspections.quickfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.python.PyBundle;
import com.jetbrains.python.PyTokenTypes;
import com.jetbrains.python.documentation.PyDocumentationSettings;
import com.jetbrains.python.editor.PythonDocCommentUtil;
import com.jetbrains.python.psi.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class PyRemoveParameterQuickFix implements LocalQuickFix {

  @NotNull
  public String getName() {
    return PyBundle.message("QFIX.NAME.remove.parameter");
  }

  @NonNls
  @NotNull
  public String getFamilyName() {
    return getName();
  }

  public void applyFix(@NotNull final Project project, @NotNull final ProblemDescriptor descriptor) {
    final PsiElement element = descriptor.getPsiElement();
    assert element instanceof PyParameter;

    final PyFunction pyFunction = PsiTreeUtil.getParentOfType(element, PyFunction.class);
    final PsiElement nextSibling = PsiTreeUtil.skipSiblingsForward(element, PsiWhiteSpace.class);
    final PsiElement prevSibling = PsiTreeUtil.skipSiblingsBackward(element, PsiWhiteSpace.class);
    element.delete();
    if (nextSibling != null && nextSibling.getNode().getElementType().equals(PyTokenTypes.COMMA)) {
      nextSibling.delete();
      return;
    }
    if (prevSibling != null && prevSibling.getNode().getElementType().equals(PyTokenTypes.COMMA)) {
      prevSibling.delete();
    }

    if (pyFunction != null) {
      final PyStringLiteralExpression expression = pyFunction.getDocStringExpression();
      final String paramName = ((PyParameter)element).getName();
      if (expression != null && paramName != null) {
        final Module module = ModuleUtilCore.findModuleForPsiElement(pyFunction);
        assert module != null;
        PyDocumentationSettings documentationSettings = PyDocumentationSettings.getInstance(module);
        String prefix = documentationSettings.isEpydocFormat(pyFunction.getContainingFile()) ? "@" : ":";
        final String replacement = PythonDocCommentUtil.removeParamFromDocstring(expression.getText(), prefix, paramName);
        PyExpression str =
          PyElementGenerator.getInstance(project).createDocstring(replacement).getExpression();
        expression.replace(str);
      }
    }
  }
}
