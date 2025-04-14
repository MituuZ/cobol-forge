package com.mituuz.cobolforge;

import com.mituuz.cobolforge.psi.CobolTypes;
import com.intellij.psi.tree.TokenSet;

public interface CobolTokenSets {
    TokenSet COMMENTS = TokenSet.create(CobolTypes.COMMENT);
}
