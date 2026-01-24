package com.mituuz.cobolforge;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public final class CobolCopyResolver {
    private static final List<String> FILE_EXTENSIONS = List.of(".cbl", ".cob", ".cpy", ".cobol", "");

    private CobolCopyResolver() {
    }

    public static @NotNull String fetchFileContent(@NotNull final String filename, @NotNull final Project project) {
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
}
