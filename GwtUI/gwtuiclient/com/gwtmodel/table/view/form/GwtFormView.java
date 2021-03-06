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
package com.gwtmodel.table.view.form;

import java.util.List;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.editw.FormField;
import com.gwtmodel.table.editw.FormLineContainer;
import com.gwtmodel.table.editw.ITouchListener;
import com.gwtmodel.table.factories.IDataFormConstructorAbstractFactory;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.validate.ErrorLineContainer;
import com.gwtmodel.table.view.util.CreateFormView;
import com.gwtmodel.table.view.util.polymer.CreatePolymerForm;

class GwtFormView implements IGwtFormView {

	private final FormLineContainer fContainer;
	private Widget gg = null;
	final ErrorLineContainer eStore = new ErrorLineContainer();
	private HTMLPanel hp;

	private void setListener() {

		ITouchListener ii = new ITouchListener() {
			@Override
			public void onTouch() {
				eStore.clearE();
			}
		};
		for (FormField e : fContainer.getfList()) {
			e.getELine().setOnTouch(ii);
		}
	}

	GwtFormView(ICallContext iContext, final FormLineContainer fContainer,
			IDataFormConstructorAbstractFactory.CType cType, final ISignal iSignal) {
		this.fContainer = fContainer;
		if (cType.getfConstructor() == null) {
			if (fContainer.isPolymer())
				gg = CreatePolymerForm.construct(fContainer.getfList());
			else
				gg = CreateFormView.construct(fContainer.getfList());
			iSignal.signal();
		} else {
			ISetGWidget iSet = new ISetGWidget() {
				@Override
				public void set(IGWidget w) {
					gg = w.getGWidget();
					iSignal.signal();
				}
			};
			cType.getfConstructor().construct(iSet, iContext, fContainer);
		}
		hp = null;

		setListener();
	}

	@Override
	public Widget getGWidget() {
		if (hp == null) {
			return gg;
		}
		return hp;
	}

	@Override
	public void showInvalidate(InvalidateFormContainer errContainer) {
		List<InvalidateMess> col = errContainer.getErrMess();

		boolean something = false;
		for (InvalidateMess m : col) {
			IVField mFie = m.getFie();
			for (FormField re : fContainer.getfList()) {
				if ((mFie == null) || re.getFie().eq(mFie)) {
					eStore.setEMess(re.getELine(), m);
					something = true;
				}
			}
		}
		if (!something) {
			FormField re = fContainer.getfList().get(0);
			InvalidateMess m = col.get(0);
			eStore.setEMess(re.getELine(), m);
		}
	}

	@Override
	public void fillHtml(HTMLPanel pa, BinderWidget bw) {
		hp = pa;
		CreateFormView.setHtml(pa, fContainer.getfList(), bw);
	}

	@Override
	public void setHtmlId(String id, IGWidget g) {
		if (gg instanceof HTMLPanel) {
			HTMLPanel pa = (HTMLPanel) gg;
			CreateFormView.replace(pa, id, g.getGWidget());
		}
	}

}
