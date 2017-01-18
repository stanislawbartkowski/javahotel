/*
F * Copyright 2017 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not ue this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.gwtmodel.table.view.ewidget.polymer;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IResponseJson;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.mm.LogT;
import com.vaadin.polymer.iron.widget.IronAjax;
import com.vaadin.polymer.iron.widget.event.ErrorEvent;
import com.vaadin.polymer.iron.widget.event.ErrorEventHandler;
import com.vaadin.polymer.iron.widget.event.ResponseEventHandler;

class AjaxWidget extends AbstractWField {

	private IronAjax aj = new IronAjax();

	private final IResponseJson iR;

	protected AjaxWidget(IVField v, IFormFieldProperties pr, IResponseJson iR) {
		super(v, pr, null);
		this.iR = iR;
	}


	@Override
	public Object getValObj() {
		return null;
	}

	@Override
	public void setValObj(Object o) {
	}

	@Override
	public Widget getGWidget() {
		return aj;
	}

	@Override
	public void replaceWidget(Widget w) {
		if (w instanceof IronAjax) {
			aj = (IronAjax) w;
			aj.addAttachHandler(new AttachEvent.Handler() {

				@Override
				public void onAttachOrDetach(AttachEvent event) {

					if (!event.isAttached())
						return;
					// only isAttached
					if (!CUtil.EmptyS(aj.getUrl()))
						aj.generateRequest();
				}
			});
			aj.addResponseHandler(new ResponseEventHandler() {

				@Override
				public void onResponse(com.vaadin.polymer.iron.widget.event.ResponseEvent event) {
					iR.run(v, aj.getLastResponse());
				}
			});
			aj.addErrorHandler(new ErrorEventHandler() {

				@Override
				public void onError(ErrorEvent event) {
					Utils.errAlert(aj.getUrl(), LogT.getT().ErrorWhileRunningAjax(aj.getLastError().toString()));
				}
			});

			return;
		}
		Utils.ReplaceForClassNotImplemented(v.getId(), w.getClass().getName());
	}

}
