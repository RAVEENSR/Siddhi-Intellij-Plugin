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

package org.wso2.siddhi.plugins.idea.debugger.protocol;

public enum Command {

    START("START"), STOP("STOP"), SET_POINTS("SET_POINTS"), STEP_OVER("STEP_OVER"), RESUME("RESUME") ,CMD_SEND_EVENT
            ("SEND_EVENT"), REMOVE_BREAKPOINT("REMOVE_BREAKPOINT");

    private String myCommand;

    Command(String command) {
        myCommand = command;
    }

    @Override
    public String toString() {
        return myCommand;
    }
}
