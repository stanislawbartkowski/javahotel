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

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.TOptional;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.view.ewidget.comboutil.AddBoxValues;
import com.gwtmodel.table.view.ewidget.comboutil.IValueLB;
import com.jythonui.shared.ICommonConsts;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.elemental.Function;
import com.vaadin.polymer.iron.widget.event.IronSelectEvent;
import com.vaadin.polymer.iron.widget.event.IronSelectEventHandler;
import com.vaadin.polymer.paper.PaperMenuElement;
import com.vaadin.polymer.paper.widget.PaperDropdownMenu;
import com.vaadin.polymer.paper.widget.PaperItem;
import com.vaadin.polymer.paper.widget.event.PaperDropdownOpenEvent;
import com.vaadin.polymer.paper.widget.event.PaperDropdownOpenEventHandler;

class PolymerCombo extends AbstractWField implements IValueLB {

	private final boolean addEmpty;

	private PaperMenuElement pMenu;
	private Element pMenuElem;
	private PaperDropdownMenu pDown;
	private List<String> ids;
	private List<String> vals;
	private TOptional<String> beforeVal = null;

	private String openid;

	private String html = "<paper-menu id=\"" + ICommonConsts.DROPMENUID + "\" class=\"" + ICommonConsts.DROPDOWNCONTENT
			+ "\"></paper-menu>";

	private final SynchronizeList iS = new SynchronizeList(2) {

		@Override
		protected void doTask() {
			if (addEmpty)
				Utils.addE(pMenuElem, new PaperItem("&nbsp;").getElement());
			for (String s : vals)
				Utils.addE(pMenuElem, new PaperItem(s).getElement());
		}

	};

	protected PolymerCombo(IVField v, IFormFieldProperties pr, IGetDataList iGet, boolean addEmpty) {
		super(v, pr, null);
		this.addEmpty = addEmpty;
		pDown = new PaperDropdownMenu(html);
		pDown.setLabel(v.getLabel());
		AddBoxValues.addValues(v, iGet, this);
		pDown.addDomHandler(new IronSelectEventHandler() {

			@Override
			public void onIronSelect(IronSelectEvent event) {
				String curr = (String) getValObj();
				if (!CUtil.EqNS(openid, curr))
					onChangeEdit(false);
			}
		}, IronSelectEvent.TYPE);
		Polymer.ready(pDown.getElement(), new Function() {

			@Override
			public Object call(Object arg) {
				pMenuElem = pDown.getElementById(ICommonConsts.DROPMENUID);
				pMenu = (PaperMenuElement) pMenuElem;
				pDown.addPaperDropdownOpenHandler(new PaperDropdownOpenEventHandler() {

					@Override
					public void onPaperDropdownOpen(PaperDropdownOpenEvent event) {
						openid = (String) getValObj();
					}
				});
				iS.signalDone();
				return null;
			}
		});

	}

	@Override
	public boolean addEmpty() {
		return addEmpty;
	}

	@Override
	public TOptional<String> getBeforeVal() {
		return beforeVal;
	}

	@Override
	public Widget getGWidget() {
		return pDown;
	}

	@Override
	public Object getValObj() {
		if (pMenu == null)
			return null;
		String id = pMenu.getSelected();
		if (id == null)
			return null;
		int inde = CUtil.toInteger(id);
		String o = ids.get(inde);
		return o;
	}

	@Override
	public boolean isInvalid() {
		return pDown.getInvalid();
	}

	@Override
	public void setCellTitle(String title) {
		// pMenu.setTitle(title);
	}

	@Override
	public void setFocus(boolean focus) {
		pDown.setFocused(focus);
	}

	@Override
	public void setGStyleName(String styleMess, boolean set) {
	}

	@Override
	public void setHidden(boolean hidden) {
		pDown.setVisible(!hidden);
	}

	@Override
	public void setIdList(List<String> li) {
		ids = li;
	}

	@Override
	public void setInvalidMess(String errmess) {
		if (errmess == null)
			pDown.setInvalid(false);
		else
			pDown.setInvalid(true);
	}

	@Override
	public void setList(List<String> li) {
		vals = li;
		iS.signalDone();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		pDown.setDisabled(readOnly);
	}

	@Override
	public void setSuggestList(List<String> list) {
	}

	@Override
	public void setValObj(Object o) {
		String s = (String) o;
		if (ids == null || pMenu == null)
			beforeVal = (TOptional<String>) TOptional.fromNullable(s);
		else {
			int inde = ids.indexOf(s);
			if (inde == -1)
				return;
			pMenu.setSelected(CUtil.NumbToS(inde));
		}
	}

}
