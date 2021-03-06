/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.siddhi.plugins.idea.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.antlr.jetbrains.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.jetbrains.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;
import org.wso2.siddhi.plugins.idea.SiddhiLanguage;
import org.wso2.siddhi.plugins.idea.SiddhiParserDefinition;
import org.wso2.siddhi.plugins.idea.grammar.SiddhiQLLexer;

import java.util.HashMap;
import java.util.Map;

public class SiddhiSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<>();

    static {
        fillMap(ATTRIBUTES, SiddhiParserDefinition.COMMENTS, SiddhiSyntaxHighlightingColors.LINE_COMMENT);
        fillMap(ATTRIBUTES, SiddhiParserDefinition.KEYWORDS, SiddhiSyntaxHighlightingColors.KEYWORD);
        fillMap(ATTRIBUTES, SiddhiParserDefinition.STRING_LITERALS, SiddhiSyntaxHighlightingColors.STRING);
        fillMap(ATTRIBUTES, SiddhiParserDefinition.BAD_CHARACTER, SiddhiSyntaxHighlightingColors.BAD_CHARACTER);
        fillMap(ATTRIBUTES, SiddhiParserDefinition.SEMICOLON, SiddhiSyntaxHighlightingColors.SEMICOLON);
        fillMap(ATTRIBUTES, SiddhiParserDefinition.DOT, SiddhiSyntaxHighlightingColors.DOT);
        fillMap(ATTRIBUTES, SiddhiParserDefinition.COLON, SiddhiSyntaxHighlightingColors.COL);
        fillMap(ATTRIBUTES, SiddhiParserDefinition.TRIPLE_DOT, SiddhiSyntaxHighlightingColors.TRIPLE_DOT);
        fillMap(ATTRIBUTES, SiddhiParserDefinition.PARENTHESIS, SiddhiSyntaxHighlightingColors.PARENTHESIS);
        fillMap(ATTRIBUTES, SiddhiParserDefinition.SQUARE_BRACKETS, SiddhiSyntaxHighlightingColors.SQUARE_BRACKETS);
        fillMap(ATTRIBUTES, SiddhiParserDefinition.COMMA, SiddhiSyntaxHighlightingColors.COMMA);
        fillMap(ATTRIBUTES, SiddhiParserDefinition.SYMBOLS, SiddhiSyntaxHighlightingColors.SYMBOLS);
    }

    @NotNull
    public Lexer getHighlightingLexer() {
        SiddhiQLLexer lexer = new SiddhiQLLexer(null);
        return new ANTLRLexerAdaptor(SiddhiLanguage.INSTANCE, lexer);
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (!(tokenType instanceof TokenIElementType)) {
            return EMPTY_KEYS;
        }
        TokenIElementType myType = (TokenIElementType) tokenType;
        return pack(ATTRIBUTES.get(myType));
    }
}
