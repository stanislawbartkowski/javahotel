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

import java.util.List;
import java.util.Set;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.security.ISecurity;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.ElemDescription;
import com.jythonui.shared.SecurityInfo;

class AddSecurityInfo {

    private AddSecurityInfo() {
    }

    private static void scanList(ISecurity iSec, String token,
            Set<String> accessSet, Set<String> readonlySet,
            List<? extends ElemDescription> list) {
        for (ElemDescription e : list) {
            if (CUtil.EmptyS(token)) {
                // no security token, full access
                accessSet.add(e.getId());
                continue;
            }
            if (!e.isSecAccess()) {
                // access code is not set
                if (!e.isSecReadOnly()) {
                    // if no access code and no read only code full access
                    accessSet.add(e.getId());
                    continue;
                }
                // no access code but read only code
                if (iSec.isAuthorized(token, e.getSecReadOnly())) {
                    // read only access
                    readonlySet.add(e.getId());
                }
                // no access code, read only code and read only not permitted
                // not access at all
                continue;
            }
            // access code
            if (iSec.isAuthorized(token, e.getSecAccess())) {
                // access permitted
                accessSet.add(e.getId());
                continue;
            }
            if (!e.isSecReadOnly()) // no access
                continue;
            // read only code
            if (iSec.isAuthorized(token, e.getSecReadOnly())) {
                // read only access
                readonlySet.add(e.getId());
            }
        } // for

    }

    static SecurityInfo create(ISecurity iSec, String token, DialogFormat d) {
        SecurityInfo sInfo = new SecurityInfo();
        scanList(iSec, token, sInfo.getFieldAccess(), sInfo.getFieldReadOnly(),
                d.getFieldList());
        scanList(iSec, token, sInfo.getButtonAccess(),
                sInfo.getButtonReadOnly(), d.getButtonList());
        scanList(iSec, token, sInfo.getButtonAccess(),
                sInfo.getButtonReadOnly(), d.getLeftButtonList());
        return sInfo;
    }

}
