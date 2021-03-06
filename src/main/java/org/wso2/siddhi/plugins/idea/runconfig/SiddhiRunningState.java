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

package org.wso2.siddhi.plugins.idea.runconfig;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;
import org.wso2.siddhi.plugins.idea.util.SiddhiExecutor;

public abstract class SiddhiRunningState<T extends SiddhiRunConfigurationBase<?>> extends CommandLineState {

    @NotNull
    private final Module myModule;

    @NotNull
    public T getConfiguration() {
        return myConfiguration;
    }

    @NotNull
    protected final T myConfiguration;

    public SiddhiRunningState(@NotNull ExecutionEnvironment env, @NotNull Module module, @NotNull T configuration) {
        super(env);
        myModule = module;
        myConfiguration = configuration;
        addConsoleFilters(new SiddhiConsoleFilter(myConfiguration.getProject(), myModule,
                myConfiguration.getWorkingDirectoryUrl()));
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        SiddhiExecutor executor = patchExecutor(createCommonExecutor());
        // We only need to set
        GeneralCommandLine commandLine;
        if (myConfiguration instanceof SiddhiRunConfigurationWithMain) {
            RunConfigurationKind runKind = ((SiddhiRunConfigurationWithMain) myConfiguration).getRunKind();
            if (runKind == RunConfigurationKind.MAIN) {
                commandLine = executor.withParameterString(myConfiguration.getParams()).createCommandLine();
            } else {
                commandLine = executor.createCommandLine();
            }
        } else {
            commandLine = executor.withParameterString(myConfiguration.getParams()).createCommandLine();
        }
        KillableColoredProcessHandler handler = new KillableColoredProcessHandler(commandLine, true);
        ProcessTerminatedListener.attach(handler);
        return handler;
    }

    @NotNull
    private SiddhiExecutor createCommonExecutor() {
        return SiddhiExecutor.in(myModule).withWorkDirectory(myConfiguration.getWorkingDirectory())
                .withExtraEnvironment(myConfiguration.getCustomEnvironment())
                .withPassParentEnvironment(myConfiguration.isPassParentEnvironment());
    }

    protected SiddhiExecutor patchExecutor(@NotNull SiddhiExecutor executor) throws ExecutionException {
        return executor;
    }
}
