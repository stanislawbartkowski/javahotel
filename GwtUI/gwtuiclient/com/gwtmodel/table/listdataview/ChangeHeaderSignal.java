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
package com.gwtmodel.table.listdataview;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.tabledef.VListHeaderDesc;

public class ChangeHeaderSignal extends CustomObjectValue<VListHeaderDesc> {

	private final ChangeType cType;

	public enum ChangeType {
		HEADER, VISIBILITY
	}

	public ChangeHeaderSignal(String value, IVField v) {
		super(new VListHeaderDesc(value, v));
		cType = ChangeType.HEADER;
	}

	public ChangeHeaderSignal(boolean vis, IVField v) {
		super(new VListHeaderDesc(null, v, !vis));
		cType = ChangeType.VISIBILITY;
	}

	private static final String SIGNAL_ID = SetSortColumnSignal.class.getName()
			+ "TABLE_PUBLIC_CHANGE_HEADER_STRING";

	public static CustomStringSlot constructSlotChangeHeaderSignal(
			IDataType dType) {
		return new CustomStringDataTypeSlot(dType, SIGNAL_ID);
	}

	public ChangeType getcType() {
		return cType;
	}

}
