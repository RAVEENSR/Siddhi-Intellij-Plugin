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

package org.ballerinalang.plugins.idea.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.antlr.jetbrains.adaptor.psi.ANTLRPsiNode;
import org.wso2.plugins.idea.psi.AnnotationDefinitionNode;
import org.wso2.plugins.idea.psi.CallableUnitBodyNode;
import org.wso2.plugins.idea.psi.ConnectorBodyNode;
import org.wso2.plugins.idea.psi.ConnectorDefinitionNode;
import org.wso2.plugins.idea.psi.ConstantDefinitionNode;
import org.wso2.plugins.idea.psi.DefinitionNode;
import org.wso2.plugins.idea.psi.FunctionDefinitionNode;
import org.wso2.plugins.idea.psi.GlobalVariableDefinitionNode;
import org.wso2.plugins.idea.psi.IdentifierPSINode;
import org.wso2.plugins.idea.psi.ImportDeclarationNode;
import org.wso2.plugins.idea.psi.MapStructKeyValueNode;
import org.wso2.plugins.idea.psi.NameReferenceNode;
import org.wso2.plugins.idea.psi.PackageDeclarationNode;
import org.wso2.plugins.idea.psi.ResourceDefinitionNode;
import org.wso2.plugins.idea.psi.ServiceBodyNode;
import org.wso2.plugins.idea.psi.TypeNameNode;
import org.jetbrains.annotations.NotNull;

import static org.wso2.plugins.idea.completion.SiddhiCompletionUtils.*;

public class SiddhiKeywordsCompletionContributor extends CompletionContributor {

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        PsiElement element = parameters.getPosition();
        PsiElement parent = element.getParent();

        if (parent instanceof NameReferenceNode /*|| parent instanceof PsiErrorElement*/) {
            PsiElement prevVisibleSibling = PsiTreeUtil.prevVisibleLeaf(element);
            if (prevVisibleSibling instanceof IdentifierPSINode) {
                addAttachKeyword(result);
                return;
            }

            ANTLRPsiNode definitionParent = PsiTreeUtil.getParentOfType(parent, CallableUnitBodyNode.class,
                    ServiceBodyNode.class, ConnectorBodyNode.class);
            if (definitionParent != null && prevVisibleSibling != null && "=".equals(prevVisibleSibling.getText())) {
                addCreateKeyword(result);
            }

            TypeNameNode typeNameNode = PsiTreeUtil.getParentOfType(parent, TypeNameNode.class);
            if (typeNameNode != null && prevVisibleSibling != null && !prevVisibleSibling.getText().matches("[:=]")) {
                AnnotationDefinitionNode annotationDefinitionNode = PsiTreeUtil.getParentOfType(typeNameNode,
                        AnnotationDefinitionNode.class);
                if (annotationDefinitionNode == null) {
                    addAnyTypeAsLookup(result);
                    addXmlnsAsLookup(result);
                    addValueTypesAsLookups(result);
                    addReferenceTypesAsLookups(result);
                }
            }
        }

        if (parent instanceof ConstantDefinitionNode || parent instanceof PsiErrorElement) {
            PsiElement prevVisibleSibling = PsiTreeUtil.prevVisibleLeaf(element);
            if (prevVisibleSibling != null && "const".equals(prevVisibleSibling.getText())) {
                addValueTypesAsLookups(result);
                return;
            }
        }

        if (parent instanceof PsiErrorElement) {

            FunctionDefinitionNode functionDefinitionNode = PsiTreeUtil.getParentOfType(element,
                    FunctionDefinitionNode.class);
            if (functionDefinitionNode != null) {

                PsiElement prevVisibleSibling = PsiTreeUtil.prevVisibleLeaf(element);

                if (prevVisibleSibling != null && "=".equals(prevVisibleSibling.getText())) {
                    addCreateKeyword(result);
                }

                if (prevVisibleSibling != null && prevVisibleSibling.getText().matches("[;{}]")) {
                    // Todo - change method
                    addAnyTypeAsLookup(result);
                    addXmlnsAsLookup(result);
                    addValueTypesAsLookups(result);
                    addReferenceTypesAsLookups(result);

                    addCommonKeywords(result);
                    addFunctionSpecificKeywords(parameters, result);

                    result.addAllElements(BallerinaCompletionUtils.createCommonKeywords());
                }
                addValueKeywords(result);
            }

            ConnectorBodyNode connectorBodyNode = PsiTreeUtil.getParentOfType(element, ConnectorBodyNode.class);
            if (connectorBodyNode != null) {
                addConnectorSpecificKeywords(result);
            }

            ConnectorDefinitionNode connectorDefinitionNode = PsiTreeUtil.getParentOfType(element,
                    ConnectorDefinitionNode.class);
            if (connectorDefinitionNode != null) {
                addConnectorSpecificKeywords(result);
            }
            return;
        }

        if (parent instanceof NameReferenceNode) {

            MapStructKeyValueNode mapStructKeyValueNode = PsiTreeUtil.getParentOfType(parent,
                    MapStructKeyValueNode.class);

            if (mapStructKeyValueNode == null) {
                PsiElement prevVisibleSibling = PsiTreeUtil.prevVisibleLeaf(element);
                if (prevVisibleSibling != null && "{".equals(prevVisibleSibling.getText())) {
                    FunctionDefinitionNode functionDefinitionNode = PsiTreeUtil.getParentOfType(element,
                            FunctionDefinitionNode.class);

                    if (functionDefinitionNode != null) {

                        // Todo - change method
                        addAnyTypeAsLookup(result);
                        addXmlnsAsLookup(result);
                        addValueTypesAsLookups(result);
                        addReferenceTypesAsLookups(result);

                        addFunctionSpecificKeywords(parameters, result);

                        result.addAllElements(BallerinaCompletionUtils.createCommonKeywords());

                        addValueKeywords(result);
                    }


                    ServiceBodyNode serviceBodyNode = PsiTreeUtil.getParentOfType(element, ServiceBodyNode.class);
                    if (serviceBodyNode != null) {
                        result.addAllElements(createServiceSpecificKeywords());
                    }


                    ConnectorBodyNode connectorBodyNode = PsiTreeUtil.getParentOfType(element, ConnectorBodyNode.class);
                    if (connectorBodyNode != null) {
                        addConnectorSpecificKeywords(result);
                    }

                }

            }
        }


        if (parent instanceof ResourceDefinitionNode) {
            result.addAllElements(createServiceSpecificKeywords());
        }


        if (parent.getPrevSibling() == null) {

            GlobalVariableDefinitionNode globalVariableDefinitionNode = PsiTreeUtil.getParentOfType(element,
                    GlobalVariableDefinitionNode.class);
            if (globalVariableDefinitionNode != null) {
                PsiElement prevVisibleSibling = PsiTreeUtil.prevVisibleLeaf(element);
                if (prevVisibleSibling != null && !(";".equals(prevVisibleSibling.getText()))) {
                    return;
                }

                PsiElement definitionNode = globalVariableDefinitionNode.getParent();

                PackageDeclarationNode prevPackageDeclarationNode = PsiTreeUtil.getPrevSiblingOfType(definitionNode,
                        PackageDeclarationNode.class);

                ImportDeclarationNode prevImportDeclarationNode = PsiTreeUtil.getPrevSiblingOfType(definitionNode,
                        ImportDeclarationNode.class);

                ConstantDefinitionNode prevConstantDefinitionNode = PsiTreeUtil.getPrevSiblingOfType(definitionNode,
                        ConstantDefinitionNode.class);

                DefinitionNode prevDefinitionNode =
                        PsiTreeUtil.getPrevSiblingOfType(definitionNode, DefinitionNode.class);

                GlobalVariableDefinitionNode prevGlobalVariableDefinition =
                        PsiTreeUtil.findChildOfType(prevDefinitionNode, GlobalVariableDefinitionNode.class);

                if (prevPackageDeclarationNode == null && prevImportDeclarationNode == null
                        && prevConstantDefinitionNode == null && prevGlobalVariableDefinition == null) {
                    addFileLevelKeywordsAsLookups(result, true, true);
                } else if ((prevPackageDeclarationNode != null || prevImportDeclarationNode != null)
                        && prevConstantDefinitionNode == null && prevGlobalVariableDefinition == null) {
                    addFileLevelKeywordsAsLookups(result, false, true);
                } else {
                    addFileLevelKeywordsAsLookups(result, false, false);
                }

                addTypeNamesAsLookups(result);
            }
        }
    }
}
