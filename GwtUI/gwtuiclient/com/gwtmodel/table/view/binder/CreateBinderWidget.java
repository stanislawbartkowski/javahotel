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
import com.google.gwt.user.client.ui.Image;
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
import com.vaadin.polymer.PolymerWidget;
import com.vaadin.polymer.iron.widget.IronIcon;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperDrawerPanel;
import com.vaadin.polymer.paper.widget.PaperHeaderPanel;
import com.vaadin.polymer.paper.widget.PaperIconButton;
import com.vaadin.polymer.paper.widget.PaperIconItem;
import com.vaadin.polymer.paper.widget.PaperToolbar;

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
			b.replace(i, k + 2, v);
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
			else if (k.equals(IConsts.ATTRADDSTYLENAMES)) {
				String sa[] = v.split(" ");
				for (String a : sa)
					w.addStyleName(a);
			}

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
			else if (k.equals(IConsts.ATTRHTML))
				w.setHTML(v);
		}

	};

	private static final IVisitor<PolymerWidget> polymerWidgetG = new IVisitor<PolymerWidget>() {

		@Override
		public void visit(PolymerWidget w, String k, String v) {
			if (k.equals(IConsts.ATTRATRIBUTES))
				w.setAttributes(v);
			else if (k.equals(IConsts.ATTRBOOLEANATTRIBUTE)) {
				String va[] = v.split(" ");
				for (String iv : va) {
					String[] bv = iv.split(":");
					w.setBooleanAttribute(bv[0], Utils.toB(bv[1]));
				}
			} else if (k.equals(IConsts.ATTRDISABLED))
				w.setDisabled(Utils.toB(v));
			else if (k.equals(IConsts.ATTRID))
				w.setId(v);
			else if (k.equals(IConsts.ATTRNAME))
				w.setName(v);
			else if (k.equals(IConsts.ATTRNOINK))
				w.setNoink(Utils.toB(v));
			else if (k.equals(IConsts.ATTRSTYLE))
				w.setStyle(v);
			else if (k.equals(IConsts.ATTRTABINDEX))
				w.setTabindex(CUtil.getInteger(v));
			else if (k.equals(IConsts.ATTRCLASS))
				w.setClass(v);
		}

	};

	private static final IVisitor<PaperIconItem> paperIconItemG = new IVisitor<PaperIconItem>() {

		@Override
		public void visit(PaperIconItem w, String k, String v) {
			if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(Utils.toB(v));
			else if (k.equals(IConsts.ATTRPOINTERDOWN))
				w.setPointerDown(Utils.toB(v));
			else if (k.equals(IConsts.ATTRPRESSED))
				w.setPressed(Utils.toB(v));
			else if (k.equals(IConsts.ATTRTOGGLES))
				w.setToggles(Utils.toB(v));
		}

	};

	private static final IVisitor<IronIcon> ironIconG = new IVisitor<IronIcon>() {

		@Override
		public void visit(IronIcon w, String k, String v) {
			if (k.equals(IConsts.ATTRICON))
				w.setIcon(v);
			else if (k.equals(IConsts.ATTRSRC))
				w.setSrc(v);
			else if (k.equals(IConsts.ATTRTHEME))
				w.setTheme(v);
		}

	};

	private static final IVisitor<PaperButton> paperbuttonG = new IVisitor<PaperButton>() {

		@Override
		public void visit(PaperButton w, String k, String v) {
			if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(Utils.toB(v));
			else if (k.equals(IConsts.ATTRPOINTERDOWN))
				w.setPointerDown(Utils.toB(v));
			else if (k.equals(IConsts.ATTRPRESSED))
				w.setPressed(Utils.toB(v));
			else if (k.equals(IConsts.ATTRTOGGLES))
				w.setToggles(Utils.toB(v));
			else if (k.equals(IConsts.ATTRACTIVE))
				w.setActive(Utils.toB(v));
			else if (k.equals(IConsts.ATTRRAISED))
				w.setRaised(Utils.toB(v));
			else if (k.equals(IConsts.ATTRELEVATION))
				w.setElevation(v);
		}

	};

	private static final IVisitor<PaperHeaderPanel> paperHeaderG = new IVisitor<PaperHeaderPanel>() {

		@Override
		public void visit(PaperHeaderPanel w, String k, String v) {
			if (k.equals(IConsts.ATTRATTOP))
				w.setAtTop(Utils.toB(v));
			else if (k.equals(IConsts.ATTRMODE))
				w.setMode(v);
			else if (k.equals(IConsts.ATTRSHADOW))
				w.setMode(v);
			else if (k.equals(IConsts.ATTRTALLCLASS))
				w.setTallClass(v);
		}
	};

	private static final IVisitor<PaperToolbar> paperToolbarG = new IVisitor<PaperToolbar>() {

		@Override
		public void visit(PaperToolbar w, String k, String v) {
			if (k.equals(IConsts.ATTRBOTTOMJUSTIFY))
				w.setBottomJustify(v);
			else if (k.equals(IConsts.ATTRJUSTIFY))
				w.setJustify(v);
			else if (k.equals(IConsts.ATTRMIDDLEJUSTIFY))
				w.setMiddleJustify(v);
		}
	};

	private static final IVisitor<Image> imageG = new IVisitor<Image>() {

		@Override
		public void visit(Image w, String k, String v) {
			if (k.equals(IConsts.ATTRURL))
				w.setUrl(v);
			else if (k.equals(IConsts.ATTRALTTEXT))
				w.setAltText(v);
		}

	};

	private static final IVisitor<PaperIconButton> papericonG = new IVisitor<PaperIconButton>() {

		@Override
		public void visit(PaperIconButton w, String k, String v) {
			if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(Utils.toB(v));
			else if (k.equals(IConsts.ATTRPOINTERDOWN))
				w.setPointerDown(Utils.toB(v));
			else if (k.equals(IConsts.ATTRPRESSED))
				w.setPressed(Utils.toB(v));
			else if (k.equals(IConsts.ATTRTOGGLES))
				w.setToggles(Utils.toB(v));
			else if (k.equals(IConsts.ATTRICON))
				w.setIcon(v);
			else if (k.equals(IConsts.ATTRSRC))
				w.setSrc(v);

		}
	};

	private static final IVisitor<PaperDrawerPanel> paperdrawerpanelG = new IVisitor<PaperDrawerPanel>() {

		@Override
		public void visit(PaperDrawerPanel w, String k, String v) {
			if (k.equals(IConsts.ATTRDEFAULTSELECTED))
				w.setDefaultSelected(v);
			else if (k.equals(IConsts.ATTRDISABLEEDGESWIPE))
				w.setDisableEdgeSwipe(Utils.toB(v));
			else if (k.equals(IConsts.ATTRDRAGGING))
				w.setDragging(Utils.toB(v));
			else if (k.equals(IConsts.ATTRDRAWERTOGGLEATTRIBUTE))
				w.setDrawerToggleAttribute(v);
			else if (k.equals(IConsts.ATTRDRAWEWIDTH))
				w.setDrawerWidth(v);
			else if (k.equals(IConsts.ATTREDGESWUPESENSITIVITY))
				w.setEdgeSwipeSensitivity(Double.parseDouble(v));
			else if (k.equals(IConsts.ATTREDGESWUPESENSITIVITYPIXELS))
				w.setEdgeSwipeSensitivity(v);
			else if (k.equals(IConsts.ATTRFORCENARROW))
				w.setForceNarrow(Utils.toB(v));
			else if (k.equals(IConsts.ATTRHASTRANSFORM))
				w.setHasTransform(Utils.toB(v));
			else if (k.equals(IConsts.ATTRHADWILLCHANGE))
				w.setHasWillChange(Utils.toB(v));
			else if (k.equals(IConsts.ATTRNARROW))
				w.setNarrow(Utils.toB(v));
			else if (k.equals(IConsts.ATTRPEEKING))
				w.setPeeking(Utils.toB(v));
			else if (k.equals(IConsts.ATTRRESPONSICEWIDTH))
				w.setResponsiveWidth(v);
			else if (k.equals(IConsts.ATTRRIGHTDRAWER))
				w.setRightDrawer(Utils.toB(v));
			else if (k.equals(IConsts.ATTRSELECTED))
				w.setSelected(v);

		}
	};

	private final static Map<WidgetTypes, IVisitor<Widget>[]> setAWidget = new HashMap<WidgetTypes, IVisitor<Widget>[]>();

	static {
		setAWidget.put(WidgetTypes.Button, new IVisitor[] { argF, argbaseB });
		setAWidget.put(WidgetTypes.Label, new IVisitor[] { argL });
		setAWidget.put(WidgetTypes.HTMLPanel, new IVisitor[] {});
		setAWidget.put(WidgetTypes.PaperIconItem, new IVisitor[] { polymerWidgetG, paperIconItemG });
		setAWidget.put(WidgetTypes.IronIcon, new IVisitor[] { polymerWidgetG, ironIconG });
		setAWidget.put(WidgetTypes.PaperButton, new IVisitor[] { polymerWidgetG, paperbuttonG });
		setAWidget.put(WidgetTypes.PaperHeaderPanel, new IVisitor[] { polymerWidgetG, paperHeaderG });
		setAWidget.put(WidgetTypes.PaperToolBar, new IVisitor[] { polymerWidgetG, paperToolbarG });
		setAWidget.put(WidgetTypes.Image, new IVisitor[] { imageG });
		setAWidget.put(WidgetTypes.PaperIconButton, new IVisitor[] { polymerWidgetG, papericonG });
		setAWidget.put(WidgetTypes.PaperDrawerPanel, new IVisitor[] { polymerWidgetG, paperdrawerpanelG });
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

	private void setWAttribute(Widget w, BinderWidget bw) {
		if (bw.isFieldId())
			Utils.setWidgetAttribute(w, BinderWidget.FIELDID, bw.getFieldId());
		setAttr(w, bw, setAWidget.get(bw.getType()));
	}

	private Widget createWidget(BinderWidget bw) {
		Widget w = null;
		String html = "";
		if (!CUtil.EmptyS(bw.getContentHtml()))
			html = convert(bw.getContentHtml());
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
		case PaperToolBar:
			w = new PaperToolbar(html);
			break;
		case Image:
			w = new Image(html);
			break;
		case PaperIconButton:
			w = new PaperIconButton(html);
			break;
		case PaperDrawerPanel:
			w = new PaperDrawerPanel();
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
