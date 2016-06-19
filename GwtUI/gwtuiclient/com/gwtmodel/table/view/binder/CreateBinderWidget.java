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
package com.gwtmodel.table.view.binder;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gwt.dev.jjs.ast.HasName.Util;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.mm.LogT;

public class CreateBinderWidget {

	private CreateBinderWidget() {

	}

	private static void setWAttribute(Widget w, BinderWidget bw) {
		if (bw.isFieldId())
			Utils.setWidgetAttribute(w, BinderWidget.FIELDID, bw.getFieldId());
		Iterator<String> i = bw.getKeys();
		while (i.hasNext()) {
			String k = i.next();
			String v = bw.getAttr(k);
			if (k.equals(IConsts.ATTRHEIGHT))
				w.setHeight(v);
			else if (k.equals(IConsts.ATTRSIZE)) {
				String[] pp = v.split(" ");
				if (pp.length == 2)
					w.setSize(pp[0], pp[1]);
			} else if (k.equals(IConsts.ATTRPIXELSIZE)) {
				String[] pp = v.split(" ");
				if (pp.length == 2)
					w.setPixelSize(CUtil.toInteger(pp[0]), CUtil.toInteger(pp[1]));
			} else if (k.equals(IConsts.ATTRSTYLENAME))
				w.setStyleName(v);
			else if (k.equals(IConsts.ATTRSTYLEPRIMARYNAME))
				w.setStylePrimaryName(v);
			else if (k.equals(IConsts.ATTRVISIBLE))
				w.setVisible(Boolean.parseBoolean(v));
			else if (k.equals(IConsts.ATTRTITLE))
				w.setTitle(v);
			else if (k.equals(IConsts.ATTRWIDTH))
				w.setWidth(v);
		}
	}

	private static void setLabelAttribute(Label l, BinderWidget bw) {
		Iterator<String> i = bw.getKeys();
		while (i.hasNext()) {
			String k = i.next();
			String v = bw.getAttr(k);
			if (k.equals(IConsts.ATTRTEXT))
				l.setText(v);
		}
	}

	private static Widget createWidget(BinderWidget bw) {
		Widget w = null;
		switch (bw.getType()) {
		case HTMLPanel:
			w = new HTMLPanel(bw.getContentHtml());
			break;
		case Button:
			w = new Button(bw.getContentHtml());
			break;
		case Label:
			Label l = new Label(bw.getContentHtml());
			setLabelAttribute(l, bw);
			w = l;
			break;
		} // switch
		setWAttribute(w, bw);
		if (bw.getwList().isEmpty())
			return w;
		if (!(w instanceof HasWidgets))
			Utils.errAlertB(LogT.getT().BinderCannotHaveWidgets(bw.getType().name()));
		HTMLPanel h = null;
		HasWidgets hw = null;
		if (w instanceof HTMLPanel)
			h = (HTMLPanel) w;
		else
			hw = (HasWidgets) w;
		for (BinderWidget c : bw.getwList()) {
			Widget child = createWidget(c);
			Element ee = h.getElementById(c.getId());
			if (ee == null)
				Utils.errAlert(LogT.getT().BinderCannotFindWidget(c.getId()));

			if (h != null) {
				// String html = h.toString();
				try {
					h.addAndReplaceElement(child, ee);
				} catch (NoSuchElementException e) {
					Utils.errAlert(c.getId(), bw.getContentHtml(), e);
				}
			} else
				hw.add(child);

		} // for
		return w;
	}

	public static HTMLPanel create(BinderWidget w) {
		if (w.getwList().isEmpty())
			Utils.errAlertB(LogT.getT().BinderWidgetNoPanels());
		BinderWidget p = w.getwList().get(0);
		Widget ww = createWidget(p);
		if (ww instanceof HTMLPanel)
			return (HTMLPanel) ww;
		Utils.errAlert(LogT.getT().BinderNotHTMLPanel(p.getType().name()));
		return null;
	}

}
