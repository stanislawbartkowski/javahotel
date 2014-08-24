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
package com.jythonui.server.dialog;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.IConsts;
import com.jythonui.server.SaxUtil;
import com.jythonui.server.security.ISecurity;

class EvaluateJexlValue implements SaxUtil.ITransformVal {

    private final ISecurity iSec;
    private final String token;

    EvaluateJexlValue(String token, ISecurity iSec) {
        this.iSec = iSec;
        this.token = token;
    }

    @Override
    public String transform(String val) {
        if (CUtil.EmptyS(val))
            return val;
        if (val.length() <= 1 || val.charAt(0) != IConsts.EVALSIGN)
            return val;
        return iSec.evaluateExpr(token, val.substring(1));
    }

    private static int findSecurity(String s) {
        if (CUtil.EmptyS(s))
            return -1;
        int startp = 0;
        int len = s.length();

        while (true) {
            int pos = s.indexOf(IConsts.PERMSIGN, startp);
            if (pos == -1)
                return -1;
            if (pos == len - 1)
                return -1; // empty security
            if (s.charAt(pos + 1) != IConsts.PERMSIGN)
                return pos;
            // double (escaped) $
            startp = pos + 2;
        }
    }

    private static String getSecurityPart(String val) {
        int sec = findSecurity(val);
        if (sec == -1)
            return null;
        return val.substring(sec + 1);
    }

    @Override
    public String getSecurity(String val) {
        String secPart = getSecurityPart(val);
        if (secPart == null)
            return val;
        if (token == null || !iSec.isAuthorized(token, secPart))
            return null;
        int sec = findSecurity(val);
        if (sec == 0)
            return "";
        return val.substring(0, sec - 1); // remove security part
    }

}
