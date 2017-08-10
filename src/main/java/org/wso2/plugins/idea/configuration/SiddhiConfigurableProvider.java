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

package org.wso2.plugins.idea.configuration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableProvider;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.project.Project;
//import org.wso2.plugins.idea.codeInsight.imports.SiddhiAutoImportConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SiddhiConfigurableProvider extends ConfigurableProvider {

    @NotNull
    private final Project myProject;

    public SiddhiConfigurableProvider(@NotNull Project project) {
        myProject = project;
    }

    @Nullable
    @Override
    public Configurable createConfigurable() {
//        //        Configurable projectSettingsConfigurable = new SiddhiProjectSettingsConfigurable(myProject);
//        Configurable librariesConfigurable = new SiddhiLibrariesConfigurableProvider(myProject).createConfigurable();
//        Configurable autoImportConfigurable = new SiddhiAutoImportConfigurable(myProject, false);
//        // Todo: sdkConfigurable needed?
//        return new SiddhiCompositeConfigurable(librariesConfigurable, autoImportConfigurable);
        return null;
    }

    private static class SiddhiCompositeConfigurable extends SearchableConfigurable.Parent.Abstract {

        private Configurable[] myConfigurables;

        public SiddhiCompositeConfigurable(Configurable... configurables) {
            myConfigurables = configurables;
        }

        @Override
        protected Configurable[] buildConfigurables() {
            return myConfigurables;
        }

        @NotNull
        @Override
        public String getId() {
            return "Siddhi";
        }

        @Nls
        @Override
        public String getDisplayName() {
            return "Siddhi";
        }

        @Nullable
        @Override
        public String getHelpTopic() {
            return null;
        }

        @Override
        public void disposeUIResources() {
            super.disposeUIResources();
            myConfigurables = null;
        }
    }

    public static class SiddhiProjectSettingsConfigurable extends SiddhiModuleAwareConfigurable {

        public SiddhiProjectSettingsConfigurable(@NotNull Project project) {
            super(project, "Project Settings", null);
        }

        @NotNull
        @Override
        protected UnnamedConfigurable createModuleConfigurable(Module module) {
            return new SiddhiModuleSettingsConfigurable(module, false);
        }
    }
}
