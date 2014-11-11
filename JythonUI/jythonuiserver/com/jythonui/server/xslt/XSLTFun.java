/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.xslt;

import java.util.Map;

import com.jythonui.server.holder.Holder;

public class XSLTFun {

    private XSLTFun() {

    }

    public static String fillString(int length, String s) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < length; i++)
            b.append(s.charAt(0));
        return b.toString();
    }

    public static String paddingLeft(int padd, String s) {
        if (s.length() >= padd)
            return s;
        return fillString(padd - s.length(), " ") + s;
    }

    public static String paddingRight(int padd, String s) {
        if (s.length() >= padd)
            return s;
        return s + fillString(padd - s.length(), " ");
    }

    public static String m(String key) {
        Map<String, String> res = Holder.getLastBundle();
        if (res != null) {
            String val = res.get(key);
            if (val != null)
                return val;
        }
        return Holder.getAppMess().getCustomMess().getAttr(key);
    }

}
