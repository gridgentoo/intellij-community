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
package com.intellij.codeInspection.dataFlow;

import com.intellij.codeInsight.NullableNotNullDialog;
import com.intellij.codeInspection.*;
import com.intellij.codeInspection.nullable.NullableStuffInspection;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.refactoring.util.RefactoringUtil;
import com.intellij.util.ui.JBUI;
import com.siyeh.ig.fixes.IntroduceVariableFix;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import static com.intellij.codeInspection.InspectionsBundle.message;
import static com.intellij.xml.util.XmlStringUtil.wrapInHtml;
import static javax.swing.SwingConstants.TOP;

public class DataFlowInspection extends DataFlowInspectionBase {
  @Override
  protected void addSurroundWithIfFix(PsiExpression qualifier, List<LocalQuickFix> fixes, boolean onTheFly) {
    if (onTheFly && SurroundWithIfFix.isAvailable(qualifier)) {
      fixes.add(new SurroundWithIfFix(qualifier));
    }
  }

  @Override
  protected LocalQuickFix[] createConditionalAssignmentFixes(boolean evaluatesToTrue, PsiAssignmentExpression assignment, final boolean onTheFly) {
    IElementType op = assignment.getOperationTokenType();
    boolean toRemove = op == JavaTokenType.ANDEQ && !evaluatesToTrue || op == JavaTokenType.OREQ && evaluatesToTrue;
    if (toRemove && !onTheFly) {
      return LocalQuickFix.EMPTY_ARRAY;
    }
    return new LocalQuickFix[]{toRemove ? new RemoveAssignmentFix() : createSimplifyToAssignmentFix()};
  }

  @Override
  public JComponent createOptionsPanel() {
    return new OptionsPanel();
  }

  @Override
  protected AddAssertStatementFix createAssertFix(PsiBinaryExpression binary, PsiExpression expression) {
    return RefactoringUtil.getParentStatement(expression, false) == null ? null : new AddAssertStatementFix(binary);
  }

  @Override
  protected LocalQuickFix createReplaceWithTrivialLambdaFix(Object value) {
    return new ReplaceWithTrivialLambdaFix(value);
  }

  @Override
  protected LocalQuickFix createIntroduceVariableFix(final PsiExpression expression) {
    return new IntroduceVariableFix(true);
  }

  @Override
  protected LocalQuickFix createNavigateToNullParameterUsagesFix(PsiParameter parameter) {
    return new NullableStuffInspection.NavigateToNullLiteralArguments(parameter);
  }

  private static JCheckBox createCheckBoxWithHTML(String text, boolean selected, Consumer<JCheckBox> consumer) {
    JCheckBox box = new JCheckBox(wrapInHtml(text));
    box.setVerticalTextPosition(TOP);
    box.setSelected(selected);
    box.getModel().addItemListener(event -> consumer.accept(box));
    return box;
  }

  private class OptionsPanel extends JPanel {
    private static final int BUTTON_OFFSET = 20;
    private final JButton myConfigureAnnotations;
    private final JCheckBox myIgnoreAssertions;
    private final JCheckBox myReportConstantReferences;
    private final JCheckBox mySuggestNullables;
    private final JCheckBox myDontReportTrueAsserts;
    private final JCheckBox myTreatUnknownMembersAsNullable;
    private final JCheckBox myReportNullArguments;
    private final JCheckBox myReportNullableMethodsReturningNotNull;
    private final JCheckBox myReportUncheckedOptionals;

    private OptionsPanel() {
      super(new GridBagLayout());

      GridBagConstraints gc = new GridBagConstraints();
      gc.weighty = 0;
      gc.weightx = 1;
      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.anchor = GridBagConstraints.NORTHWEST;

      mySuggestNullables = createCheckBoxWithHTML(
        message("inspection.data.flow.nullable.quickfix.option"),
        SUGGEST_NULLABLE_ANNOTATIONS, box -> SUGGEST_NULLABLE_ANNOTATIONS = box.isSelected());

      myDontReportTrueAsserts = createCheckBoxWithHTML(
        message("inspection.data.flow.true.asserts.option"),
        DONT_REPORT_TRUE_ASSERT_STATEMENTS, box -> DONT_REPORT_TRUE_ASSERT_STATEMENTS = box.isSelected());

      myIgnoreAssertions = createCheckBoxWithHTML(
        "Ignore assert statements",
        IGNORE_ASSERT_STATEMENTS, box -> IGNORE_ASSERT_STATEMENTS = box.isSelected());

      myReportConstantReferences = createCheckBoxWithHTML(
        "Warn when reading a value guaranteed to be constant",
        REPORT_CONSTANT_REFERENCE_VALUES, box -> REPORT_CONSTANT_REFERENCE_VALUES = box.isSelected());

      myTreatUnknownMembersAsNullable = createCheckBoxWithHTML(
        "Treat non-annotated members and parameters as @Nullable",
        TREAT_UNKNOWN_MEMBERS_AS_NULLABLE, box -> TREAT_UNKNOWN_MEMBERS_AS_NULLABLE = box.isSelected());

      myReportNullArguments = createCheckBoxWithHTML(
        "Report not-null required parameter with null-literal argument usages",
        REPORT_NULLS_PASSED_TO_NOT_NULL_PARAMETER, box -> REPORT_NULLS_PASSED_TO_NOT_NULL_PARAMETER = box.isSelected());

      myReportNullableMethodsReturningNotNull = createCheckBoxWithHTML(
        "Report nullable methods that always return a non-null value",
        REPORT_NULLABLE_METHODS_RETURNING_NOT_NULL, box -> REPORT_NULLABLE_METHODS_RETURNING_NOT_NULL = box.isSelected());

      myReportUncheckedOptionals = createCheckBoxWithHTML(
        "Report Optional.get() calls without previous isPresent check",
        REPORT_UNCHECKED_OPTIONALS, box -> REPORT_UNCHECKED_OPTIONALS = box.isSelected());

      gc.insets = JBUI.emptyInsets();
      gc.gridy = 0;
      add(mySuggestNullables, gc);

      myConfigureAnnotations = NullableNotNullDialog.createConfigureAnnotationsButton(this);
      gc.gridy++;
      gc.fill = GridBagConstraints.NONE;
      gc.insets.left = BUTTON_OFFSET;
      gc.insets.bottom = 15;
      add(myConfigureAnnotations, gc);

      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.weighty = 1;
      gc.insets.left = 0;
      gc.gridy++;
      add(myDontReportTrueAsserts, gc);

      gc.gridy++;
      add(myIgnoreAssertions, gc);

      gc.gridy++;
      add(myReportConstantReferences, gc);

      gc.gridy++;
      add(myTreatUnknownMembersAsNullable, gc);

      gc.gridy++;
      add(myReportNullArguments, gc);

      gc.gridy++;
      add(myReportNullableMethodsReturningNotNull, gc);

      gc.gridy++;
      add(myReportUncheckedOptionals, gc);
    }

    @Override
    public Dimension getPreferredSize() {
      Dimension preferred = super.getPreferredSize();
      if (!isPreferredSizeSet()) {
        // minimize preferred width to align HTML text within ScrollPane
        Dimension size = myConfigureAnnotations.getPreferredSize();
        preferred.width = size.width + BUTTON_OFFSET;
      }
      return preferred;
    }
  }
}