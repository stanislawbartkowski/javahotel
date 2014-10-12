/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;

public class CreateFormView {

	private CreateFormView() {
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
		try {
			// w.getElement().setId(htmlId);
			// ha.add(w, htmlId);
			ha.addAndReplaceElement(w, htmlId);
		} catch (NoSuchElementException e) {
			// expected
		}

	}

	public interface IGetButtons {
		List<ClickButtonType> getDList();

		List<IGFocusWidget> getBList();
	}

	public static void setHtml(HTMLPanel pa, IGetButtons iG) {
		int i = 0;
		for (ClickButtonType c : iG.getDList()) {
			IGFocusWidget b = iG.getBList().get(i++);
			String htmlId = c.getHtmlElementName();
			replace(pa, htmlId, b.getGWidget());
		}
	}

	public static void setHtml(HTMLPanel pa, List<FormField> fList) {
		EditWidgetFactory eFactory = GwtGiniInjector.getI()
				.getEditWidgetFactory();
		IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();
		for (FormField d : fList) {
			String htmlId = d.getELine().getHtmlName();
			if (CUtil.EmptyS(htmlId)) {
				continue;
			}
			Widget w = d.getELine().getGWidget();
			// in case of label always visible
			if (d.isLabel())
				w.setVisible(true);
			replace(pa, htmlId, w);
			if (d.isLabel())
				continue;
			String labelId = IConsts.LABELPREFIX + htmlId;
			if (pa.getElementById(labelId) != null) {
				IFormLineView v = eFactory.constructLabelField(d.getFie(),
						iMess.getMessage(d.getPLabel()));
				replace(pa, labelId, v.getGWidget());
			}

		}
	}

	public static HTMLPanel setHtml(String html, List<FormField> fList) {
		HTMLPanel pa = new HTMLPanel(html);
		setHtml(pa, fList);
		return pa;
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
	public static Grid construct(final List<FormField> fList, Set<IVField> add) {

		IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();
		// number of rows
		int rows = 0;
		for (FormField d : fList) {
			IVField v = d.getFie();
			if (add != null) {
				if (!add.contains(v)) {
					continue;
				}
			}
			if (d.isRange()) {
				continue;
			}
			rows++;
		}
		Grid g = new Grid(rows, 2);
		rows = 0;
		for (FormField d : fList) {
			if (d.isLabel()) {
				g.setWidget(rows, 0, d.getELine().getGWidget());
				rows++;
				continue;
			}
			if (d.isRange()) {
				continue;
			}
			IVField v = d.getFie();
			if (add != null) {
				if (!add.contains(v)) {
					continue;
				}
			}
			String mLabel = iMess.getMessage(d.getPLabel());

			// FormField fRange = null;
			List<FormField> fRange = new ArrayList<FormField>();
			for (FormField fo : fList) {
				if (!fo.isRange()) {
					continue;
				}
				if (fo.getFRange().eq(d.getFie())) {
					fRange.add(fo);
				}
			}
			Label la = new Label(mLabel);
			if (d.getELine().isHidden()) {
				la.setVisible(false);
			}
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

	public static Grid construct(final List<FormField> fList) {
		return construct(fList, null);
	}
}
