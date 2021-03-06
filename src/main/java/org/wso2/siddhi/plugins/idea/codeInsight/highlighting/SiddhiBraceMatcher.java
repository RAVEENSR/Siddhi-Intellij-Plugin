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

package org.wso2.siddhi.plugins.idea.codeInsight.highlighting;

import com.intellij.codeInsight.hint.DeclarationRangeUtil;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import org.wso2.siddhi.plugins.idea.SiddhiTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SiddhiBraceMatcher implements PairedBraceMatcher {

    private static final BracePair[] pairs = new BracePair[]{
            new BracePair(SiddhiTypes.OPEN_PAR, SiddhiTypes.CLOSE_PAR, false),
            new BracePair(SiddhiTypes.OPEN_SQUARE_BRACKETS, SiddhiTypes.CLOSE_SQUARE_BRACKETS, false)
    };

    @Override
    public BracePair[] getPairs() {
        return pairs;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull final IElementType lbraceType,
                                                   @Nullable final IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(final PsiFile file, int openingBraceOffset) {
        PsiElement element = file.findElementAt(openingBraceOffset);
        if (element == null || element instanceof PsiFile) return openingBraceOffset;
        PsiElement parent = element.getParent();
        if (parent instanceof PsiCodeBlock) {
            parent = parent.getParent();
            if (parent instanceof PsiMethod || parent instanceof PsiClassInitializer) {
                TextRange range = DeclarationRangeUtil.getDeclarationRange(parent);
                return range.getStartOffset();
            } else if (parent instanceof PsiStatement) {
                if (parent instanceof PsiBlockStatement && parent.getParent() instanceof PsiStatement) {
                    parent = parent.getParent();
                }
                return parent.getTextRange().getStartOffset();
            }
        } else if (parent instanceof PsiClass) {
            TextRange range = DeclarationRangeUtil.getDeclarationRange(parent);
            return range.getStartOffset();
        }
        return openingBraceOffset;
    }
}
