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
package com.gwtmodel.table.panelview;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

public class BinderWidgetSignal extends CustomObjectValue<HTMLPanel> {

	private final BinderWidget bw;

	BinderWidgetSignal(HTMLPanel h, BinderWidget bw) {
		super(h);
		this.bw = bw;
	}

	public BinderWidget getBw() {
		return bw;
	}

	private static final String SIGNAL_ID = BinderWidgetSignal.class.getName() + "TABLE_PUBLIC_GET_BINDER_WIDGET";

	public static CustomStringSlot constructSlotLineErrorSignal(IDataType dType) {
		return new CustomStringDataTypeSlot(dType, SIGNAL_ID);
	}

}
