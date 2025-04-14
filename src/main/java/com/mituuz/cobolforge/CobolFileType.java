package com.mituuz.cobolforge;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CobolFileType extends LanguageFileType {
    public static final CobolFileType INSTANCE = new CobolFileType();

    private CobolFileType() {
        super(CobolLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "Cobol";
    }

    @Override
    public @NotNull String getDescription() {
        return "COBOL";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "cbl";
    }

    @Override
    public Icon getIcon() {
        return CobolIcons.FILE;
    }
}
