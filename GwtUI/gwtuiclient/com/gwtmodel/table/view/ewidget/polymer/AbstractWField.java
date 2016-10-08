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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editw.IFormChangeListener;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.ITouchListener;
import com.gwtmodel.table.mm.MM;
import com.vaadin.polymer.elemental.EventListener;
import com.vaadin.polymer.iron.event.KeysPressedEvent;
import com.vaadin.polymer.paper.widget.event.ChangeEvent;
import com.vaadin.polymer.paper.widget.event.ChangeEventHandler;

abstract class AbstractWField implements IFormLineView {

	protected final IVField v;
	private final List<ITouchListener> lTouch = new ArrayList<ITouchListener>();
	private final List<IFormChangeListener> cList = new ArrayList<IFormChangeListener>();
	protected final IFormFieldProperties pr;
	private final String standErrMess;
	private final String htmlName;

	protected AbstractWField(IVField v, IFormFieldProperties pr, String standErrMess) {
		this.v = v;
		this.pr = pr;
		this.standErrMess = standErrMess;
		this.htmlName = pr.getHtmlId();
	}

	@Override
	public IVField getV() {
		return v;
	}

	@Override
	public void setOnTouch(ITouchListener lTouch) {
		this.lTouch.add(lTouch);
	}

	protected void runOnTouch() {
		for (ITouchListener i : lTouch)
			i.onTouch();
	}

	protected void onChangeEdit(boolean afterfocus) {
		for (IFormChangeListener c : cList)
			c.onChange(AbstractWField.this, afterfocus);
	}

	/* focus lost and something changed */
	protected class ChangeHa implements ChangeEventHandler {

		@Override
		public void onChange(ChangeEvent event) {
			onChangeEdit(true);
		}
	}

	protected final class TouchEvent implements EventListener<KeysPressedEvent> {

		@Override
		public void handleEvent(KeysPressedEvent event) {
			runOnTouch();
		}
	}

	@Override
	public void addChangeListener(IFormChangeListener cListener) {
		cList.add(cListener);
	}

	@Override
	public void setAttr(String attrName, String attrValue) {
		this.getGWidget().getElement().setAttribute(attrName, attrValue);
	}

	protected String getStandErrMess() {
		String emptyMess = null;
		if (pr.isNotEmpty())
			emptyMess = MM.getL().EmptyFieldMessage();
		String errMess = CUtil.joinS(',', emptyMess, standErrMess);
		return errMess;
	}

	@Override
	public String getHtmlName() {
		if (!CUtil.EmptyS(htmlName)) {
			return htmlName;
		}
		return v.getId();
	}

	@Override
	public void replaceWidget(Widget w) {
		Utils.ReplaceWidgetNotImplements(v.getId(),w.getClass().getName());
	}

}
