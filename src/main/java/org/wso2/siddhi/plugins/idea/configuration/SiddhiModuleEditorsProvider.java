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

package org.wso2.siddhi.plugins.idea.configuration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.*;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.wso2.siddhi.plugins.idea.SiddhiModuleType;

import javax.swing.*;
import java.util.List;

public class SiddhiModuleEditorsProvider implements ModuleConfigurationEditorProvider {

    @Override
    public ModuleConfigurationEditor[] createEditors(@NotNull ModuleConfigurationState state) {
        ModifiableRootModel rootModel = state.getRootModel();
        Module module = rootModel.getModule();
        if (!(ModuleType.get(module) instanceof SiddhiModuleType)) {
            return ModuleConfigurationEditor.EMPTY;
        }

        String moduleName = module.getName();
        List<ModuleConfigurationEditor> editors = ContainerUtil.newArrayList();
        editors.add(new ContentEntriesEditor(moduleName, state));
        editors.add(new OutputEditorEx(state));
        editors.add(new ClasspathEditor(state));
        return editors.toArray(new ModuleConfigurationEditor[editors.size()]);
    }

    static class OutputEditorEx extends OutputEditor {
        protected OutputEditorEx(ModuleConfigurationState state) {
            super(state);
        }

        @Override
        protected JComponent createComponentImpl() {
            JComponent component = super.createComponentImpl();
            component.remove(1);
            return component;
        }
    }
}
