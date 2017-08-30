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
package org.wso2.plugins.idea.codeInsight.daemon.impl;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.wso2.plugins.idea.highlighter.SiddhiSyntaxHighlightingColors;
import org.wso2.plugins.idea.psi.StreamIdNode;


public class SiddhiAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if ( element instanceof StreamIdNode) { //PsiTreeUtil.getParentOfType(element.getParent(), AttributeNameNode
            // .class).
            annotateStreamIdNodes(element, holder);
        }
    }

    private void annotateStreamIdNodes(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        Annotation annotation = holder.createInfoAnnotation(element.getTextRange(), null);
        annotation.setTextAttributes(SiddhiSyntaxHighlightingColors.STREAM_ID);
    }

}
