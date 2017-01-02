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
package com.jythonui.server.mail;

import com.gwtmodel.table.shared.RMap;
import com.jythonui.server.ISharedConsts;

public class NoteAttach extends RMap {

    private static final long serialVersionUID = 1L;

    public String getFileName() {
        return getAttr(ISharedConsts.MAILATTACHFILENAME);
    }

    public void setFileName(String fileName) {
        setAttr(ISharedConsts.MAILATTACHFILENAME, fileName);
    }

    public String getRealm() {
        return getAttr(ISharedConsts.MAILATTACHREALM);
    }

    public void setRealm(String realm) {
        setAttr(ISharedConsts.MAILATTACHREALM, realm);
    }

    public String getBlobKey() {
        return getAttr(ISharedConsts.MAILATTACHKEY);
    }

    public void setBlobKey(String blobkey) {
        setAttr(ISharedConsts.MAILATTACHKEY, blobkey);
    }

}
