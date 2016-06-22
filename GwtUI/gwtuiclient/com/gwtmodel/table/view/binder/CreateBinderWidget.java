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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.binder.WidgetTypes;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.smessage.IGetStandardMessage;

@SuppressWarnings("unchecked")
public class CreateBinderWidget implements ICreateBinderWidget {

	private final IGetStandardMessage cValues;

	@Inject
	public CreateBinderWidget(IGetStandardMessage cValues) {
		this.cValues = cValues;
	}

	private interface IVisitor<T extends Widget> {
		void visit(T w, String k, String v);
	}

	private String convert(String s) {
		StringBuffer b = new StringBuffer(s);
		while (true) {
			int i = b.indexOf(IConsts.RESBEG);
			if (i == -1)
				break;
			int k = b.indexOf(IConsts.RESEND, i);
			if (k == -1)
				break;
			String res = b.substring(i + 1, k);
			// together with $
			String v = cValues.getMessage(res);
			b.replace(i, k+2, v);
		}
		return b.toString();
	}

	private final static IVisitor<Widget> argW = new IVisitor<Widget>() {

		@Override
		public void visit(Widget w, String k, String v) {
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
				w.setVisible(Utils.toB(v));
			else if (k.equals(IConsts.ATTRTITLE))
				w.setTitle(v);
			else if (k.equals(IConsts.ATTRWIDTH))
				w.setWidth(v);
		}
	};

	private static final IVisitor<Label> argL = new IVisitor<Label>() {

		@Override
		public void visit(Label l, String k, String v) {
			if (k.equals(IConsts.ATTRTEXT))
				l.setText(v);
		}
	};

	private static final IVisitor<FocusWidget> argF = new IVisitor<FocusWidget>() {

		@Override
		public void visit(FocusWidget w, String k, String v) {
			if (k.equals(IConsts.ATTREENABLED))
				w.setEnabled(Utils.toB(v));
		}
	};

	private static final IVisitor<ButtonBase> argbaseB = new IVisitor<ButtonBase>() {

		@Override
		public void visit(ButtonBase w, String k, String v) {
			if (k.equals(IConsts.ATTRTEXT))
				w.setText(v);
			if (k.equals(IConsts.ATTRHTML))
				w.setHTML(v);
		}

	};

	private final static Map<WidgetTypes, IVisitor<Widget>[]> setAWidget = new HashMap<WidgetTypes, IVisitor<Widget>[]>();

	static {
		setAWidget.put(WidgetTypes.Button, new IVisitor[] { argF, argbaseB });
		setAWidget.put(WidgetTypes.Label, new IVisitor[] { argL });
		setAWidget.put(WidgetTypes.HTMLPanel, new IVisitor[] {});
	}

	private <T extends Widget> void setAttr(T w, BinderWidget bw, IVisitor<T>... vil) {
		Iterator<String> i = bw.getKeys();
		while (i.hasNext()) {
			String k = i.next();
			String v = convert(bw.getAttr(k));
			argW.visit(w, k, v);
			for (IVisitor<T> vi : vil)
				vi.visit(w, k, v);
		}
	}

	@SuppressWarnings("unchecked")
	private void setWAttribute(Widget w, BinderWidget bw) {
		if (bw.isFieldId())
			Utils.setWidgetAttribute(w, BinderWidget.FIELDID, bw.getFieldId());
		setAttr(w, bw, setAWidget.get(bw.getType()));
	}

	private Widget createWidget(BinderWidget bw) {
		Widget w = null;
		String html = "";
		if (!CUtil.EmptyS(bw.getContentHtml())) html = convert(bw.getContentHtml());
		switch (bw.getType()) {
		case HTMLPanel:
			w = new HTMLPanel(html);
			break;
		case Button:
			w = new Button(html);
			break;
		case Label:
			w = new Label(html);
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

	@Override
	public HTMLPanel create(BinderWidget w) {
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
