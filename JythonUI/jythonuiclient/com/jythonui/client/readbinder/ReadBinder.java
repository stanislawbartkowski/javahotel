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
package com.jythonui.client.readbinder;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmodel.table.ISetSynchData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.binder.BinderWidget;
import com.jythonui.client.M;

public class ReadBinder {

	private ReadBinder() {
	}

	public static void readBinder(final ISetSynchData<BinderWidget> i, final String fileName) {
		M.JR().readBinderWidget(fileName, new AsyncCallback<BinderWidget>() {

			@Override
			public void onFailure(Throwable caught) {
				Utils.errAlert(M.M().CannotReadBinderFile(fileName));
			}

			@Override
			public void onSuccess(BinderWidget result) {
				i.set(result);
			}

		});
	}

}
