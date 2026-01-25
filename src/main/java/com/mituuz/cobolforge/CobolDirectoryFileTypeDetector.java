package com.mituuz.cobolforge;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.util.io.ByteSequence;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CobolDirectoryFileTypeDetector implements FileTypeRegistry.FileTypeDetector {

    @Override
    public @Nullable FileType detect(
            @NotNull final VirtualFile virtualFile,
            @NotNull final ByteSequence byteSequence,
            @Nullable final CharSequence charSequence
    ) {
        if (virtualFile.isDirectory()) return null;

        if (isUnderCobolDir(virtualFile)) {
            return CobolFileType.INSTANCE;
        }

        return null;
    }

    private static boolean isUnderCobolDir(@NotNull final VirtualFile file) {
        VirtualFile current = file.getParent();
        while (current != null) {
            if ("cobol".equalsIgnoreCase(current.getName())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
}
