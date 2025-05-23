package com.mituuz.cobolforge;

import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.mituuz.cobolforge.psi.CobolTypes;
import com.intellij.codeInsight.codeVision.*;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class CobolCopyVisionProvider implements CodeVisionProvider {
    private static final List<String> FILE_EXTENSIONS = List.of(".cbl", ".cob", ".cpy", ".cobol", "");

    @Override
    public @NotNull CodeVisionAnchorKind getDefaultAnchor() {
        return CodeVisionAnchorKind.Default;
    }

    @Override
    public Object precomputeOnUiThread(@NotNull Editor editor) {
        return null;
    }

    @Override
    public @Nls @NotNull String getName() {
        return "Cobol Copy Vision";
    }

    @Override
    public @NotNull List<CodeVisionRelativeOrdering> getRelativeOrderings() {
        return List.of();
    }

    @Override
    public @NotNull String getId() {
        return "";
    }

    @Override
    public @NotNull CodeVisionState computeCodeVision(@NotNull final Editor editor, final Object uiData) {
        final Project project = editor.getProject();
        final List<kotlin.Pair<TextRange, CodeVisionEntry>> lenses = new ArrayList<>();

        if (project == null) {
            return CodeVisionState.Companion.getREADY_EMPTY();
        }

        ReadAction.run(() -> {
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
            final List<PsiElement> cobolIdentifiers = findIdentifiersSafely(psiFile);

            for (final PsiElement identifier : cobolIdentifiers) {
                final TextRange textRange = identifier.getTextRange();
                final String filename = identifier.getText();

                if (textRange == null) {
                    continue;
                }

                final String fileContent = fetchFileContent(filename, project);

                final String tooltip = String.format("""
                        <html>
                        <strong>%s</strong>
                        <pre>%s</pre>
                        </html>""", filename, fileContent);
                final String inlayText = "Hover to preview: " + filename;

                lenses.add(new kotlin.Pair<>(textRange, new CobolVisionEntry(
                        "ProviderId",
                        null,
                        inlayText,
                        tooltip,
                        List.of()
                )));
            }
        });

        return new CodeVisionState.Ready(lenses);
    }

    public String fetchFileContent(@NotNull final String filename, @NotNull final Project project) {
        if (filename.isBlank()) {
            return "Filename cannot be blank.";
        }

        VirtualFile file = null;

        for (String extension : FILE_EXTENSIONS) {
            final Collection<VirtualFile> files = FilenameIndex.getVirtualFilesByName(
                    filename + extension,
                    false,
                    GlobalSearchScope.allScope(project)
            );
            if (!files.isEmpty()) {
                file = files.iterator().next();
                if (files.size() > 1) {
                    return "Multiple files found with name: " + filename + extension;
                }
                break;
            }
        }

        if (file == null) {
            return "File not found: " + filename;
        }

        try {
            return new String(file.contentsToByteArray(), file.getCharset());
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    private static class CobolVisionEntry extends CodeVisionEntry {
        public CobolVisionEntry(@NotNull String providerId, @Nullable Icon icon, @Nls @NotNull String longPresentation, @NotNull String tooltip, @NotNull List<CodeVisionEntryExtraActionModel> extraActions) {
            super(providerId, icon, longPresentation, tooltip, extraActions);
        }

        @Override
        public @NotNull String toString() {
            return getLongPresentation();
        }
    }

    public static List<PsiElement> findIdentifiersSafely(final PsiFile file) {
        return PsiTreeUtil.collectElementsOfType(file, PsiElement.class).stream()
                .filter(element -> element.getNode().getElementType() == CobolTypes.IDENTIFIER)
                .toList();
    }
}