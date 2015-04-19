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
package com.gwtmodel.table.view.table.util;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.factories.IWebPanelResources;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.table.IGwtTableModel;

/**
 * @author hotel
 * 
 */
public abstract class PresentationCellHelper {

	protected IGwtTableModel model = null;
	protected final IGetCustomValues cValues = GwtGiniInjector.getI()
			.getCustomValues();
	protected final IWebPanelResources pResources = GwtGiniInjector.getI()
			.getWebPanelResources();
	protected final DateTimeFormat fo = DateTimeFormat.getFormat(cValues
			.getCustomValue(IGetCustomValues.DATEFORMAT));
	protected static final SafeHtml INPUT_CHECKED = SafeHtmlUtils
			.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" disabled=\"disabled\" checked/>");
	protected static final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils
			.fromSafeConstant("<input type=\"checkbox\" disabled=\"disabled\" tabindex=\"-1\"/>");
	protected final NumberCell iCell = new NumberCell(
			NumberFormat.getFormat(getNumberFormat(0)));
	protected final NumberCell nCell1 = new NumberCell(
			NumberFormat.getFormat(getNumberFormat(1)));
	protected final NumberCell nCell2 = new NumberCell(
			NumberFormat.getFormat(getNumberFormat(2)));
	protected final NumberCell nCell3 = new NumberCell(
			NumberFormat.getFormat(getNumberFormat(3)));
	protected final NumberCell nCell4 = new NumberCell(
			NumberFormat.getFormat(getNumberFormat(4)));

	public void setGModel(IGwtTableModel model) {
		this.model = model;
	}

	protected String getS(String value) {
		return value == null ? "" : value;
	}

	protected class CheckBoxColumn extends Column<MutableInteger, Boolean> {

		private final IVField iF;

		public CheckBoxColumn(Cell<Boolean> cell, IVField iF) {
			super(cell);
			this.iF = iF;
		}

		@Override
		public Boolean getValue(MutableInteger object) {
			IVModelData vData = model.get(object.intValue());
			Boolean b = FUtils.getValueBoolean(vData, iF);
			return b;
		}
	}

	protected class DateColumn extends Column<MutableInteger, Date> {

		private final IVField v;

		public DateColumn(IVField v, Cell<Date> ce) {
			super(ce);
			this.v = v;
		}

		@Override
		public Date getValue(MutableInteger object) {
			IVModelData vData = model.get(object.intValue());
			return FUtils.getValueDate(vData, v);
		}
	}

	protected String getNumberFormat(int afterdot) {
		String defaFormat;
		String parName;
		switch (afterdot) {
		case 0:
			defaFormat = "####0";
			parName = IGetCustomValues.NUMBERFORMAT0;
			break;
		case 1:
			defaFormat = "##########0.0";
			parName = IGetCustomValues.NUMBERFORMAT1;
			break;
		case 2:
			defaFormat = "##########0.00";
			parName = IGetCustomValues.NUMBERFORMAT2;
			break;
		case 3:
			defaFormat = "##########0.000";
			parName = IGetCustomValues.NUMBERFORMAT3;
			break;
		default:
			defaFormat = "##########0.0000";
			parName = IGetCustomValues.NUMBERFORMAT4;
			break;
		}
		String forma = cValues.getCustomValue(parName);
		if (forma == null) {
			forma = defaFormat;
		}
		return forma;
	}

	protected class NumberColumn extends Column<MutableInteger, Number> {

		protected final IVField v;

		public NumberColumn(Cell<Number> n, IVField v) {
			super(n);
			this.v = v;
		}

		@Override
		public Number getValue(MutableInteger object) {
			IVModelData vData = model.get(object.intValue());
			BigDecimal b = FUtils.getValueBigDecimal(vData, v);
			return b;
		}
	}

	protected class GLongColumn extends Column<MutableInteger, Number> {

		private final IVField v;

		GLongColumn(Cell<Number> c, IVField v) {
			super(c);
			this.v = v;
		}

		@Override
		public Long getValue(MutableInteger object) {
			IVModelData vData = model.get(object.intValue());
			return FUtils.getValueLong(vData, v);
		}
	}

	protected class LongColumn extends GLongColumn {

		public LongColumn(IVField v) {
			super(iCell, v);
		}
	}

	protected class IntegerColumn extends GIntegerColumn {

		public IntegerColumn(IVField v) {
			super(iCell, v);
		}

	}

	protected class GIntegerColumn extends Column<MutableInteger, Number> {

		private final IVField v;

		GIntegerColumn(Cell<Number> c, IVField v) {
			super(c);
			this.v = v;
		}

		@Override
		public Number getValue(MutableInteger object) {
			IVModelData vData = model.get(object.intValue());
			Integer val = FUtils.getValueInteger(vData, v);
			if (val == null)
				return null;
			return new BigDecimal(val);
		}
	}

}
