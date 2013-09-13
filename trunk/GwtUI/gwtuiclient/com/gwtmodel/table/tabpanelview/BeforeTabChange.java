package com.gwtmodel.table.tabpanelview;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.listdataview.DataIntegerSignal;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

public class BeforeTabChange extends DataIntegerSignal {

	private final String tabId;

	BeforeTabChange(int tab, String tabId) {
		super(tab);
		this.tabId = tabId;
	}

	private static final String SIGNAL_ID = "TABLE_PUBLIC_BEFORE_TAB_CHANGE"
			+ BeforeTabChange.class.getName();

	public static CustomStringSlot constructBeforeTabChangeSignal(
			IDataType dType) {
		return new CustomStringDataTypeSlot(dType, SIGNAL_ID);
	}

	public String getTabId() {
		return tabId;
	}

}