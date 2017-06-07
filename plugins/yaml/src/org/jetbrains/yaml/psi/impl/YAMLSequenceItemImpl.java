package org.jetbrains.yaml.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLTokenTypes;
import org.jetbrains.yaml.psi.*;

import java.util.Collection;
import java.util.Collections;

/**
 * @author oleg
 */
public class YAMLSequenceItemImpl extends YAMLPsiElementImpl implements YAMLSequenceItem {
  public YAMLSequenceItemImpl(@NotNull final ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public YAMLValue getValue() {
    return PsiTreeUtil.findChildOfType(this, YAMLValue.class);
  }

  @NotNull
  public Collection<YAMLKeyValue> getKeysValues() {
    final YAMLMapping mapping = PsiTreeUtil.findChildOfType(this, YAMLMapping.class);
    if (mapping == null) {
      return Collections.emptyList();
    }
    else {
      return mapping.getKeyValues();
    }
  }

  @Override
  public String toString() {
    return "YAML sequence item";
  }

  @Nullable
  @Override
  public PsiElement getAnchor() {
    YAMLValue value = getValue();

    if (value != null) {
      for (PsiElement child = value.getFirstChild(); child != null; child = child.getNextSibling()) {
        if (child.getNode().getElementType() == YAMLTokenTypes.ANCHOR) {
          return child;
        }
      }
    }

    return null;
  }
}
