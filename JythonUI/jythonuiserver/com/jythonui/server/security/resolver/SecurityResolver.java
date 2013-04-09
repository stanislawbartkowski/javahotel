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
package com.jythonui.server.security.resolver;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.JexlException;
import org.apache.commons.jexl2.MapContext;
import org.apache.shiro.subject.Subject;

import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.logmess.LogMess;
import com.jythonui.server.security.ISecurityResolver;

public class SecurityResolver implements ISecurityResolver {

    private static final Logger log = Logger.getLogger(SecurityResolver.class
            .getName());

    @Override
    public boolean isAuthorized(Subject currentUser, String permission) {
        JexlEngine jexl = new JexlEngine();
        // Create an expression object
        String jexlExp = permission;
        Expression e;

        try {
            e = jexl.createExpression(jexlExp);
        } catch (JexlException j) {
            log.log(Level.SEVERE, LogMess.getMess(IErrorCode.ERRORCODE7,
                    ILogMess.JEXLERROR, permission), j);
            return false;
        }

        // Create a context and add data
        JexlContext jc = new MapContext();
        jc.set("sec", new SecurityFoo(currentUser));

        // Now evaluate the expression, getting the result
        Object o = e.evaluate(jc);
        if (o == null) {
            log.severe(LogMess.getMess(IErrorCode.ERRORCODE11,
                    ILogMess.JEXLERROREVALUATION, permission));
            return false;
        }
        if (!(o instanceof Boolean)) {
            log.severe(LogMess.getMess(IErrorCode.ERRORCODE10,
                    ILogMess.JEXLERRORNOTBOOLEAN, permission));
            return false;
        }
        Boolean b = (Boolean) o;
        return b.booleanValue();
    }

}
