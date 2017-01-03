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
package com.gwtmodel.table.view.binder.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.binder.IAttrName;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.view.binder.ICreateBinderWidget;
import com.gwtmodel.table.view.binder.ISetWidgetAttribute;
import com.gwtmodel.table.view.util.polymer.PolymerUtil;
import com.vaadin.polymer.iron.widget.IronAjax;
import com.vaadin.polymer.iron.widget.IronCollapse;
import com.vaadin.polymer.iron.widget.IronIcon;
import com.vaadin.polymer.iron.widget.IronImage;
import com.vaadin.polymer.iron.widget.IronList;
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
import com.vaadin.polymer.paper.widget.PaperProgress;
import com.vaadin.polymer.paper.widget.PaperRadioButton;
import com.vaadin.polymer.paper.widget.PaperRadioGroup;
import com.vaadin.polymer.paper.widget.PaperRipple;
import com.vaadin.polymer.paper.widget.PaperSlider;
import com.vaadin.polymer.paper.widget.PaperSpinner;
import com.vaadin.polymer.paper.widget.PaperTab;
import com.vaadin.polymer.paper.widget.PaperTabs;
import com.vaadin.polymer.paper.widget.PaperTextarea;
import com.vaadin.polymer.paper.widget.PaperToast;
import com.vaadin.polymer.paper.widget.PaperToggleButton;
import com.vaadin.polymer.paper.widget.PaperToolbar;
import com.vaadin.polymer.paper.widget.PaperTooltip;

public class CreateBinderWidget implements ICreateBinderWidget {

	private final ISetWidgetAttribute iAttr;

	@Inject
	public CreateBinderWidget(ISetWidgetAttribute iAttr) {
		this.iAttr = iAttr;
	}

	private void setWAttribute(Widget w, BinderWidget bw) {
		if (bw.isFieldId())
			Utils.setWidgetAttribute(w, IAttrName.FIELDID, bw.getFieldId());
		Iterator<String> i = bw.getKeys();
		while (i.hasNext()) {
			String k = i.next();
			if (k.equals(IAttrName.FIELDID))
				continue;
			String v = bw.getAttr(k);
			iAttr.setAttr(w, k, v);
		}
	}

	private Widget constructEmptyWidget(BinderWidget bw) {
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
		case HTML:
			w = new HTML(html);
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
		case PaperTab:
			w = new PaperTab(html);
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
		case PaperProgress:
			w = new PaperProgress(html);
			break;
		case PaperRadioButton:
			w = new PaperRadioButton(html);
			break;
		case PaperRadioGroup:
			w = new PaperRadioGroup(html);
			break;
		case PaperRipple:
			w = new PaperRipple(html);
			break;
		case PaperSpinner:
			w = new PaperSpinner(html);
			break;
		case PaperSlider:
			w = new PaperSlider(html);
			break;
		case InlineLabel:
			w = new InlineLabel(html);
			break;
		case FlowPanel:
			if (CUtil.EmptyS(html))
				w = new FlowPanel();
			else
				w = new FlowPanel(html);
			break;
		case PaperTooltip:
			w = new PaperTooltip(html);
			break;
		case PaperToast:
			w = new PaperToast(html);
			break;
		case PaperToggleButton:
			w = new PaperToggleButton(html);
			break;
		case IronAjax:
			w = new IronAjax(html);
			break;
		case IronCollapse:
			w = new IronCollapse(html);
			break;
		case IronImage:
			w = new IronImage(html);
			break;			
		case IronList:
			w = new IronList(html);
			break;
		default:
			Utils.errAlertB(LogT.getT().PolymerWidgetNotImplemented(bw.getType().name()));
			break;
		} // switch

		return w;
	}

	private void buildWidget(Widget w, BinderWidget bw) {
		setWAttribute(w, bw);
		if (bw.getwList().isEmpty())
			return;
		if (!(w instanceof HasWidgets))
			Utils.errAlertB(LogT.getT().BinderCannotHaveWidgets(bw.getType().name()));
		HTMLPanel h;
		HasWidgets hw;
		if (w instanceof HTMLPanel) {
			h = (HTMLPanel) w;
			hw = null;
		} else {
			hw = (HasWidgets) w;
			h = null;
		}

		bw.getwList().forEach(c -> {
			Widget child = constructEmptyWidget(c);
			buildWidget(child, c);
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
		});

	} // for

	@Override
	public HTMLPanel createEmptyHtmlPanel(BinderWidget w) {
		if (w.getwList().isEmpty())
			Utils.errAlertB(LogT.getT().BinderWidgetNoPanels());
		BinderWidget p = w.getwList().get(0);
		Widget ww = constructEmptyWidget(p);
		if (ww instanceof HTMLPanel)
			return (HTMLPanel) ww;
		Utils.errAlert(LogT.getT().BinderNotHTMLPanel(p.getType().name()));
		return null;
	}

	@Override
	public void buildHTMLPanel(HTMLPanel w, BinderWidget bw) {
		buildWidget(w, bw.getwList().get(0));
	}

}
