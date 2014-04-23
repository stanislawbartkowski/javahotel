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
package com.jythonui.server;

import java.util.Map;

import org.xml.sax.Attributes;

public class SaxUtil {

    public static void readAttr(Map<String, String> bDescr,
            Attributes attributes, String[] currentT) {
        for (int i = 0; i < attributes.getLength(); i++) {
            String key = attributes.getQName(i);
            boolean set = false;
            if (currentT == null)
                set = true;
            else
                for (String s : currentT) {
                    if (key.equals(s.toLowerCase())) {
                        // String val = attributes.getValue(i);
                        // bDescr.put(s, val);
                        set = true;
                        break;
                    }
                }
            if (set) {
                String val = attributes.getValue(i);
                bDescr.put(key, val);
            }
        }
    }

    public static void readVal(Map<String, String> bDescr, String qName,
            String[] currentT, StringBuffer buf) {
        if (bDescr != null) {
            boolean set = false;
            if (currentT == null)
                set = true;
            else
                for (String s : currentT) {
                    if (s.toLowerCase().equals(qName)) {
                        set = true;
                        break;
                    }
                }
            if (set)
                bDescr.put(qName, buf.toString());
        }

    }

}
