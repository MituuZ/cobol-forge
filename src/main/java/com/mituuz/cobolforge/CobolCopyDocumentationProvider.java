package com.mituuz.cobolforge;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.mituuz.cobolforge.psi.CobolCopyStatement;
import com.mituuz.cobolforge.psi.CobolTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CobolCopyDocumentationProvider extends AbstractDocumentationProvider {
    @Override
    public @Nullable PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement) {
        if (contextElement == null || contextElement.getNode() == null) {
            return null;
        }
        if (contextElement.getNode().getElementType() == CobolTypes.COPY
                || contextElement.getNode().getElementType() == CobolTypes.IDENTIFIER) {
            return contextElement;
        }
        CobolCopyStatement statement = PsiTreeUtil.getParentOfType(contextElement, CobolCopyStatement.class);
        if (statement == null) {
            return null;
        }
        return PsiTreeUtil.collectElementsOfType(statement, PsiElement.class).stream()
                .filter(child -> child.getNode() != null
                        && (child.getNode().getElementType() == CobolTypes.COPY
                        || child.getNode().getElementType() == CobolTypes.IDENTIFIER))
                .findFirst()
                .orElse(null);
    }

    @Override
    public @Nullable String generateDoc(@Nullable PsiElement element, @Nullable PsiElement originalElement) {
        PsiElement target = originalElement != null ? originalElement : element;
        if (target == null || target.getNode() == null) {
            return null;
        }
        if (target.getNode().getElementType() != CobolTypes.IDENTIFIER
                && target.getNode().getElementType() != CobolTypes.COPY) {
            return null;
        }
        CobolCopyStatement statement = PsiTreeUtil.getParentOfType(target, CobolCopyStatement.class);
        if (statement == null) {
            return null;
        }

        PsiElement identifier = PsiTreeUtil.collectElementsOfType(statement, PsiElement.class).stream()
                .filter(child -> child.getNode() != null && child.getNode().getElementType() == CobolTypes.IDENTIFIER)
                .findFirst()
                .orElse(null);
        if (identifier == null) {
            return null;
        }

        String filename = identifier.getText();
        String fileContent = CobolCopyResolver.fetchFileContent(filename, target.getProject());

        String safeFilename = StringUtil.escapeXmlEntities(filename);
        String safeContent = StringUtil.escapeXmlEntities(fileContent);
        return "<html><body><h3>" + safeFilename + "</h3><pre>" + safeContent + "</pre></body></html>";
    }
}
