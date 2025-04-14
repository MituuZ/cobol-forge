package com.mituuz.cobolforge;

import com.intellij.lang.Language;

public class CobolLanguage extends Language {
    public static final CobolLanguage INSTANCE = new CobolLanguage();

    protected CobolLanguage() {
        super("COBOL");
    }
}
