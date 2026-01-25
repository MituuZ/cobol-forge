package com.mituuz.cobolforge;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.SyntaxTraverser;
import com.mituuz.cobolforge.psi.CobolCopyStatement;
import com.mituuz.cobolforge.psi.CobolTypes;
import com.intellij.codeInsight.codeVision.*;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CobolCopyVisionProvider implements CodeVisionProvider {
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
            final List<CobolCopyStatement> copyStatements = findCopyStatements(psiFile);

            for (final CobolCopyStatement copyStatement : copyStatements) {
                PsiElement identifier = SyntaxTraverser.psiTraverser(copyStatement)
                        .filter(el -> el.getNode().getElementType() == CobolTypes.IDENTIFIER)
                        .traverse().first();

                if (identifier == null) {
                    continue;
                }

                final TextRange textRange = identifier.getTextRange();
                final String filename = identifier.getText();

                if (textRange == null) {
                    continue;
                }

                final String inlayText = "Hover to preview: " + filename;

                lenses.add(new kotlin.Pair<>(textRange, new CobolVisionEntry(
                        "ProviderId",
                        null,
                        inlayText,
                        "",
                        List.of()
                )));
            }
        });

        return new CodeVisionState.Ready(lenses);
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

    public static List<CobolCopyStatement> findCopyStatements(final PsiFile file) {
        return PsiTreeUtil.collectElementsOfType(file, CobolCopyStatement.class).stream().toList();
    }
}