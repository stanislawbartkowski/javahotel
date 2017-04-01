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
package com.jythonui.server;

import java.util.Map;

import org.xml.sax.Attributes;

public class SaxUtil {

    public interface ITransformVal {

        String transform(String val);

        String getSecurity(String val);
    }

    public static void readAttr(Map<String, String> bDescr,
            Attributes attributes, String[] currentT, ITransformVal iT) {
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
                if (iT != null) {
                    val = iT.getSecurity(val);
                    if (val == null)
                        continue;
                    val = iT.transform(val);
                }
                bDescr.put(key, val);
            }
        }
    }

    public static void readVal(Map<String, String> bDescr, String qName,
            String[] currentT, StringBuffer buf, ITransformVal iT) {
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
            if (set) {
                String val = buf.toString();
                if (iT != null) {
                    val = iT.getSecurity(val);
                    if (val == null)
                        return;
                    val = iT.transform(val);
                }
                bDescr.put(qName, val);
            }
        }

    }

}
