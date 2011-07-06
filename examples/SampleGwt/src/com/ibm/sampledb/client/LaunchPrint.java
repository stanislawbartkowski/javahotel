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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.ibm.sampledb.shared.GetRowsInfo;
import com.ibm.sampledb.shared.ResourceInfo;

public class LaunchPrint {

	private GetRowsInfo rInfo;
	private String orderBy = null;

	private final Message message = GWT.create(Message.class);

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setrInfo(GetRowsInfo rInfo) {
		this.rInfo = rInfo;
	}

	void launchPrint(String fileName) {
		// create an URL string
		StringBuffer b = new StringBuffer(rInfo.getResource().getBirtURL());
		b.append("?__report=");
		b.append(rInfo.getResource().getEmployeeReport());
		b.append("&FILEXML=");
		b.append(fileName);
		String s;
		if (orderBy == null) {
			s = " " + message.NoOrder();
		} else {
			s = orderBy;
		}
		b.append("&ORDERBY=");
		b.append(s);
		// Window.alert(b.toString());
		Window.open(b.toString(), message.Print(), null);
	}

}
