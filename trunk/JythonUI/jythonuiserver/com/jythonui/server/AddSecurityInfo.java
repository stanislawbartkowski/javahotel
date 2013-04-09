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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.security.ISecurity;
import com.jythonui.shared.DialSecurityInfo;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.ElemDescription;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.SecurityInfo;

class AddSecurityInfo {

    private AddSecurityInfo() {
    }

    private static void scanList(ISecurity iSec, String token,
            Map<String, Set<String>> secM, List<? extends ElemDescription> list) {
        for (ElemDescription e : list) {
            String id = e.getId();
            Iterator<String> i = e.getKeys();
            while (i.hasNext()) {
                String key = i.next();
                String sec = e.getSecuriyPart(key);
                if (CUtil.EmptyS(sec) || (iSec.isAuthorized(token, sec)))
                    DialSecurityInfo.setSec(secM, id, key);
            }
        }
    }

    static SecurityInfo create(ISecurity iSec, String token, DialogFormat d) {
        SecurityInfo sInfo = new SecurityInfo();
        scanList(iSec, token, sInfo.getFieldSec(), d.getFieldList());
        scanList(iSec, token, sInfo.getButtSec(), d.getButtonList());
        scanList(iSec, token, sInfo.getButtSec(), d.getLeftButtonList());
        return sInfo;
    }

    static SecurityInfo createForColumns(ISecurity iSec, String token,
            ListFormat li) {
        SecurityInfo sInfo = new SecurityInfo();
        scanList(iSec, token, sInfo.getFieldSec(), li.getColumns());
        return sInfo;
    }

}
