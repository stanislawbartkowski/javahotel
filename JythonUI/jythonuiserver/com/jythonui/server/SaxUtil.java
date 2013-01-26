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
package com.jythonui.server;

import org.xml.sax.Attributes;

import com.jythonui.shared.ElemDescription;

class SaxUtil {

    static void readAttr(ElemDescription bDescr, Attributes attributes,
            String[] currentT) {
        for (int i = 0; i < attributes.getLength(); i++) {
            String key = attributes.getQName(i);
            for (int k = 0; k < currentT.length; k++)
                if (key.equals(currentT[k])) {
                    String val = attributes.getValue(i);
                    bDescr.setAttr(key, val);
                    break;
                }
        }
    }

    static void readVal(ElemDescription bDescr, String qName,
            String[] currentT, StringBuffer buf) {
        if (bDescr != null) {
            for (int i = 0; i < currentT.length; i++) {
                if (currentT[i].equals(qName)) {
                    bDescr.setAttr(qName, buf.toString());
                    return;
                }
            }
        }

    }

}
