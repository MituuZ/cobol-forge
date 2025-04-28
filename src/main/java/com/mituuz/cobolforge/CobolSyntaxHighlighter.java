package com.mituuz.cobolforge;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.mituuz.cobolforge.psi.CobolTypes;
import org.jetbrains.annotations.NotNull;

public class CobolSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey COMMENT = TextAttributesKey.createTextAttributesKey("COBOL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey IDENTIFIER = TextAttributesKey.createTextAttributesKey("COBOL_IDENTIFIER", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey BAD_CHARACTER = TextAttributesKey.createTextAttributesKey("COBOL_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
    private static final TextAttributesKey[] IDENTIFIER_KEYS = new TextAttributesKey[]{IDENTIFIER};
    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new CobolLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(final IElementType tokenType) {
        if (tokenType.equals(CobolTypes.COMMENT)) {
            return COMMENT_KEYS;
        }
        if (tokenType.equals(CobolTypes.IDENTIFIER)) {
            return IDENTIFIER_KEYS;
        }
        if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        }
        return EMPTY_KEYS;
    }
}
