package com.mituuz.cobolforge.docs;

import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.DocumentationTargetProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.mituuz.cobolforge.psi.CobolCopyStatement;
import com.mituuz.cobolforge.psi.CobolTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CobolCopyDocumentationTargetProvider implements DocumentationTargetProvider {

    @Override
    public @NotNull List<? extends @NotNull DocumentationTarget> documentationTargets(@NotNull PsiFile file, int offset) {
        PsiElement contextElement = file.findElementAt(offset);
        if (contextElement == null) return List.of();

        PsiElement target = pickDocumentationElement(contextElement);
        if (target == null) return List.of();

        return List.of(new CobolCopyDocumentationTarget(target));
    }

    private static PsiElement pickDocumentationElement(@NotNull PsiElement contextElement) {
        // If the caret is on a relevant token, keep it.
        if (isCopyToken(contextElement)) {
            return contextElement;
        }

        // Otherwise, resolve the surrounding COPY statement and prefer its identifier.
        CobolCopyStatement statement = PsiTreeUtil.getParentOfType(contextElement, CobolCopyStatement.class);
        if (statement == null) return null;

        PsiElement identifier = statement.getIdentifier();
        return identifier != null ? identifier : statement;
    }

    private static boolean isCopyToken(@NotNull PsiElement element) {
        if (element.getNode() == null) return false;
        IElementType type = element.getNode().getElementType();
        return type == CobolTypes.COPY || type == CobolTypes.IDENTIFIER;
    }
}
