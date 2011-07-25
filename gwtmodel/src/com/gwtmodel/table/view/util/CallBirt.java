/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import com.google.gwt.user.client.Window;
import com.gwtmodel.table.Utils;
import java.util.Map;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
public class CallBirt {

    private CallBirt() {
    }

//    private static String paraS(String key, String value) {
//        String u = "&" + key + "=" + value;
//        return u;
//    }
//
//    private static String createURL(String u, String reportName, Map<String, String> args) {
//        String url = u + "?__report=" + reportName;
//        for (Entry<String, String> e : args.entrySet()) {
//            String param = e.getKey();
//            String val = e.getValue();
//            url += paraS(param, val);
//        }
//        return url;
//    }
    public static void callBirt(String url, String reportName, Map<String, String> args) {
        String u = Utils.createURL(url, "__report", reportName, args);
        Window.open(u, "", "");
    }
}
