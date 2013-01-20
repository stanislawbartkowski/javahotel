/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.jythonui.client.util;

import com.jythonui.shared.DialogVariables;

/**
 * @author hotel
 * 
 */
public class JUtils {

    private JUtils() {
    }

    public interface IVisitor {
        void action(String fie, String field);
    };

    public static void visitListOfFields(DialogVariables var, String prefix,
            IVisitor i) {
        for (String key : var.getFields())
            if (key.startsWith(prefix)) {
                String fie = key.substring(prefix.length());
                i.action(fie, key);
            }
    }

}