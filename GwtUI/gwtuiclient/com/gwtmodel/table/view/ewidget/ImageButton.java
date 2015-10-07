/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.ewidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.rdef.IGetListOfIcons;

class ImageButton extends AbstractField {

	private final HorizontalPanel hPanel = new HorizontalPanel();
	private final IGetListOfIcons iList;
	private String value;

	private class Clicked implements ClickHandler {

		private final int no;

		Clicked(int no) {
			this.no = no;
		}

		@Override
		public void onClick(ClickEvent event) {
			iList.signalClick(new GWidget(hPanel), v, no);
		}

	}

	ImageButton(IGetCustomValues cValues, IVField v, String htmlName, int noImages, IGetListOfIcons iList) {
		super(cValues, v, false, htmlName);
		this.iList = iList;
		initWidget(hPanel);
		for (int i = 0; i < noImages; i++) {
			HTML l = new HTML();
			hPanel.add(l);
			l.addClickHandler(new Clicked(i));
		}
	}

	@Override
	public void setReadOnly(boolean readOnly) {
	}

	@Override
	public void setFocus(boolean focus) {
	}

	@Override
	public Object getValObj() {
		return value;
	}

	@Override
	public void setValObj(Object o) {
		value = (String) o;
		String[] ima = iList.getList(v);
		for (int i = 0; i < ima.length; i++) {
			HTML h = (HTML) hPanel.getWidget(i);
			if (IConsts.EMPTYIM.equals(ima[i]))
				h.setHTML("");
			else {
				String s = Utils.getImageHTML(ima[i]);
				SafeHtml html = SafeHtmlUtils.fromTrustedString(s);
				h.setHTML(html);
			}
		}
	}

}
