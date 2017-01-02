/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jythonui.client.impl;

import com.jythonui.client.IJythonUIClient;
import com.jythonui.client.M;
import com.jythonui.client.dialog.CreateFactory;
import com.jythonui.client.dialog.LeftMenu;
import com.jythonui.client.dialog.run.RunAction;

/**
 * @author hotel
 * 
 */
public class JythonUIClientFactory {

    private JythonUIClientFactory() {
    }

    public static IJythonUIClient construct() {
        CreateFactory.create();
        M.setLeftMenu(new LeftMenu());
        return new RunAction();
    }

}
