/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.sampledb.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.ibm.sampledb.shared.GetRowsInfo;
import com.ibm.sampledb.shared.IResourceType;
import com.ibm.sampledb.shared.OneRecord;
import com.ibm.sampledb.shared.RowFieldInfo;

public class SampleGwt implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	// private final SampleServiceAsync sampleService = GWT
	// .create(SampleService.class);

	private class CDraw implements IAction {

		@Override
		public void execute() {
			drawTable();
		}
	}

	private class PrintR implements AsyncCallback<String> {

		@Override
		public void onFailure(Throwable caught) {
			String mess = M.getM().ErrorMessage();
			HTML ha = new HTML(mess);
			dock.drawError(ha);
		}

		@Override
		public void onSuccess(String result) {
			lPrint.launchPrint(result);
		}

	}

	private class APrint implements IAction {

		@Override
		public void execute() {
			List<OneRecord> li = new ArrayList<OneRecord>();
			for (OneRecord e : dock.getrPresentation().getList()) {
				li.add(e);
			}
			M.getSampleService().printList(li, new PrintR());
		}

	}

	private final DockMain dock = new DockMain(new CDraw(), new APrint());

	private final LaunchPrint lPrint = new LaunchPrint();

	class PutResource implements AsyncCallback<GetRowsInfo> {

		@Override
		public void onFailure(Throwable caught) {
			String mess = M.getM().ErrorMessage();
			HTML ha = new HTML(mess);
			dock.drawError(ha);
		}

		@Override
		public void onSuccess(GetRowsInfo result) {
			dock.setResorceInfo(result);
			lPrint.setrInfo(result);
			drawTable();
		}

	}

	class DrawList implements AsyncCallback<List<OneRecord>> {

		@Override
		public void onFailure(Throwable caught) {
			String mess = M.getM().ErrorMessage();
			HTML ha = new HTML(mess);
			dock.drawError(ha);
		}

		@Override
		public void onSuccess(List<OneRecord> result) {
			dock.getrPresentation().drawList(result);
		}

	}

	private void drawTable() {
		ListBox lb = dock.getListBox();
		String orderBy = null;
		int sel = lb.getSelectedIndex();
		String val = lb.getItemText(sel);

		// extract ORDER BY value for sorting
		for (RowFieldInfo f : dock.getrPresentation().getrInfo().getfList()) {
			if (val.equals(f.getfDescr())) {
				orderBy = f.getfId();
			}
		}

		if (orderBy != null) {
			if (!dock.isIncOrder()) {
				orderBy += " DESC";
			}
		}
		lPrint.setOrderBy(orderBy);
		M.getSampleService().getList(orderBy, new DrawList());
	}

	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(dock);
		M.getSampleService().getInfo(IResourceType.EMPLOYEE, new PutResource());
	}

}
