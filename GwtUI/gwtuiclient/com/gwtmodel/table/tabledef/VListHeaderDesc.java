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
package com.gwtmodel.table.tabledef;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.LogT;

public class VListHeaderDesc {

	public enum ColAlign {

		LEFT, CENTER, RIGHT
	}

	private String headerString;
	private final IVField fie;
	private boolean hidden;
	private final String buttonAction;
	private final IGHeader gHeader;
	private final boolean editable;
	private final ColAlign align;
	private final String colWidth;
	private final String inputClass;
	private final String inputStyle;
	private final IColumnImageSelect iColSelect;
	private final int imageNo;
	private final String columnClass;
	private final String headerClass;
	private final IGetSpinnerRange iSpinner;

	public VListHeaderDesc(IGHeader gHeader, IVField fie) {
		assert fie != null : LogT.getT().cannotBeNull();
		this.headerString = null;
		this.fie = fie;
		this.hidden = false;
		this.buttonAction = null;
		this.gHeader = gHeader;
		this.editable = false;
		this.align = null;
		this.colWidth = null;
		this.inputClass = null;
		this.inputStyle = null;
		this.iColSelect = null;
		this.imageNo = 0;
		this.columnClass = null;
		this.headerClass = null;
		this.iSpinner = null;
	}

	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @return the gCell
	 */
	public IGHeader getgHeader() {
		return gHeader;
	}

	public VListHeaderDesc(String headerString, IVField fie) {
		this(headerString, fie, false, null, false, null, null, null, null,
				null, 0, null, null, null);
	}

	public VListHeaderDesc(IVField fie, VListHeaderDesc v) {
		this(v.getHeaderString(), fie, v.isHidden(), v.getButtonAction(),
				false, null, null, null, null, null, 0, null, null, null);
	}

	public VListHeaderDesc(String headerString, IVField fie, boolean hidden,
			String buttonAction, boolean editable, ColAlign align,
			String colWidth, String inputClass, String inputStyle,
			IColumnImageSelect iColSelect, int imageNo, String columnClass,
			String headerClass, IGetSpinnerRange iSpinner) {
		assert fie != null : LogT.getT().cannotBeNull();
		this.headerString = headerString;
		this.fie = fie;
		this.hidden = hidden;
		this.buttonAction = buttonAction;
		this.gHeader = null;
		this.editable = editable;
		this.align = align;
		this.colWidth = colWidth;
		this.inputClass = inputClass;
		this.inputStyle = inputStyle;
		this.iColSelect = iColSelect;
		this.imageNo = imageNo;
		this.columnClass = columnClass;
		this.headerClass = headerClass;
		this.iSpinner = iSpinner;
	}

	public VListHeaderDesc(String headerString, IVField fie, boolean hidden,
			String buttonAction, boolean editable, ColAlign align,
			String colWidth) {
		this(headerString, fie, hidden, buttonAction, editable, align,
				colWidth, null, null, null, 0, null, null, null);
	}

	public VListHeaderDesc(String headerString, IVField fie, boolean hidden) {
		this(headerString, fie, hidden, null, false, null, null, null, null,
				null, 0, null, null, null);
	}

	public String getHeaderClass() {
		return headerClass;
	}

	public String getHeaderString() {
		return headerString;
	}

	public IVField getFie() {
		return fie;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @return the buttonAction
	 */
	public String getButtonAction() {
		return buttonAction;
	}

	/**
	 * @return the align
	 */
	public ColAlign getAlign() {
		return align;
	}

	/**
	 * @return the colWidth
	 */
	public String getColWidth() {
		return colWidth;
	}

	/**
	 * @return the iColSelect
	 */
	public IColumnImageSelect getiColSelect() {
		return iColSelect;
	}

	/**
	 * @return the imageNo
	 */
	public int getImageNo() {
		return imageNo;
	}

	public boolean isImageCol() {
		return imageNo > 0;
	}

	public String getColumnClass() {
		return columnClass;
	}

	/**
	 * @return the inputClass
	 */
	public String getInputClass() {
		return inputClass;
	}

	/**
	 * @return the inputStyle
	 */
	public String getInputStyle() {
		return inputStyle;
	}

	public IGetSpinnerRange getiSpinner() {
		return iSpinner;
	}

	public void setHeaderString(String headerString) {
		this.headerString = headerString;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
