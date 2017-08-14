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

package org.wso2.plugins.idea.actions;

import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.psi.PsiDirectory;

import java.util.Properties;

public class SiddhiTemplatePropertiesProvider implements DefaultTemplatePropertiesProvider {

    private static final String SIDDHI_PACKAGE_NAME = FileTemplate.ATTRIBUTE_PACKAGE_NAME;

    @Override
    public void fillProperties(PsiDirectory directory, Properties props) {

    }
}
