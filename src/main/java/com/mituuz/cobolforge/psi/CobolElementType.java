package com.mituuz.cobolforge.psi;

import com.mituuz.cobolforge.CobolLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CobolElementType extends IElementType {
    public CobolElementType(@NonNls @NotNull String debugName) {
        super(debugName, CobolLanguage.INSTANCE);
    }
}
