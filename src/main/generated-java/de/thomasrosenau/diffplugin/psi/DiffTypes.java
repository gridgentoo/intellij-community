// This is a generated file. Not intended for manual editing.
package de.thomasrosenau.diffplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import de.thomasrosenau.diffplugin.psi.impl.*;

public interface DiffTypes {

  IElementType CONSOLE_COMMAND = new DiffElementType("CONSOLE_COMMAND");
  IElementType CONTEXT_HUNK = new DiffElementType("CONTEXT_HUNK");
  IElementType CONTEXT_HUNK_FROM = new DiffElementType("CONTEXT_HUNK_FROM");
  IElementType CONTEXT_HUNK_TO = new DiffElementType("CONTEXT_HUNK_TO");
  IElementType NORMAL_HUNK = new DiffElementType("NORMAL_HUNK");
  IElementType UNIFIED_HUNK = new DiffElementType("UNIFIED_HUNK");

  IElementType COMMAND = new DiffTokenType("COMMAND");
  IElementType CONTEXT_CHANGED_LINE = new DiffTokenType("CONTEXT_CHANGED_LINE");
  IElementType CONTEXT_COMMON_LINE = new DiffTokenType("CONTEXT_COMMON_LINE");
  IElementType CONTEXT_DELETED_LINE = new DiffTokenType("CONTEXT_DELETED_LINE");
  IElementType CONTEXT_FROM_LABEL = new DiffTokenType("CONTEXT_FROM_LABEL");
  IElementType CONTEXT_FROM_LINE_NUMBERS = new DiffTokenType("CONTEXT_FROM_LINE_NUMBERS");
  IElementType CONTEXT_HUNK_SEPARATOR = new DiffTokenType("CONTEXT_HUNK_SEPARATOR");
  IElementType CONTEXT_INSERTED_LINE = new DiffTokenType("CONTEXT_INSERTED_LINE");
  IElementType CONTEXT_TO_LABEL = new DiffTokenType("CONTEXT_TO_LABEL");
  IElementType CONTEXT_TO_LINE_NUMBERS = new DiffTokenType("CONTEXT_TO_LINE_NUMBERS");
  IElementType EOL_HINT = new DiffTokenType("EOL_HINT");
  IElementType NORMAL_ADD_COMMAND = new DiffTokenType("NORMAL_ADD_COMMAND");
  IElementType NORMAL_CHANGE_COMMAND = new DiffTokenType("NORMAL_CHANGE_COMMAND");
  IElementType NORMAL_DELETE_COMMAND = new DiffTokenType("NORMAL_DELETE_COMMAND");
  IElementType NORMAL_FROM_LINE = new DiffTokenType("NORMAL_FROM_LINE");
  IElementType NORMAL_SEPARATOR = new DiffTokenType("NORMAL_SEPARATOR");
  IElementType NORMAL_TO_LINE = new DiffTokenType("NORMAL_TO_LINE");
  IElementType OTHER = new DiffTokenType("OTHER");
  IElementType UNIFIED_COMMON_LINE = new DiffTokenType("UNIFIED_COMMON_LINE");
  IElementType UNIFIED_DELETED_LINE = new DiffTokenType("UNIFIED_DELETED_LINE");
  IElementType UNIFIED_FROM_LABEL = new DiffTokenType("UNIFIED_FROM_LABEL");
  IElementType UNIFIED_INSERTED_LINE = new DiffTokenType("UNIFIED_INSERTED_LINE");
  IElementType UNIFIED_LINE_NUMBERS = new DiffTokenType("UNIFIED_LINE_NUMBERS");
  IElementType UNIFIED_TO_LABEL = new DiffTokenType("UNIFIED_TO_LABEL");
  IElementType WHITE_SPACE = new DiffTokenType("WHITE_SPACE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == CONSOLE_COMMAND) {
        return new DiffConsoleCommandImpl(node);
      }
      else if (type == CONTEXT_HUNK) {
        return new DiffContextHunkImpl(node);
      }
      else if (type == CONTEXT_HUNK_FROM) {
        return new DiffContextHunkFromImpl(node);
      }
      else if (type == CONTEXT_HUNK_TO) {
        return new DiffContextHunkToImpl(node);
      }
      else if (type == NORMAL_HUNK) {
        return new DiffNormalHunkImpl(node);
      }
      else if (type == UNIFIED_HUNK) {
        return new DiffUnifiedHunkImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
