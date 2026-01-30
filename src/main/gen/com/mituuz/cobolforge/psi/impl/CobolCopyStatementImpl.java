// This is a generated file. Not intended for manual editing.
package com.mituuz.cobolforge.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.mituuz.cobolforge.psi.CobolTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.mituuz.cobolforge.psi.*;
import com.intellij.psi.PsiReference;

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

  @Override
  public @Nullable PsiElement getIdentifier() {
    return CobolPsiImplUtil.getIdentifier(this);
  }

  @Override
  public @Nullable PsiReference getReference() {
    return CobolPsiImplUtil.getReference(this);
  }

}
