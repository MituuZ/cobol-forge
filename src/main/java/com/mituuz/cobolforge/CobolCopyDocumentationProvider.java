package com.mituuz.cobolforge;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.mituuz.cobolforge.psi.CobolCopyStatement;
import com.mituuz.cobolforge.psi.CobolTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class CobolCopyDocumentationProvider extends AbstractDocumentationProvider {

    @Override
    public @Nullable PsiElement getCustomDocumentationElement(@NotNull final Editor editor,
                                                             @NotNull final PsiFile file,
                                                             @Nullable final PsiElement contextElement) {
        if (contextElement == null) {
            return null;
        }

        // If the user hovers exactly the interesting token (COPY/IDENTIFIER), keep it.
        if (isCopyToken(contextElement)) {
            return contextElement;
        }

        // Otherwise, resolve the surrounding COPY statement and return the most relevant PSI.
        final CobolCopyStatement statement = PsiTreeUtil.getParentOfType(contextElement, CobolCopyStatement.class);
        if (statement == null) {
            return null;
        }

        // Prefer the identifier (the copybook name) as the documentation target.
        final PsiElement identifier = statement.getIdentifier();
        if (identifier != null) {
            return identifier;
        }

        // Fallback: if we can't get identifier from the statement, still allow docs on the statement itself.
        return statement;
    }

    @Override
    public @Nullable String generateDoc(@Nullable final PsiElement element, @Nullable final PsiElement originalElement) {
        final PsiElement target = originalElement != null ? originalElement : element;
        if (target == null) {
            return null;
        }

        final CobolCopyStatement statement = PsiTreeUtil.getParentOfType(target, CobolCopyStatement.class);
        if (statement == null) {
            return null;
        }

        final PsiElement identifier = statement.getIdentifier();
        if (identifier == null) {
            return null;
        }

        final String filename = identifier.getText();
        List<VirtualFile> files = CobolCopyResolver.fetchFiles(filename, target.getProject());

        if (files.isEmpty()) {
            return "<html><body>File not found: " + StringUtil.escapeXmlEntities(filename) + "</body></html>";
        } else if (files.size() > 1) {
            return "<html><body>Multiple files found for copybook: " + StringUtil.escapeXmlEntities(filename) + "</body></html>";
        } else {
            final StringBuilder sb = new StringBuilder("<html><body>");
            final VirtualFile virtualFile = files.getFirst();

            String content;
            try {
                content = new String(virtualFile.contentsToByteArray(), virtualFile.getCharset());
            } catch (IOException e) {
                content = "Error reading file: " + e.getMessage();
            }

            sb.append("<h3>").append(StringUtil.escapeXmlEntities(virtualFile.getPath())).append("</h3>");
            sb.append("<pre>").append(StringUtil.escapeXmlEntities(content)).append("</pre>");

            sb.append("</body></html>");
            return sb.toString();
        }
    }

    private static boolean isCopyToken(@NotNull PsiElement element) {
        if (element.getNode() == null) {
            return false;
        }
        final IElementType type = element.getNode().getElementType();
        return type == CobolTypes.COPY || type == CobolTypes.IDENTIFIER;
    }
}
