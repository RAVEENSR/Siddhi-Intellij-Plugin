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

package org.wso2.plugins.idea.completion;

import com.intellij.codeInsight.completion.AddSpaceInsertHandler;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.impl.TemplateSettings;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class SiddhiCompletionUtils {

    private static final int VARIABLE_PRIORITY = 20;
    private static final int VALUE_TYPES_PRIORITY = VARIABLE_PRIORITY - 1;
    private static final int KEYWORDS_PRIORITY = VALUE_TYPES_PRIORITY - 2;


    //Initial Definition Types
    private static final LookupElementBuilder DEFINE;
    private static final LookupElementBuilder PARTITION;
    private static final LookupElementBuilder FROM;
    //private static final LookupElementBuilder ;// TODO: Add the @ Symbol

    //Define Types
    private static final LookupElementBuilder STREAM;
    private static final LookupElementBuilder TABLE;
    private static final LookupElementBuilder TRIGGER;
    private static final LookupElementBuilder FUNCTION;
    private static final LookupElementBuilder WINDOW;

    //Logical Operators
    private static final LookupElementBuilder AND;
    private static final LookupElementBuilder OR;
    private static final LookupElementBuilder NOT;
    private static final LookupElementBuilder IN;
    private static final LookupElementBuilder IS;//TODO: is null

    //Output Event Types
    private static final LookupElementBuilder CURRENT;
    private static final LookupElementBuilder ALL;
    private static final LookupElementBuilder EXPIRED;

    //Time Value Types
    private static final LookupElementBuilder YEARS;
    private static final LookupElementBuilder MONTHS;
    private static final LookupElementBuilder WEEKS;
    private static final LookupElementBuilder DAYS;
    private static final LookupElementBuilder HOURS;
    private static final LookupElementBuilder MINUTES;
    private static final LookupElementBuilder SECONDS;
    private static final LookupElementBuilder MILLISECONDS;

    //Data Types
    private static final LookupElementBuilder STRING;
    private static final LookupElementBuilder INT;
    private static final LookupElementBuilder LONG;
    private static final LookupElementBuilder FLOAT;
    private static final LookupElementBuilder DOUBLE;
    private static final LookupElementBuilder BOOL;
    private static final LookupElementBuilder OBJECT;

    private static final LookupElementBuilder EVERY;
    private static final LookupElementBuilder AT;

    //Window Processor Code Snippets
    private static final LookupElementBuilder LENGTH;
    private static final LookupElementBuilder LENGTHBATCH;
    private static final LookupElementBuilder SORT;
    private static final LookupElementBuilder EXTERNALTIMEBATCH;
    private static final LookupElementBuilder TIME;
    private static final LookupElementBuilder FREQUENT;
    private static final LookupElementBuilder LOSSYFREQUENT;
    private static final LookupElementBuilder TIMEBATCH;
    private static final LookupElementBuilder CRON;
    private static final LookupElementBuilder TIMELENGTH;
    private static final LookupElementBuilder EXTERNALTIME;

    //Other Keywords
    private static final LookupElementBuilder SELECT;
    private static final LookupElementBuilder GROUP;
    private static final LookupElementBuilder BY;
    private static final LookupElementBuilder HAVING;
    private static final LookupElementBuilder INSERT;
    private static final LookupElementBuilder DELETE;
    private static final LookupElementBuilder UPDATE;
    private static final LookupElementBuilder RETURN;
    private static final LookupElementBuilder EVENTS;
    private static final LookupElementBuilder INTO;
    private static final LookupElementBuilder OUTPUT;
    private static final LookupElementBuilder SNAPSHOT;
    private static final LookupElementBuilder FOR;
    private static final LookupElementBuilder RAW;
    private static final LookupElementBuilder OF;
    private static final LookupElementBuilder AS;
    private static final LookupElementBuilder ON;
    private static final LookupElementBuilder WITHIN;
    private static final LookupElementBuilder WITH;
    private static final LookupElementBuilder BEGIN;
    private static final LookupElementBuilder END;
    private static final LookupElementBuilder NULL;
    private static final LookupElementBuilder LAST;
    private static final LookupElementBuilder FIRST;
    private static final LookupElementBuilder JOIN;
    private static final LookupElementBuilder INNER;
    private static final LookupElementBuilder OUTER;
    private static final LookupElementBuilder RIGHT;
    private static final LookupElementBuilder LEFT;
    private static final LookupElementBuilder FULL;
    private static final LookupElementBuilder UNIDIRECTIONAL;

    private static final LookupElementBuilder FALSE;
    private static final LookupElementBuilder TRUE;

    private static final LookupElementBuilder AGGREGATION;
    private static final LookupElementBuilder AGGREGATE;
    private static final LookupElementBuilder PER;


    static {
        DEFINE = createKeywordLookupElement("define");
        PARTITION = createKeywordLookupElement("partition");
        FROM = createKeywordLookupElement("from");


        STREAM = createKeywordLookupElement("stream");
        TABLE = createKeywordLookupElement("table");
        TRIGGER = createKeywordLookupElement("trigger");
        FUNCTION = createKeywordLookupElement("function");
        WINDOW = createKeywordLookupElement("window");

        OR = createKeywordLookupElement("or");
        AND = createKeywordLookupElement("and");
        NOT = createKeywordLookupElement("not");
        IN = createKeywordLookupElement("in");
        IS = createKeywordLookupElement("is");

        EXPIRED = createKeywordLookupElement("expired");
        CURRENT = createKeywordLookupElement("current");
        ALL = createKeywordLookupElement("all");


        YEARS = createKeywordLookupElement("years");
        MONTHS = createKeywordLookupElement("months");
        WEEKS = createKeywordLookupElement("weeks");
        DAYS = createKeywordLookupElement("days");
        HOURS = createKeywordLookupElement("hours");
        MINUTES = createKeywordLookupElement("minutes");
        SECONDS = createKeywordLookupElement("seconds");
        MILLISECONDS = createKeywordLookupElement("milliseconds");

        AT = createKeywordLookupElement("at");
        EVERY = createKeywordLookupElement("every");

        SELECT = createKeywordLookupElement("select");
        GROUP = createKeywordLookupElement("group");
        BY = createKeywordLookupElement("by");
        HAVING = createKeywordLookupElement("having");
        INSERT = createKeywordLookupElement("insert");
        DELETE = createKeywordLookupElement("delete");
        UPDATE = createKeywordLookupElement("update");
        RETURN = createKeywordLookupElement("return");
        EVENTS = createKeywordLookupElement("events");
        INTO = createKeywordLookupElement("into");
        OUTPUT = createKeywordLookupElement("output");
        SNAPSHOT = createKeywordLookupElement("snapshot");
        FOR = createKeywordLookupElement("for");
        RAW = createKeywordLookupElement("raw");
        OF = createKeywordLookupElement("of");
        AS = createKeywordLookupElement("as");
        ON = createKeywordLookupElement("on");
        WITHIN = createKeywordLookupElement("within");
        WITH = createKeywordLookupElement("with");
        BEGIN = createKeywordLookupElement("begin");
        END = createKeywordLookupElement("end");
        NULL = createKeywordLookupElement("null");

        LAST = createKeywordLookupElement("last");
        FIRST = createKeywordLookupElement("first");
        JOIN = createKeywordLookupElement("join");
        INNER = createKeywordLookupElement("inner");
        OUTER = createKeywordLookupElement("outer");
        RIGHT = createKeywordLookupElement("right");
        LEFT = createKeywordLookupElement("left");
        FULL = createKeywordLookupElement("full");
        UNIDIRECTIONAL = createKeywordLookupElement("unidirectional");

        FALSE = createKeywordLookupElement("false");
        TRUE = createKeywordLookupElement("true");

        AGGREGATION = createKeywordLookupElement("aggregation");
        AGGREGATE = createKeywordLookupElement("aggregate");
        PER = createKeywordLookupElement("per");


        STRING = createDataTypeLookupElement("string", AddSpaceInsertHandler.INSTANCE);
        INT = createDataTypeLookupElement("int", AddSpaceInsertHandler.INSTANCE);
        LONG = createDataTypeLookupElement("long", AddSpaceInsertHandler.INSTANCE);
        FLOAT = createDataTypeLookupElement("float", AddSpaceInsertHandler.INSTANCE);
        DOUBLE = createDataTypeLookupElement("double", AddSpaceInsertHandler.INSTANCE);
        BOOL = createDataTypeLookupElement("bool", AddSpaceInsertHandler.INSTANCE);
        OBJECT = createDataTypeLookupElement("object", AddSpaceInsertHandler.INSTANCE);

        LENGTH = createWindowProcessorTypeLookupElement("length(window.length)", AddSpaceInsertHandler.INSTANCE).withPresentableText("length");
        LENGTHBATCH = createWindowProcessorTypeLookupElement("lengthBatch(window.length)", AddSpaceInsertHandler.INSTANCE).withPresentableText("lengthBatch");
        SORT = createWindowProcessorTypeLookupElement("sort(window.length, attribute, order)", AddSpaceInsertHandler.INSTANCE).withPresentableText("sort");
        EXTERNALTIMEBATCH = createWindowProcessorTypeLookupElement("externalTimeBatch(timestamp, window.time, start.time, timeout)", AddSpaceInsertHandler.INSTANCE).withPresentableText("externalTimeBatch");
        TIME = createWindowProcessorTypeLookupElement("time(window.time)", AddSpaceInsertHandler.INSTANCE).withPresentableText("time");
        FREQUENT = createWindowProcessorTypeLookupElement("frequent(event.count, attribute)", AddSpaceInsertHandler.INSTANCE).withPresentableText("frequent");
        LOSSYFREQUENT = createWindowProcessorTypeLookupElement("lossyFrequent(support.threshold, error.bound, attribute)", AddSpaceInsertHandler.INSTANCE).withPresentableText("lossyFrequent");
        TIMEBATCH = createWindowProcessorTypeLookupElement("timeBatch(window.time, start.time)", AddSpaceInsertHandler.INSTANCE).withPresentableText("timeBatch");
        CRON = createWindowProcessorTypeLookupElement("cron(cron.expression)", AddSpaceInsertHandler.INSTANCE).withPresentableText("cron");
        TIMELENGTH = createWindowProcessorTypeLookupElement("timeLength(window.time, window.length)", AddSpaceInsertHandler.INSTANCE).withPresentableText("timeLength");
        EXTERNALTIME = createWindowProcessorTypeLookupElement("externalTime(window.time)", AddSpaceInsertHandler.INSTANCE).withPresentableText("externalTime");

    }

    private SiddhiCompletionUtils() {

    }

    /**
     * Creates a lookup element.
     *
     * @param name          name of the lookup
     * @param insertHandler insert handler of the lookup
     * @return {@link LookupElementBuilder} which will be used to create the lookup element.
     */
    @NotNull
    private static LookupElementBuilder createLookupElement(@NotNull String name,
                                                            @Nullable InsertHandler<LookupElement> insertHandler) {
        return LookupElementBuilder.create(name).withBoldness(true).withInsertHandler(insertHandler);
    }

    /**
     * Creates a keyword lookup element.
     *
     * @param name name of the lookup
     * @return {@link LookupElementBuilder} which will be used to create the lookup element.
     */
    @NotNull
    private static LookupElementBuilder createKeywordLookupElement(@NotNull String name) {
        return createLookupElement(name, createTemplateBasedInsertHandler("siddhi_lang_" + name));
    }


    @NotNull
    private static InsertHandler<LookupElement> createTemplateBasedInsertHandler(@NotNull String templateId) {
        return (context, item) -> {
            Template template = TemplateSettings.getInstance().getTemplateById(templateId);
            Editor editor = context.getEditor();
            if (template != null) {
                editor.getDocument().deleteString(context.getStartOffset(), context.getTailOffset());
                TemplateManager.getInstance(context.getProject()).startTemplate(editor, template);
            } else {
                int currentOffset = editor.getCaretModel().getOffset();
                CharSequence documentText = editor.getDocument().getImmutableCharSequence();
                if (documentText.length() <= currentOffset || documentText.charAt(currentOffset) != ' ') {
                    EditorModificationUtil.insertStringAtCaret(editor, " ");
                } else {
                    EditorModificationUtil.moveCaretRelatively(editor, 1);
                }
            }
        };
    }

    /**
     * Creates a <b>Type</b> lookup element.
     *
     * @param name          of the lookup
     * @param insertHandler insert handler of the lookup
     * @return {@link LookupElementBuilder} which will be used to create the lookup element.
     */
    @NotNull
    private static LookupElementBuilder createDataTypeLookupElement(@NotNull String name,
                                                                    @Nullable InsertHandler<LookupElement> insertHandler) {
        return createLookupElement(name, insertHandler).withTypeText("Data Type");
    }

    /**
     * Creates a <b>Type</b> lookup element.
     *
     * @param name          of the lookup
     * @param insertHandler insert handler of the lookup
     * @return {@link LookupElementBuilder} which will be used to create the lookup element.
     */
    @NotNull
    private static LookupElementBuilder createWindowProcessorTypeLookupElement(@NotNull String name,
                                                                               @Nullable InsertHandler<LookupElement> insertHandler) {
        return createLookupElement(name, insertHandler).withTypeText("Window Processor");
    }

    /**
     * Adds value types as lookups.
     *
     * @param resultSet result list which is used to add lookups
     */
    static void addValueTypesAsLookups(@NotNull CompletionResultSet resultSet) {
        resultSet.addElement(PrioritizedLookupElement.withPriority(LONG, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(INT, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(FLOAT, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(STRING, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(BOOL, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(DOUBLE, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(OBJECT, VALUE_TYPES_PRIORITY));
    }

    /**
     * Adds value types as lookups.
     *
     * @param resultSet result list which is used to add lookups
     */
    static void addInitialTypesAsLookups(@NotNull CompletionResultSet resultSet) {
        resultSet.addElement(PrioritizedLookupElement.withPriority(DEFINE, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(PARTITION, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(FROM, VALUE_TYPES_PRIORITY));
        //resultSet.addElement(PrioritizedLookupElement.withPriority('@', VALUE_TYPES_PRIORITY)); TODO: @ symbol
    }

    /**
     * Adds value types as lookups.
     *
     * @param resultSet result list which is used to add lookups
     */
    static void addDefineTypesAsLookups(@NotNull CompletionResultSet resultSet) {
        resultSet.addElement(PrioritizedLookupElement.withPriority(STREAM, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(TABLE, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(TRIGGER, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(FUNCTION, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(WINDOW, VALUE_TYPES_PRIORITY));
    }

    /**
     * Adds a keyword as a lookup.
     *
     * @param resultSet     result list which is used to add lookups
     * @param lookupElement lookup element which needs to be added to the result list
     */
    private static void addKeywordAsLookup(@NotNull CompletionResultSet resultSet, @NotNull LookupElement
            lookupElement) {
        resultSet.addElement(PrioritizedLookupElement.withPriority(lookupElement, KEYWORDS_PRIORITY));
    }

    private static LookupElement createKeywordAsLookup(@NotNull LookupElement lookupElement) {
        return PrioritizedLookupElement.withPriority(lookupElement, KEYWORDS_PRIORITY);
    }

    static void addAtKeyword(@NotNull CompletionResultSet resultSet) {
        addKeywordAsLookup(resultSet, AT);
    }

    static void addEveryKeyword(@NotNull CompletionResultSet resultSet) {
        addKeywordAsLookup(resultSet, AT);
    }

    /**
     * Adds value types as lookups.
     *
     * @param resultSet result list which is used to add lookups
     */
    static void addWindowProcessorTypesAsLookups(@NotNull CompletionResultSet resultSet, boolean withWhitespace, InsertHandler<LookupElement> insertHandler) {

        LookupElementBuilder elementBuilder = LENGTH.withInsertHandler(insertHandler);
        resultSet.addElement(PrioritizedLookupElement.withPriority(elementBuilder, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(LENGTHBATCH, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(SORT, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(EXTERNALTIMEBATCH, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(TIME, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(FREQUENT, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(LOSSYFREQUENT, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(TIMEBATCH, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(CRON, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(TIMELENGTH, VALUE_TYPES_PRIORITY));
        resultSet.addElement(PrioritizedLookupElement.withPriority(EXTERNALTIME, VALUE_TYPES_PRIORITY));


    }


    /**
     * Adds common keywords like if, else as lookup elements.
     *
     * @param resultSet result list which is used to add lookups
     */
    static void addCommonKeywords(@NotNull CompletionResultSet resultSet) {


        addKeywordAsLookup(resultSet, TABLE);
        addKeywordAsLookup(resultSet, WINDOW);
        addKeywordAsLookup(resultSet, SELECT);
        addKeywordAsLookup(resultSet, GROUP);
        addKeywordAsLookup(resultSet, BY);
        addKeywordAsLookup(resultSet, HAVING);
        addKeywordAsLookup(resultSet, INSERT);
        addKeywordAsLookup(resultSet, DELETE);
        addKeywordAsLookup(resultSet, UPDATE);
        addKeywordAsLookup(resultSet, RETURN);
        addKeywordAsLookup(resultSet, EVENTS);
        addKeywordAsLookup(resultSet, INTO);
        addKeywordAsLookup(resultSet, OUTPUT);
        addKeywordAsLookup(resultSet, EXPIRED);
        addKeywordAsLookup(resultSet, CURRENT);
        addKeywordAsLookup(resultSet, SNAPSHOT);
        addKeywordAsLookup(resultSet, FOR);
        addKeywordAsLookup(resultSet, RAW);
        addKeywordAsLookup(resultSet, OF);
        addKeywordAsLookup(resultSet, AS);
        addKeywordAsLookup(resultSet, OR);
        addKeywordAsLookup(resultSet, AND);
        addKeywordAsLookup(resultSet, ON);
        addKeywordAsLookup(resultSet, IN);
        addKeywordAsLookup(resultSet, IS);
        addKeywordAsLookup(resultSet, NOT);
        addKeywordAsLookup(resultSet, WITHIN);
        addKeywordAsLookup(resultSet, WITH);
        addKeywordAsLookup(resultSet, BEGIN);
        addKeywordAsLookup(resultSet, END);
        addKeywordAsLookup(resultSet, NULL);

        addKeywordAsLookup(resultSet, LAST);
        addKeywordAsLookup(resultSet, ALL);
        addKeywordAsLookup(resultSet, FIRST);
        addKeywordAsLookup(resultSet, JOIN);
        addKeywordAsLookup(resultSet, INNER);
        addKeywordAsLookup(resultSet, OUTER);
        addKeywordAsLookup(resultSet, RIGHT);
        addKeywordAsLookup(resultSet, LEFT);
        addKeywordAsLookup(resultSet, FULL);
        addKeywordAsLookup(resultSet, UNIDIRECTIONAL);
        addKeywordAsLookup(resultSet, YEARS);
        addKeywordAsLookup(resultSet, MONTHS);
        addKeywordAsLookup(resultSet, WEEKS);
        addKeywordAsLookup(resultSet, DAYS);
        addKeywordAsLookup(resultSet, HOURS);
        addKeywordAsLookup(resultSet, MINUTES);
        addKeywordAsLookup(resultSet, SECONDS);
        addKeywordAsLookup(resultSet, MILLISECONDS);

        addKeywordAsLookup(resultSet, AGGREGATION);
        addKeywordAsLookup(resultSet, AGGREGATE);
        addKeywordAsLookup(resultSet, PER);

    }

    @NotNull
    public static List<LookupElement> createCommonKeywords() {
        List<LookupElement> lookupElements = new LinkedList<>();


        lookupElements.add(createKeywordAsLookup(TABLE));
        lookupElements.add(createKeywordAsLookup(WINDOW));
        lookupElements.add(createKeywordAsLookup(SELECT));
        lookupElements.add(createKeywordAsLookup(GROUP));
        lookupElements.add(createKeywordAsLookup(BY));
        lookupElements.add(createKeywordAsLookup(HAVING));
        lookupElements.add(createKeywordAsLookup(INSERT));
        lookupElements.add(createKeywordAsLookup(DELETE));
        lookupElements.add(createKeywordAsLookup(UPDATE));
        lookupElements.add(createKeywordAsLookup(RETURN));
        lookupElements.add(createKeywordAsLookup(EVENTS));
        lookupElements.add(createKeywordAsLookup(INTO));
        lookupElements.add(createKeywordAsLookup(OUTPUT));
        lookupElements.add(createKeywordAsLookup(EXPIRED));
        lookupElements.add(createKeywordAsLookup(CURRENT));
        lookupElements.add(createKeywordAsLookup(SNAPSHOT));
        lookupElements.add(createKeywordAsLookup(FOR));
        lookupElements.add(createKeywordAsLookup(RAW));
        lookupElements.add(createKeywordAsLookup(OF));
        lookupElements.add(createKeywordAsLookup(AS));
        lookupElements.add(createKeywordAsLookup(OR));
        lookupElements.add(createKeywordAsLookup(AND));
        lookupElements.add(createKeywordAsLookup(ON));
        lookupElements.add(createKeywordAsLookup(IN));
        lookupElements.add(createKeywordAsLookup(IS));
        lookupElements.add(createKeywordAsLookup(NOT));
        lookupElements.add(createKeywordAsLookup(WITHIN));
        lookupElements.add(createKeywordAsLookup(WITH));
        lookupElements.add(createKeywordAsLookup(BEGIN));
        lookupElements.add(createKeywordAsLookup(END));
        lookupElements.add(createKeywordAsLookup(NULL));

        lookupElements.add(createKeywordAsLookup(LAST));
        lookupElements.add(createKeywordAsLookup(ALL));
        lookupElements.add(createKeywordAsLookup(FIRST));
        lookupElements.add(createKeywordAsLookup(JOIN));
        lookupElements.add(createKeywordAsLookup(INNER));
        lookupElements.add(createKeywordAsLookup(OUTER));
        lookupElements.add(createKeywordAsLookup(RIGHT));
        lookupElements.add(createKeywordAsLookup(LEFT));
        lookupElements.add(createKeywordAsLookup(FULL));
        lookupElements.add(createKeywordAsLookup(UNIDIRECTIONAL));
        lookupElements.add(createKeywordAsLookup(YEARS));
        lookupElements.add(createKeywordAsLookup(MONTHS));
        lookupElements.add(createKeywordAsLookup(WEEKS));
        lookupElements.add(createKeywordAsLookup(DAYS));
        lookupElements.add(createKeywordAsLookup(HOURS));
        lookupElements.add(createKeywordAsLookup(MINUTES));
        lookupElements.add(createKeywordAsLookup(SECONDS));
        lookupElements.add(createKeywordAsLookup(MILLISECONDS));

        lookupElements.add(createKeywordAsLookup(AGGREGATION));
        lookupElements.add(createKeywordAsLookup(AGGREGATE));
        lookupElements.add(createKeywordAsLookup(PER));

        return lookupElements;
    }

    static void addValueKeywords(@NotNull CompletionResultSet resultSet) {
        addKeywordAsLookup(resultSet, TRUE);
        addKeywordAsLookup(resultSet, FALSE);
        addKeywordAsLookup(resultSet, NULL);
    }

    @NotNull
    public static List<LookupElement> createValueKeywords() {
        List<LookupElement> lookupElements = new LinkedList<>();
        lookupElements.add(createKeywordAsLookup(TRUE));
        lookupElements.add(createKeywordAsLookup(FALSE));
        lookupElements.add(createKeywordAsLookup(NULL));
        return lookupElements;
    }

}