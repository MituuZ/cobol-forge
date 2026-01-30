package com.mituuz.cobolforge;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public final class CobolCopyResolver {
    private static final List<String> FILE_EXTENSIONS = List.of(".cbl", ".cob", ".cpy", ".cobol", "");

    private CobolCopyResolver() {
    }

    public static @NotNull List<VirtualFile> fetchFiles(@NotNull final String filename, @NotNull final Project project) {
        if (filename.isBlank()) {
            return List.of();
        }

        final List<VirtualFile> foundFiles = new java.util.ArrayList<>();
        for (final String extension : FILE_EXTENSIONS) {
            final Collection<VirtualFile> files = FilenameIndex.getVirtualFilesByName(
                    filename + extension,
                    false,
                    GlobalSearchScope.allScope(project)
            );
            foundFiles.addAll(files);
        }

        return foundFiles;
    }
}
