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

/**
 * @author hotel
 * 
 */
public class ButtonItem extends ElemDescription {

    private static final long serialVersionUID = 1L;

    public boolean isAction() {
        return isAttr(ICommonConsts.ACTIONTYPE);
    }

    public String getAction() {
        return getAttr(ICommonConsts.ACTIONTYPE);
    }

    public String getActionParam() {
        return getAttr(ICommonConsts.ACTIONPARAM);
    }

    public boolean isValidateAction() {
        return isAttr(ICommonConsts.VALIDATE);
    }

    public boolean isHeaderButton() {
        return isAttr(ICommonConsts.BUTTONHEADER);
    }

}
