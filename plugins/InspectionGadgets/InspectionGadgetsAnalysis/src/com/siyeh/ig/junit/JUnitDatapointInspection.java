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
package com.siyeh.ig.junit;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.siyeh.InspectionGadgetsBundle;
import com.siyeh.ig.BaseInspection;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.InspectionGadgetsFix;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JUnitDatapointInspection extends BaseInspection {
  public static final String DATAPOINT_FQN = "org.junit.experimental.theories.DataPoint";


  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return InspectionGadgetsBundle.message("junit.datapoint.display.name");
  }

  @NotNull
  @Override
  protected String buildErrorString(Object... infos) {
    return (String)infos[0];
  }

  @Nullable
  @Override
  protected InspectionGadgetsFix buildFix(Object... infos) {
    return infos.length > 1 ? new MakePublicStaticFix((String)infos[1], true) : null;
  }

  @Override
  public BaseInspectionVisitor buildVisitor() {
    return new BaseInspectionVisitor() {
      @Override
      public void visitMethod(PsiMethod method) {
        visitMember(method, "method");
      }

      @Override
      public void visitField(PsiField field) {
        visitMember(field, "field");
      }

      private <T extends PsiMember & PsiNameIdentifierOwner> void visitMember(T member,
                                                                              final String memberDescription) {
        final boolean dataPointAnnotated = AnnotationUtil.isAnnotated(member, DATAPOINT_FQN, false);
        if (dataPointAnnotated) {
          final String errorMessage = JUnitRuleInspection.getPublicStaticErrorMessage(member, false, true);
          if (errorMessage != null) {
            final PsiElement identifier = member.getNameIdentifier();
            registerError(identifier != null ? identifier : member,
                          InspectionGadgetsBundle.message("junit.datapoint.problem.descriptor", errorMessage, StringUtil.capitalize(memberDescription)),
                          "Make " + memberDescription + " " + errorMessage, DATAPOINT_FQN);
          }
        }
      }
    };
  }
}
