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
package com.gwtmodel.table.view.util;

import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import java.util.Map;

public class GetActionName {

    private GetActionName() {
    }

    /**
     * Get action string related to action enum
     * @param action Action enm
     * @return String or null
     */
    public static String getActionName(ClickButtonType.StandClickEnum action) {
        Map<String, String> ma = MM.getL().ActionName();
        return ma.get(action.toString());
    }
}
