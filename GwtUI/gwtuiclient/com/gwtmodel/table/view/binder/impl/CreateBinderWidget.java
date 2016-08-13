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
package com.gwtmodel.table.view.binder.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.view.binder.ICreateBinderWidget;
import com.gwtmodel.table.view.binder.ISetWidgetAttribute;
import com.gwtmodel.table.view.util.polymer.PolymerUtil;
import com.vaadin.polymer.iron.widget.IronIcon;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperCheckbox;
import com.vaadin.polymer.paper.widget.PaperDialog;
import com.vaadin.polymer.paper.widget.PaperDialogScrollable;
import com.vaadin.polymer.paper.widget.PaperDrawerPanel;
import com.vaadin.polymer.paper.widget.PaperDropdownMenu;
import com.vaadin.polymer.paper.widget.PaperFab;
import com.vaadin.polymer.paper.widget.PaperHeaderPanel;
import com.vaadin.polymer.paper.widget.PaperIconButton;
import com.vaadin.polymer.paper.widget.PaperIconItem;
import com.vaadin.polymer.paper.widget.PaperInput;
import com.vaadin.polymer.paper.widget.PaperItem;
import com.vaadin.polymer.paper.widget.PaperItemBody;
import com.vaadin.polymer.paper.widget.PaperMaterial;
import com.vaadin.polymer.paper.widget.PaperMenu;
import com.vaadin.polymer.paper.widget.PaperTabs;
import com.vaadin.polymer.paper.widget.PaperTextarea;
import com.vaadin.polymer.paper.widget.PaperToolbar;

public class CreateBinderWidget implements ICreateBinderWidget {

	private final ISetWidgetAttribute iAttr;

	@Inject
	public CreateBinderWidget(ISetWidgetAttribute iAttr) {
		this.iAttr = iAttr;
	}

	private void setWAttribute(Widget w, BinderWidget bw) {
		if (bw.isFieldId())
			Utils.setWidgetAttribute(w, BinderWidget.FIELDID, bw.getFieldId());
		Iterator<String> i = bw.getKeys();
		while (i.hasNext()) {
			String k = i.next();
			String v = bw.getAttr(k);
			iAttr.setAttr(w, k, v);
		}
	}

	private Widget createWidget(BinderWidget bw) {
		Widget w = null;
		String html = "";
		if (!CUtil.EmptyS(bw.getContentHtml()))
			html = PolymerUtil.convert(bw.getContentHtml());
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
		case PaperIconItem:
			w = new PaperIconItem(html);
			break;
		case IronIcon:
			w = new IronIcon(html);
			break;
		case PaperButton:
			w = new PaperButton(html);
			break;
		case PaperHeaderPanel:
			w = new PaperHeaderPanel(html);
			break;
		case PaperToolbar:
			w = new PaperToolbar(html);
			break;
		case Image:
			w = new Image(html);
			break;
		case PaperIconButton:
			w = new PaperIconButton(html);
			break;
		case PaperDrawerPanel:
			w = new PaperDrawerPanel(html);
			break;
		case PaperCheckbox:
			w = new PaperCheckbox(html);
			break;
		case PaperDialog:
			w = new PaperDialog(html);
			break;
		case PaperDialogScrollable:
			w = new PaperDialogScrollable(html);
			break;
		case PaperMenu:
			w = new PaperMenu(html);
			break;
		case PaperDropdownMenu:
			w = new PaperDropdownMenu(html);
			break;
		case PaperTabs:
			w = new PaperTabs(html);
			break;
		case PaperFab:
			w = new PaperFab(html);
			break;
		case PaperItem:
			w = new PaperItem(html);
			break;
		case PaperItemBody:
			w = new PaperItemBody(html);
			break;
		case PaperInput:
			w = new PaperInput(html);
			break;
		case PaperTextarea:
			w = new PaperTextarea(html);
			break;
		case PaperMaterial:
			w = new PaperMaterial(html);
			break;
		default:
			Utils.errAlertB(LogT.getT().PolymerWidgetNotImplemented(bw.getType().name()));
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
		// TODO: debug only
		String elemS = w.toString();
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
