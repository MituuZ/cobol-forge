package com.mituuz.cobolforge.docs;

import com.intellij.model.Pointer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.backend.documentation.DocumentationResult;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.presentation.TargetPresentation;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.mituuz.cobolforge.CobolCopyResolver;
import com.mituuz.cobolforge.psi.CobolCopyStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class CobolCopyDocumentationTarget implements DocumentationTarget {
    private final @NotNull SmartPsiElementPointer<?> pointer;

    public CobolCopyDocumentationTarget(@NotNull com.intellij.psi.PsiElement element) {
        this.pointer = SmartPointerManager.getInstance(element.getProject()).createSmartPsiElementPointer(element);
    }

    @Override
    public @NotNull Pointer<? extends DocumentationTarget> createPointer() {
        SmartPsiElementPointer<?> p = this.pointer;
        return () -> {
            com.intellij.psi.PsiElement element = p.getElement();
            return element == null ? null : new CobolCopyDocumentationTarget(element);
        };
    }

    @Override
    public @NotNull TargetPresentation computePresentation() {
        com.intellij.psi.PsiElement element = pointer.getElement();
        String title = element != null ? ("COPY " + element.getText()) : "COPY";
        return TargetPresentation.builder(title).presentation();
    }

    @Override
    public @NotNull DocumentationResult computeDocumentation() {
        // Note: DocumentationResult.async(...) doesn't exist in this platform version.
        // Keep it synchronous for now (or migrate to AsyncDocumentation later).
        return buildHtml(pointer.getElement());
    }

    private static @NotNull DocumentationResult buildHtml(@Nullable final com.intellij.psi.PsiElement element) {
        if (element == null) {
            return DocumentationResult.documentation("<html><body>Target is no longer valid.</body></html>");
        }

        final CobolCopyStatement statement = PsiTreeUtil.getParentOfType(element, CobolCopyStatement.class);
        if (statement == null || statement.getIdentifier() == null) {
            return DocumentationResult.documentation("<html><body>No COPY statement found.</body></html>");
        }

        final String filename = statement.getIdentifier().getText();
        final List<VirtualFile> files = CobolCopyResolver.fetchFiles(filename, element.getProject());

        if (files.isEmpty()) {
            return DocumentationResult.documentation(
                    "<html><body>File not found: " + StringUtil.escapeXmlEntities(filename) + "</body></html>"
            );
        } else if (files.size() > 1) {
            final StringBuilder html = new StringBuilder("<html><body>");
            html.append("Multiple files found for copybook: ").append(StringUtil.escapeXmlEntities(filename));
            html.append("<ul>");
            for (final VirtualFile file : files) {
                html.append("<li>").append(StringUtil.escapeXmlEntities(file.getPath())).append("</li>");
            }
            html.append("</ul>");
            html.append("</body></html>");
            return DocumentationResult.documentation(html.toString());
        } else {
            final String html = getString(files);
            return DocumentationResult.documentation(html);
        }
    }

    private static @NotNull String getString(@NotNull final List<VirtualFile> files) {
        final VirtualFile vf = files.getFirst();
        String content;
        try {
            content = new String(vf.contentsToByteArray(), vf.getCharset());
        } catch (IOException e) {
            content = "Error reading file: " + e.getMessage();
        }

        return "<html><body>" +
                "<h3>" + StringUtil.escapeXmlEntities(vf.getPath()) + "</h3>" +
                "<pre>" + StringUtil.escapeXmlEntities(content) + "</pre>" +
                "</body></html>";
    }
}
