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
package com.gwtmodel.table.view.table;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Header;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.tabledef.VFooterDesc;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.edit.IPresentationCellEdit;
import com.gwtmodel.table.view.table.util.PresentationCellHelper;

/**
 * @author hotel
 * 
 */
class PresentationFooterFactory extends PresentationCellHelper {

	@SuppressWarnings("unused")
	private final PresentationCellFactory cFactory;
	private IVModelData footerV;
	private final IPresentationCellEdit faEdit;

	private IGetStandardMessage iMess = GwtGiniInjector.getI()
			.getStandardMessage();

	PresentationFooterFactory(PresentationCellFactory cFactory,
			IPresentationCellEdit faEdit) {
		this.cFactory = cFactory;
		this.faEdit = faEdit;
	}

	// center, left, right
	interface InputTemplate extends SafeHtmlTemplates {

		@SafeHtmlTemplates.Template("<p style=\"text-align:{0};\">{1}</p>")
		SafeHtml input(String align, String value);
	}

	private InputTemplate headerInput = GWT.create(InputTemplate.class);

	private SafeHtml getHtml(VListHeaderDesc.ColAlign align,
			FieldDataType dType, String value) {
		String ali = null;
		switch (AlignCol.getCo(align, dType)) {
		case LEFT:
			ali = "left";
			break;
		case CENTER:
			ali = "center";
			break;
		case RIGHT:
			ali = "right";
			break;
		}
		return headerInput.input(ali, value == null ? "" : value);
	}

	private class FooterH extends Header<SafeHtml> {

		private final VFooterDesc he;

		FooterH(VFooterDesc he) {
			super(new SafeHtmlCell());
			this.he = he;
		}

		@Override
		public SafeHtml getValue() {
			if (footerV == null) {
				return null;
			}
			Object o = footerV.getF(he.getFie());
			String val;
			if (he.getfType().getType() == TT.BIGDECIMAL) {
				if (o == null)
					val = "";
				else {
					BigDecimal b = (BigDecimal) o;
					val = NumberFormat.getFormat(
							getNumberFormat(he.getfType().getAfterdot()))
							.format(b);
				}
			} else
				val = FUtils.getValueS(o, he.getfType().getType(), he
						.getfType().getAfterdot());

			return getHtml(he.getAlign(), he.getfType(), val);
		}

	}

	private class MySafeHtmlHeader extends Header<SafeHtml> {

		private final VListHeaderDesc he;

		/**
		 * Construct a Header with a given {@link SafeHtml} text value.
		 *
		 * @param text
		 *            the header text, as safe HTML
		 */
		public MySafeHtmlHeader(VListHeaderDesc he) {
			super(new SafeHtmlCell());
			this.he = he;
		}

		/**
		 * Return the {@link SafeHtml} text value.
		 */
		@Override
		public SafeHtml getValue() {
			return getHtml(he.getAlign(), he.getFie().getType(),
					iMess.getMessage(he.getHeaderString()));
		}
	}

	private class C extends AbstractCell<SafeHtml> {

		private final VListHeaderDesc he;
		private WSize w = null;

		C(VListHeaderDesc he) {
			super(BrowserEvents.CLICK);
			this.he = he;
		}

		@Override
		public void onBrowserEvent(Context context, Element parent,
				SafeHtml value, NativeEvent event,
				ValueUpdater<SafeHtml> valueUpdater) {
			w = new WSize(parent.getAbsoluteTop(), parent.getAbsoluteLeft(),
					parent.getAbsoluteBottom() - parent.getAbsoluteTop(),
					parent.getAbsoluteRight() - parent.getAbsoluteLeft());
			if (BrowserEvents.CLICK.equals(event.getType())) {
				if (faEdit.geteParam().geteList() == null)
					return;
				if (!faEdit.geteParam().isEditable())
					return;
				List<IVField> eList = faEdit.geteParam().geteList();
				for (IVField v : eList)
					if (v.eq(he.getFie())) {
						model.getBClicked().clicked(he.getFie(), w);
						break;
					}
			}
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				SafeHtml value, SafeHtmlBuilder sb) {
			if (value != null)
				sb.append(value);
		}

	}

	private class BoolActionHeader extends Header<SafeHtml> {

		private final VListHeaderDesc he;

		BoolActionHeader(VListHeaderDesc he) {
			super(new C(he));
			this.he = he;
		}

		@Override
		public SafeHtml getValue() {
			return getHtml(he.getAlign(), he.getFie().getType(),
					iMess.getMessage(he.getHeaderString()));
		}

	}

	Header<?> constructHeader(VListHeaderDesc he, boolean editable) {
		if (he.getFie().getType().getType() == TT.BOOLEAN && editable) {
			return new BoolActionHeader(he);
		}
		return new MySafeHtmlHeader(he);
	}

	Header<?> constructFooter(VFooterDesc he) {
		return new FooterH(he);
	}

	/**
	 * @param footerV
	 *            the footerV to set
	 */
	void setFooterV(IVModelData footerV) {
		this.footerV = footerV;
	}

}
