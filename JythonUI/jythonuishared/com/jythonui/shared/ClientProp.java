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

package com.jythonui.shared;

import com.gwtmodel.table.common.CUtil;

public class ClientProp extends ElemDescription {

    private static final long serialVersionUID = 1L;
    private CustomMessages customM = null;
    private boolean isCached = false;

    public CustomMessages getCustomM() {
        return customM;
    }

    public void setCustomM(CustomMessages customM) {
        this.customM = customM;
    }

    public boolean isAuthenticate() {
        String s = this.getAttr(ICommonConsts.AUTHENTICATE);
        if (CUtil.EmptyS(s))
            return false;
        return CUtil.EqNS(s, ICommonConsts.YESAUTHENTICATE);
    }

    public boolean addLogOut() {
        String s = this.getAttr(ICommonConsts.CLOSEOUT);
        if (CUtil.EmptyS(s))
            return false;
        return CUtil.EqNS(s, ICommonConsts.YESAUTHENTICATE);

    }

    public boolean isLoginPage() {
        return isAttr(ICommonConsts.LOGINPAGE);
    }

    public String getLoginPage() {
        return getAttr(ICommonConsts.LOGINPAGE);
    }

    public boolean isCached() {
        return isCached;
    }

    public void setCached(boolean isCached) {
        this.isCached = isCached;
    }

}
