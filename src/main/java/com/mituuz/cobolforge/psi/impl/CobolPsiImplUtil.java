package com.mituuz.cobolforge.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.PsiElementResolveResult;
import com.mituuz.cobolforge.CobolCopyResolver;
import com.mituuz.cobolforge.psi.CobolCopyStatement;
import com.mituuz.cobolforge.psi.CobolTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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

    public static @Nullable PsiReference getReference(CobolCopyStatement o) {
        PsiElement identifier = getIdentifier(o);
        if (identifier == null) {
            return null;
        }

        return new PsiPolyVariantReferenceBase<>(o, identifier.getTextRangeInParent()) {
            @Override
            public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
                String filename = identifier.getText();
                return CobolCopyResolver.fetchFiles(filename, o.getProject()).stream()
                        .map(virtualFile -> PsiManager.getInstance(o.getProject()).findFile(virtualFile))
                        .filter(Objects::nonNull)
                        .map(PsiElementResolveResult::new)
                        .toArray(ResolveResult[]::new);
            }

            @Override
            public @Nullable PsiElement resolve() {
                ResolveResult[] resolveResults = multiResolve(false);
                return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
            }

            @Override
            public Object @NotNull [] getVariants() {
                return new Object[0];
            }
        };
    }
}
