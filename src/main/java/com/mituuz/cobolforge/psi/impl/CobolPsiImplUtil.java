package com.mituuz.cobolforge.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.mituuz.cobolforge.psi.CobolCopyStatement;
import com.mituuz.cobolforge.psi.CobolTypes;
import org.jetbrains.annotations.Nullable;

public final class CobolPsiImplUtil {
    private CobolPsiImplUtil() {
    }

    public static @Nullable PsiElement getIdentifier(CobolCopyStatement o) {
        for (PsiElement child = o.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNode() != null && child.getNode().getElementType() == CobolTypes.IDENTIFIER) {
                return child;
            }
        }
        return null;
    }
}
