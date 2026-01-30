package com.mituuz.cobolforge;

import com.intellij.testFramework.ParsingTestCase;

public class CobolParsingTest extends ParsingTestCase {
    public CobolParsingTest() {
        super("parsingTestData", "cbl", new CobolParserDefinition());
    }

    public void testParsingTestData() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }
}
