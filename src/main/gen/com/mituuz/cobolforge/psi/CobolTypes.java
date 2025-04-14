// This is a generated file. Not intended for manual editing.
package com.mituuz.cobolforge.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.mituuz.cobolforge.psi.impl.CobolCopyStatementImpl;

public interface CobolTypes {

  IElementType COPY_STATEMENT = new CobolElementType("COPY_STATEMENT");

  IElementType COMMENT = new CobolTokenType("COMMENT");
  IElementType COPY = new CobolTokenType("COPY");
  IElementType CRLF = new CobolTokenType("CRLF");
  IElementType DOT = new CobolTokenType("DOT");
  IElementType IDENTIFIER = new CobolTokenType("IDENTIFIER");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == COPY_STATEMENT) {
        return new CobolCopyStatementImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
