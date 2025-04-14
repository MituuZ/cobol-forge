package com.mituuz.cobolforge;

import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.mituuz.cobolforge.psi.CobolTypes;
import com.intellij.codeInsight.codeVision.*;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
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
    private final Map<String, String> fileContentMap = new FixedSizeMap<>(30);

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
    public @NotNull CodeVisionState computeCodeVision(@NotNull Editor editor, Object uiData) {
        Project project = editor.getProject();
        List<kotlin.Pair<TextRange, CodeVisionEntry>> lenses = new ArrayList<>();

        if (project == null) {
            return CodeVisionState.Companion.getREADY_EMPTY();
        }

        ReadAction.run(() -> {
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
            var elements = findIdentifiersSafely(psiFile);

            for (PsiElement element : elements) {
                final TextRange textRange = element.getTextRange();
                final String filename = element.getText();

                String fileContent = fileContentMap.get(filename);
                if (fileContent == null) {
                    fileContent = fetchFileContent(filename, project);
                    fileContentMap.put(filename, fileContent);
                }

                if (textRange == null) {
                    continue;
                }

                final String tooltip = String.format("""
                        <html>
                        <strong>%s</strong>
                        <pre>%s</pre>
                        </html>""", filename, fileContent);


                lenses.add(new kotlin.Pair<>(textRange, new CobolVisionEntry(
                        "ProviderId",
                        null,
                        element.getText(),
                        tooltip,
                        List.of()
                )));
            }
        });

        return new CodeVisionState.Ready(lenses);
    }

    public String fetchFileContent(@NotNull String filename, @NotNull Project project) {
        if (filename.isBlank()) {
            return "Filename cannot be blank.";
        }

        final Collection<VirtualFile> files = FilenameIndex.getVirtualFilesByName(filename, GlobalSearchScope.allScope(project));
        final VirtualFile file;

        if (files.isEmpty()) {
            return "File not found: " + filename;
        } else {
            file = files.iterator().next();
        }

        if (files.size() > 1) {
            return "Multiple files found with the same name: " + filename;
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

    public static List<PsiElement> findIdentifiersSafely(PsiFile file) {
        return PsiTreeUtil.collectElementsOfType(file, PsiElement.class).stream()
                .filter(element -> element.getNode().getElementType() == CobolTypes.IDENTIFIER)
                .toList();
    }

    public static class FixedSizeMap<K, V> extends LinkedHashMap<K, V> {
        private final int maxSize;

        public FixedSizeMap(int maxSize) {
            super(maxSize, 0.75f, true); // true for access order
            this.maxSize = maxSize;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxSize;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            FixedSizeMap<?, ?> that = (FixedSizeMap<?, ?>) o;
            return maxSize == that.maxSize;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + maxSize;
            return result;
        }
    }
}