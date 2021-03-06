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

package org.wso2.siddhi.plugins.idea;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.jetbrains.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.jetbrains.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.jetbrains.adaptor.lexer.RuleIElementType;
import org.antlr.jetbrains.adaptor.lexer.TokenIElementType;
import org.antlr.jetbrains.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.jetbrains.adaptor.psi.ANTLRPsiNode;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;
import org.wso2.siddhi.plugins.idea.grammar.SiddhiQLLexer;
import org.wso2.siddhi.plugins.idea.grammar.SiddhiQLParser;
import org.wso2.siddhi.plugins.idea.psi.*;

import static org.wso2.siddhi.plugins.idea.grammar.SiddhiQLParser.*;

public class SiddhiParserDefinition implements ParserDefinition {

    private static final IFileElementType FILE = new IFileElementType(SiddhiLanguage.INSTANCE);

    static {
        PSIElementTypeFactory.defineLanguageIElementTypes(SiddhiLanguage.INSTANCE, SiddhiQLParser.tokenNames,
                SiddhiQLParser.ruleNames);
    }

    public static final TokenSet KEYWORDS = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE, STREAM,
            DEFINE, FUNCTION, TRIGGER, TABLE, APP, FROM, PARTITION, WINDOW, SELECT, GROUP, BY, HAVING, INSERT,
            DELETE, UPDATE, RETURN, EVENTS, INTO, OUTPUT, EXPIRED, CURRENT, SNAPSHOT, FOR, RAW, OF, AS, AT, OR, AND,
            ON, IN, IS, NOT, WITHIN, WITH, BEGIN, END, NULL, EVERY, LAST, ALL, FIRST, JOIN, INNER, OUTER, RIGHT, LEFT,
            FULL, UNIDIRECTIONAL, YEARS, MONTHS, WEEKS, DAYS, HOURS, MINUTES, SECONDS, MILLISECONDS, FALSE, TRUE,
            STRING, INT, LONG, FLOAT, DOUBLE, BOOL, OBJECT, AGGREGATION, AGGREGATE, PER);

    public static final TokenSet COMMENTS = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            SINGLE_LINE_COMMENT, MULTILINE_COMMENT);
    public static final TokenSet WHITESPACE = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            SPACES);
    public static final TokenSet STRING_LITERALS = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            STRING_LITERAL);
    public static final TokenSet BAD_CHARACTER = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            UNEXPECTED_CHAR);
    public static final TokenSet SEMICOLON = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            SCOL);
    public static final TokenSet DOT = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            SiddhiQLParser.DOT);
    public static final TokenSet COLON = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            SiddhiQLParser.COL);
    public static final TokenSet TRIPLE_DOT = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            SiddhiQLParser.TRIPLE_DOT);
    public static final TokenSet PARENTHESIS = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            SiddhiQLParser.OPEN_PAR,CLOSE_PAR);
    public static final TokenSet SQUARE_BRACKETS = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            SiddhiQLParser.OPEN_SQUARE_BRACKETS,CLOSE_SQUARE_BRACKETS);
    public static final TokenSet COMMA = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            SiddhiQLParser.COMMA);
    public static final TokenSet SYMBOLS = PSIElementTypeFactory.createTokenSet(SiddhiLanguage.INSTANCE,
            ASSIGN,STAR,PLUS,QUESTION,MINUS,DIV,MOD,LT,LT_EQ,GT,GT_EQ,EQ,NOT_EQ,AT_SYMBOL,FOLLOWED_BY,HASH);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        SiddhiQLLexer lexer = new SiddhiQLLexer(null);
        return new ANTLRLexerAdaptor(SiddhiLanguage.INSTANCE, lexer);
    }

    @NotNull
    public PsiParser createParser(final Project project) {
        final SiddhiQLParser parser = new SiddhiQLParser(null);
        return new ANTLRParserAdaptor(SiddhiLanguage.INSTANCE, parser) {
            @Override
            protected ParseTree parse(Parser parser, IElementType root) {
                // Start rule depends on root passed in; sometimes we want to create an ID node etc...
                // Eg: if (root instanceof IFileElementType) { }
                return ((SiddhiQLParser) parser).parse();
            }
        };
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return WHITESPACE;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return STRING_LITERALS;
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new SiddhiFile(viewProvider);
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        IElementType elementType = node.getElementType();
        if (elementType instanceof TokenIElementType) {
            return new ANTLRPsiNode(node);
        }
        if (!(elementType instanceof RuleIElementType)) {
            return new ANTLRPsiNode(node);
        }

        RuleIElementType ruleElType = (RuleIElementType) elementType;
        switch (ruleElType.getRuleIndex()) {
            case RULE_stream_id:
                return new StreamIdNode(node);
            case RULE_attribute_type:
                return new AttributeTypeNode(node);
            case RULE_trigger_name:
                return new TriggerNameNode(node);
            case RULE_definition_trigger:
                return new TriggerDefinitionNode(node);
            case RULE_definition_window:
                return new WindowDefinitionNode(node);
            case RULE_function_operation:
                return new FunctionOperationNode(node);
            case RULE_attribute_name:
                return new AttributeNameNode(node);
            case RULE_language_name:
                return new LanguageNameNode(node);
            case RULE_function_body:
                return new FunctionBodyNode(node);
            case RULE_definition_function:
                return new FunctionDefinitionNode(node);
            case RULE_function_name:
                return new FunctionNameNode(node);
            case RULE_siddhi_app:
                return new SiddhiAppNode(node);
            case RULE_output_event_type:
                return new OutputEventTypeNode(node);
            case RULE_parse:
                return new ParseNode(node);
            case RULE_query_section:
                return new QuerySectionNode(node);
            case RULE_execution_element:
                return new ExecutionElementNode(node);
            case RULE_annotation_element:
                return new AnnotationElementNode(node);
            case RULE_annotation:
                return new AnnotationNode(node);
            case RULE_app_annotation:
                return new AppAnnotationNode(node);
            case RULE_query_input:
                return new QueryInputNode(node);
            case RULE_query_output:
                return new QueryOutputNode(node);
            case RULE_query:
                return new QueryNode(node);
            case RULE_partition:
                return new PartitionNode(node);
            case RULE_definition_aggregation:
                return new AggregationDefinitionNode(node);
            case RULE_definition_table:
                return new TableDefinitionNode(node);
            case RULE_definition_stream:
                return new StreamDefinitionNode(node);
            case RULE_output_rate:
                return new OutputRateNode(node);
            case RULE_name:
                return new NameNode(node);
            case RULE_source:
                return new SourceNode(node);
            case RULE_id:
                return new IdNode(node);
            case RULE_definition_element_with_execution_element:
                return new DefinitionElementWithExecutionElementNode(node);
            case RULE_standard_stream:
                return new StandardStreamNode(node);
            case RULE_join_stream:
                return new JoinStreamNode(node);
            case RULE_join_source:
                return new JoinSourceNode(node);
            case RULE_join:
                return new JoinNode(node);
            case RULE_basic_source:
                return new BasicSourceNode(node);
            case RULE_target:
                return new TargetNode(node);
            case RULE_attribute_reference:
                return new AttributeReferenceNode(node);
            case RULE_output_attribute:
                return new OutputAttributeNode(node);
            case RULE_definition_element:
                return new DefinitionElementNode(node);
            case RULE_having:
                return new HavingNode(node);
            case RULE_expression:
                return new ExpressionNode(node);
            case RULE_math_operation:
                return new MathOperationNode(node);
            case RULE_constant_value:
                return new ConstantValueNode(node);
            case RULE_signed_int_value:
                return new SignedIntValueNode(node);
            case RULE_group_by:
                return new GroupByNode(node);
            case RULE_time_value:
                return new TimeValueNode(node);
            case RULE_update_or_insert_into:
                return new UpdateOrInsertIntoNode(node);
            case RULE_delete_from_table:
                return new DeleteFromTableNode(node);
            case RULE_update_table:
                return new UpdateTableNode(node);
            default:
                return new ANTLRPsiNode(node);
        }
    }
}