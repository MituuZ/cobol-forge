// This is a generated file. Not intended for manual editing.
package com.mituuz.cobolforge.psi.impl;

import com.mituuz.cobolforge.psi.CobolCopyStatement;
import com.mituuz.cobolforge.psi.CobolVisitor;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;

public class CobolCopyStatementImpl extends ASTWrapperPsiElement implements CobolCopyStatement {

  public CobolCopyStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CobolVisitor visitor) {
    visitor.visitCopyStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CobolVisitor) accept((CobolVisitor)visitor);
    else super.accept(visitor);
  }

}
