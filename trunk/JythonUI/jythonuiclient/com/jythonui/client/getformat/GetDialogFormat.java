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
package com.jythonui.client.getformat;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.client.M;
import com.jythonui.client.injector.UIGiniInjector;
import com.jythonui.client.interfaces.IGetDialogFormat;
import com.jythonui.client.interfaces.IMemCache;
import com.jythonui.shared.DialogInfo;

public class GetDialogFormat implements IGetDialogFormat {

    private String lastUser = null;

    private final IMemCache mCache;

    @Inject
    public GetDialogFormat(IMemCache mCache) {
        this.mCache = mCache;
    }

    private class CacheBack implements AsyncCallback<DialogInfo> {

        private final AsyncCallback<DialogInfo> clCall;
        private final String dialogName;

        CacheBack(AsyncCallback<DialogInfo> clCall, String dialogName) {
            this.clCall = clCall;
            this.dialogName = dialogName;
        }

        @Override
        public void onFailure(Throwable caught) {
            clCall.onFailure(caught);
        }

        @Override
        public void onSuccess(DialogInfo result) {
            mCache.put(dialogName, result);
            clCall.onSuccess(result);
        }

    }

    @Override
    public void getDialogFormat(String dialogName,
            AsyncCallback<DialogInfo> callback) {
        if (!M.isCached()) {
            M.JR().getDialogFormat(UIGiniInjector.getI().getRequestContext(),
                    dialogName, callback);
            return;
        }
        boolean theSame = false;
        if (lastUser == null && M.getUserName() == null) theSame = true;
        if (lastUser != null && M.getUserName() != null) theSame = CUtil.EqNS(lastUser,M.getUserName());
        if (!theSame) {
            mCache.clear();
            lastUser = M.getUserName();
        }
        DialogInfo i = (DialogInfo) mCache.get(dialogName);
        if (i != null) {
            callback.onSuccess(i);
            return;
        }
        M.JR().getDialogFormat(UIGiniInjector.getI().getRequestContext(),
                dialogName, new CacheBack(callback, dialogName));
    }

}
