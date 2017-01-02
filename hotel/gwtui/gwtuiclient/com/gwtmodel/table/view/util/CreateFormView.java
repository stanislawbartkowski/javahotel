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
package com.gwtmodel.table.view.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.binder.IAttrName;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editw.FormField;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.gwtmodel.table.view.ewidget.IEditWidget;

public class CreateFormView {

	private CreateFormView() {
	}

	private static void replaceId(HTMLPanel ha, String htmlId, Widget w, boolean addId) {
		try {
			if (addId)
				w.getElement().setId(htmlId);
			// ha.add(w, htmlId);
			ha.addAndReplaceElement(w, htmlId);
		} catch (NoSuchElementException e) {
			// expected
		}

	}

	/**
	 * Inserts one position into HTML panel. Do nothing if position not found
	 * 
	 * @param ha
	 *            HTMLpanel
	 * @param htmlId
	 *            Position id inside HTML
	 * @param w
	 *            Widget to insert Important: do nothing if position not found
	 */
	public static void replace(HTMLPanel ha, String htmlId, Widget w) {
		replaceId(ha, htmlId, w, false);
	}

	public interface IGetButtons {
		List<ClickButtonType> getDList();

		List<IGFocusWidget> getBList();
	}

	@FunctionalInterface
	private interface IReplace {
		void replace(String id, Widget w);
	}

	private static void replaceBinder(HasWidgets pa, IReplace iR) {
		Iterator<Widget> i = pa.iterator();
		while (i.hasNext()) {
			Widget w = i.next();
			String id = Utils.getWidgetAttribute(w, IAttrName.FIELDID);
			if (!CUtil.EmptyS(id))
				iR.replace(id, w);
			if (w instanceof HasWidgets)
				replaceBinder((HasWidgets) w, iR);
		}
	}

	public static Map<String, Widget> createListOfFieldsId(HTMLPanel pa) {

		final Map<String, Widget> ma = new HashMap<String, Widget>();
		replaceBinder(pa, (id, w) -> ma.put(id, w));
		return ma;
	}

	public static void setHtml(HTMLPanel pa, final IGetButtons iG, BinderWidget bw) {
		IntStream.range(0, iG.getDList().size()).forEach(i -> {
			ClickButtonType c = iG.getDList().get(i);
			IGFocusWidget b = iG.getBList().get(i);
			String htmlId = c.getHtmlElementName();
			replace(pa, htmlId, b.getGWidget());
		});
		// try to replace widgets
		replaceBinder(pa, (id, w) -> {
			IntStream.range(0, iG.getDList().size()).forEach(i -> {
				ClickButtonType c = iG.getDList().get(i);
				IGFocusWidget b = iG.getBList().get(i);
				if (c.getHtmlElementName().equals(id))
					b.replaceButtonWidget(w);

			});
		});
	}

	public static void setHtml(HTMLPanel pa, final List<FormField> fList, BinderWidget bw) {
		// TODO: debug only
		String h = pa.toString();
		IEditWidget eFactory = EditWidgetFactory.getGwtE();
		IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();
		IGetCustomValues c = GwtGiniInjector.getI().getCustomValues();
		boolean addId = CUtil.EqNS(c.getCustomValue(IGetCustomValues.HTMLPANELADDID), IGetCustomValues.VALUEYES);

		for (FormField d : fList) {
			String htmlId = d.getELine().getHtmlName();
			if (CUtil.EmptyS(htmlId)) {
				continue;
			}
			Widget w = d.getELine().getGWidget();
			// in case of label always visible
			if (d.getFormProp().isLabel())
				w.setVisible(true);

			replaceId(pa, htmlId, w, addId);
			if (d.getFormProp().isLabel())
				continue;

			String labelId = IConsts.LABELPREFIX + htmlId;
			if (pa.getElementById(labelId) != null) {
				IFormLineView v = eFactory.constructLabelField(d.getFie(), null, iMess.getMessage(d.getPLabel()));
				replace(pa, labelId, v.getGWidget());
			}

			String labelFor = IConsts.LABELFORPREFIX + htmlId;
			if (pa.getElementById(labelFor) != null) {
				IFormLineView v = eFactory.constructLabelFor(d.getFie(), null, iMess.getMessage(d.getPLabel()));
				replace(pa, labelFor, v.getGWidget());
			}

		}

		// try to replace widgets
		replaceBinder(pa, (id, w) -> {
			fList.stream().filter(f -> f.getELine().getV().getId().equals(id))
					.forEach(f -> f.getELine().replaceWidget(w));
		});

	}

	/**
	 * Creates widget grid for list of form (enter) fields
	 * 
	 * @param fList
	 *            List of fields
	 * @param add
	 *            if not null contains set of fields for add (ignore the others)
	 * @return Grid created
	 */
	public static Grid construct(final List<FormField> fList) {

		IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();
		// number of rows
		int rows = 0;
		for (FormField d : fList) {
			if (d.isRange())
				continue;
			rows++;
		}
		Grid g = new Grid(rows, 2);
		rows = 0;
		for (FormField d : fList) {
			if (d.getFormProp().isLabel()) {
				g.setWidget(rows, 0, d.getELine().getGWidget());
				Element e = g.getCellFormatter().getElement(rows, 0);
				e.setAttribute("colspan", "2");
				rows++;
				continue;
			}
			if (d.isRange())
				continue;
			String mLabel = iMess.getMessage(d.getPLabel());

			// FormField fRange = null;
			List<FormField> fRange = new ArrayList<FormField>();
			for (FormField fo : fList) {
				if (!fo.isRange())
					continue;
				if (fo.getFRange().eq(d.getFie()))
					fRange.add(fo);
			}
			Label la = new Label(mLabel);
			if (d.getFormProp().isHidden())
				la.setVisible(false);
			Widget w1;
			Widget w2;
			if (fRange.isEmpty()) {
				w1 = la;
				w2 = d.getELine().getGWidget();
			} else {
				HorizontalPanel vp1 = new HorizontalPanel();
				HorizontalPanel vp2 = new HorizontalPanel();
				vp1.add(la);
				vp2.add(d.getELine().getGWidget());
				w1 = vp1;
				w2 = vp2;
				for (FormField f : fRange) {
					if (f.getPLabel() != null) {
						String rLabel = iMess.getMessage(f.getPLabel());
						Label la2 = new Label(rLabel);
						vp2.add(la2);
					}
					vp2.add(f.getELine().getGWidget());
				}
			}
			g.setWidget(rows, 0, w1);
			g.setWidget(rows, 1, w2);
			rows++;
		}
		return g;
	}

}
