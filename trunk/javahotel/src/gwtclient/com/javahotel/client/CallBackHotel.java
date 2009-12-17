/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public abstract class CallBackHotel<T> implements AsyncCallback<T> {

    abstract public void onMySuccess(final T arg);

    final private IResLocator pLocator;
    final private onFailureExt ext;

    public interface onFailureExt {

        boolean doSth(Throwable ext);
    }

    public CallBackHotel(final IResLocator p) {
        pLocator = p;
        pLocator.IncDecCounter(true);
        ext = null;
    }

    protected void incC(final int no) {
        for (int i = 0; i < no; i++) {
            pLocator.IncDecCounter(true);
        }
    }

    public CallBackHotel(final IResLocator p, final onFailureExt e) {
        pLocator = p;
        pLocator.IncDecCounter(true);
        ext = e;
    }

    public void onSuccess(final T arg) {
        pLocator.IncDecCounter(false);
        onMySuccess(arg);
    }

    public void onFailure(final Throwable caught) {
        pLocator.IncDecCounter(false);
        if (ext != null) {
            if (ext.doSth(caught)) {
                return;
            }
        }
        pLocator.getPanel().setErrorL(pLocator.getLabels().commError());
    }
}
