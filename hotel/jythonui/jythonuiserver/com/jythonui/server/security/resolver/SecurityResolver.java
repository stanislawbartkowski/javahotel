/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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

import javax.inject.Named;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.JexlException;
import org.apache.commons.jexl2.MapContext;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;
import com.jythonui.server.IConsts;
import com.jythonui.server.IGetAppProp;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.security.ISecurityResolver;

public class SecurityResolver extends UtilHelper implements ISecurityResolver {

    private final IGetLogMess gMess;
    private final IGetAppProp iGet;

    @Inject
    public SecurityResolver(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess,
            IGetAppProp iGet) {
        this.gMess = gMess;
        this.iGet = iGet;
    }

    private Object evaluate(Subject currentUser, String permission) {
        JexlEngine jexl = new JexlEngine();
        // Create an expression object
        String jexlExp = permission;
        Expression e;

        try {
            e = jexl.createExpression(jexlExp);
        } catch (JexlException j) {
            severe(gMess.getMess(IErrorCode.ERRORCODE7, ILogMess.JEXLERROR,
                    permission), j);
            return null;
        }

        // Create a context and add data
        JexlContext jc = new MapContext();
        jc.set(IConsts.JECLSECFUNCTION, new SecurityFoo(currentUser));
        jc.set(IConsts.JECLENVFUNCTION, new EnvFoo(iGet));

        // Now evaluate the expression, getting the result
        Object o = e.evaluate(jc);
        if (o == null) {
            severe(gMess.getMess(IErrorCode.ERRORCODE11,
                    ILogMess.JEXLERROREVALUATION, permission));
            return null;
        }
        return o;

    }

    @Override
    public boolean isAuthorized(Subject currentUser, String permission) {
        Object o = evaluate(currentUser, permission);
        if (!(o instanceof Boolean)) {
            severe(gMess.getMess(IErrorCode.ERRORCODE10,
                    ILogMess.JEXLERRORNOTBOOLEAN, permission));
            return false;
        }
        Boolean b = (Boolean) o;
        return b.booleanValue();
    }

    @Override
    public String evaluateExpr(Subject currentUser, String expr) {
        Object o = evaluate(currentUser, expr);
        if (!(o instanceof String)) {
            severe(gMess.getMess(IErrorCode.ERRORCODE95,
                    ILogMess.JEXLERRORNOTSTRING, expr));
            return null;
        }
        return (String) o;
    }

}
