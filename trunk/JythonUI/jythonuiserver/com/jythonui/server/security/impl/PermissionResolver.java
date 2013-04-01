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
package com.jythonui.server.security.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.logmess.LogMess;
import com.jythonui.shared.ICommonConsts;

class PermissionResolver {

    private static final Logger log = Logger.getLogger(PermissionResolver.class
            .getName());

    static class S {
        final List<String> users;
        final List<String> roles;
        final List<String> permission;

        S(List<String> users, List<String> roles, List<String> permission) {
            this.users = users;
            this.roles = roles;
            this.permission = permission;
        }
    }

    static private boolean extractElem(List<String> li, String s,
            String beginner) {
        int i = s.indexOf(beginner);
        if (i == 0) {
            li.add(s.substring(beginner.length()));
            return true;
        }
        return false;
    }

    static S resolvePermission(String pe) {
        List<String> users = new ArrayList<String>();
        List<String> roles = new ArrayList<String>();
        List<String> permission = new ArrayList<String>();
        String[] li = pe.split(ICommonConsts.PERMDELIMITER);
        for (String s : li) {
            if (extractElem(users, s, ICommonConsts.UPERM))
                continue;
            if (extractElem(roles, s, ICommonConsts.RPERM))
                continue;
            if (extractElem(permission, s, ICommonConsts.PPERM))
                continue;
            log.log(Level.SEVERE, LogMess.getMess(IErrorCode.ERRORCODE7,
                    ILogMess.PERMISTRINGERR, pe, s, ICommonConsts.UPERM,
                    ICommonConsts.RPERM, ICommonConsts.PPERM));
        }
        return new S(users, roles, permission);
    }
}
