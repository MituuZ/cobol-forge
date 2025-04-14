package com.mituuz.cobolforge;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.mituuz.cobolforge.psi.CobolTypes;
import com.intellij.psi.TokenType;

%%


%class CobolLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R
WHITE_SPACE=[\ \n\t\f]
END_OF_LINE_COMMENT=("*")[^\r\n]*
IDENTIFIER=[a-zA-Z\-äÄöÖåÅ0-9]+
COPY = "COPY"

%state WAITING_IDENTIFIER

%%

<YYINITIAL> {COPY} {
    yybegin(WAITING_IDENTIFIER); return CobolTypes.COPY;
}

<WAITING_IDENTIFIER> {IDENTIFIER} {
    return CobolTypes.IDENTIFIER;
}

<WAITING_IDENTIFIER> "." {
    yybegin(YYINITIAL); return CobolTypes.DOT;

}
<WAITING_IDENTIFIER> {WHITE_SPACE}+ {
    return TokenType.WHITE_SPACE;
}

<YYINITIAL> {WHITE_SPACE}* {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return CobolTypes.COMMENT; }

({CRLF}|{WHITE_SPACE})+                                     { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

[^]                                                         { return TokenType.BAD_CHARACTER; }
