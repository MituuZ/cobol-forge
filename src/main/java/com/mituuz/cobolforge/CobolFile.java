package com.mituuz.cobolforge;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class CobolFile extends PsiFileBase {
    public CobolFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, CobolLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return CobolFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "CobolFile";
    }
}
