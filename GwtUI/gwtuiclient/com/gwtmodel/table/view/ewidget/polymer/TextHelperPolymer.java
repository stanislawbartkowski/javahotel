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
package com.gwtmodel.table.view.ewidget.polymer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.view.ewidget.comboutil.ChooseListHelper;
import com.gwtmodel.table.view.util.PopupUtil;

class TextHelperPolymer extends TextHelperImage {

	private final IDataType dType;

	private final boolean refreshAlways;

	final private ISetGWidget is = new ISetGWidget() {

		@Override
		public void set(IGWidget w) {
			pUp = new PopupPanel(true);
			PopupUtil.setPos(pUp, getGWidget());
			pUp.add(w.getGWidget());
			pUp.show();
		}
	};

	private PopupPanel pUp = null;

	private class ListC extends ChooseListHelper {

		protected ListC(IDataType dType) {
			super(dType);
		}

		@Override
		protected void asetValue(String sy) {
			setValObj(sy);
		}

		@Override
		protected void hide() {
			pUp.hide();
		}

	}

	private final IRequestForGWidget iReq;

	TextHelperPolymer(IVField v, IFormFieldProperties pr, IDataType dType, boolean refreshAlways) {
		super(v, pr, null, null, "vaadin-icons:arrow-down");
		this.dType = dType;
		this.refreshAlways = refreshAlways;
		iReq = new ListC(dType).getI();
		bu.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if ((pUp == null) || refreshAlways)
					iReq.run(v, new WSize(getGWidget()), is, null);
				else
					pUp.show();
			}
		});

	}

}
