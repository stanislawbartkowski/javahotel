/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.tabledef.VFooterDesc;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.util.PresentationCellHelper;

/**
 * @author hotel
 * 
 */
class PresentationFooterFactory extends PresentationCellHelper {

	@SuppressWarnings("unused")
	private final PresentationCellFactory cFactory;
	private IVModelData footerV;
	private IGetStandardMessage iMess = GwtGiniInjector.getI()
			.getStandardMessage();

	PresentationFooterFactory(PresentationCellFactory cFactory) {
		this.cFactory = cFactory;
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

	Header<?> constructHeader(VListHeaderDesc he) {
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
