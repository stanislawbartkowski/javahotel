/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.webpanel.IWebPanel;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
public abstract class CommonCallBack<T> implements AsyncCallback<T> {

    abstract public void onMySuccess(final T arg);
    final private IWebPanel iWeb;
    final private onFailureExt ext;

    public interface onFailureExt {

        boolean doSth(Throwable ext);
    }

    public CommonCallBack() {
        iWeb = GwtGiniInjector.getI().getWebPanel();
        iWeb.IncDecCounter(true);
        ext = null;
    }

    protected void incC(final int no) {
        for (int i = 0; i < no; i++) {
            iWeb.IncDecCounter(true);
        }
    }

    public CommonCallBack(final onFailureExt e) {
        iWeb = GwtGiniInjector.getI().getWebPanel();
        iWeb.IncDecCounter(true);
        ext = e;
    }

    @Override
    public void onSuccess(final T arg) {
        iWeb.IncDecCounter(false);
        onMySuccess(arg);
    }

    @Override
    public void onFailure(final Throwable caught) {
        iWeb.IncDecCounter(false);
        if (ext != null) {
            if (ext.doSth(caught)) {
                return;
            }
        }
        ITableCustomFactories fa = GwtGiniInjector.getI().getTableFactoriesContainer();
        IGetCustomValues va = fa.getGetCustomValues();
        String cMessage = va.getCustomValue(IGetCustomValues.COMMERROR);
        if (caught.getMessage() != null) {
            cMessage += " " + caught.getMessage();
        }
        iWeb.setErrorL(cMessage);
    }
}
