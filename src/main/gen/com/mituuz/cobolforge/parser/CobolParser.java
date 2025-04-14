// This is a generated file. Not intended for manual editing.
package com.mituuz.cobolforge.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.mituuz.cobolforge.psi.CobolTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class CobolParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return cobolFile(b, l + 1);
  }

  /* ********************************************************** */
  // item_*
  static boolean cobolFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cobolFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "cobolFile", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // COPY IDENTIFIER DOT
  public static boolean copy_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "copy_statement")) return false;
    if (!nextTokenIs(b, COPY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COPY, IDENTIFIER, DOT);
    exit_section_(b, m, COPY_STATEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // copy_statement | COMMENT | CRLF
  static boolean item_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_")) return false;
    boolean r;
    r = copy_statement(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, CRLF);
    return r;
  }

}
