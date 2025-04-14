package com.mituuz.cobolforge;

import com.intellij.testFramework.ParsingTestCase;

public class CobolParsingTest extends ParsingTestCase {
    public CobolParsingTest() {
        super("", "cbl", new CobolParserDefinition());
    }

    public void testParsingTestData() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }
}
