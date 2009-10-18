/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.view.gwt.record.view;

import java.util.Set;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.ContrButtonViewFactory;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.record.view.IAuxRecordPanel;
import com.javahotel.client.mvc.record.view.helper.IInitDialog;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class VRecordView extends AbstractRecordView implements IInitDialog {

	private Widget vP;
	private final IContrPanel contr;
	private final IControlClick co;
	private final IAuxRecordPanel auxV;
	private final ISetGwtWidget iSet;

	VRecordView(final IResLocator rI, ISetGwtWidget iSet, final DictData da,
			final IRecordDef model, final IContrPanel contr,
			final IControlClick co, final IAuxRecordPanel auxV) {
		super(rI, model);
		this.iSet = iSet;
		this.contr = contr;
		this.co = co;
		this.auxV = auxV;
	}

    protected void createWDialog(final VerticalPanel iPanel,
			final Set<IField> filter) {

		int rows = 0;
		for (RecordField d : model.getFields()) {
			if (filter != null) {
				if (!filter.contains(d.getFie())) {
					continue;
				}
			}
			rows++;
		}
		Grid g = new Grid(rows, 2);
		rows = 0;
		for (RecordField d : model.getFields()) {
			if (filter != null) {
				if (!filter.contains(d.getFie())) {
					continue;
				}
			}
			Label la = new Label(d.getPLabel());
			g.setWidget(rows, 0, la);
			g.setWidget(rows, 1, d.getELine().getMWidget().getWidget());
			d.getELine().setKLi(new DListener());
			// eStore.saveELine(d, g, rows);
			rows++;
		}
		iPanel.add(g);

	}

	public void initW(final IPanel vP, final Widget iW) {
		Widget iWidget;
		if (iW == null) {
			VerticalPanel iPanel = new VerticalPanel();
			createWDialog(iPanel, null);
			iWidget = iPanel;
		} else {
			iWidget = iW;
		}

		if (auxV != null) {
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(iWidget);
			hp.add(auxV.getMWidget().getWidget());
			vP.add(hp);
		} else {
			vP.add(iWidget);
		}

		if (contr != null) {
			IContrButtonView i = ContrButtonViewFactory.getView(rI, contr, co);
			vP.add(i.getMWidget().getWidget());
		}

		this.vP = vP.getPanel();
		iSet.setGwtWidget(new DefaultMvcWidget(this.vP));

	}

	public IAuxRecordPanel getAuxV() {
		return auxV;
	}

	public boolean isExt() {
		return false;
	}

	public void addEmptyList() {
	}

}
