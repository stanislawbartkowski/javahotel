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
package com.polymerui.client.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.jythonui.client.M;
import com.polymerui.client.view.panel.IMainPanel;

/**
 * Common AsyncCallBack for all async service Handles OnFailure
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
public abstract class CommonCallBack<T> implements AsyncCallback<T> {

	/**
	 * OnSuccess handler
	 * 
	 * @param arg
	 *            Value returned
	 */
	abstract public void onMySuccess(final T arg);

	final private IMainPanel iWeb;
	final private onFailureExt ext;

	/**
	 * Customer handler for onFailure (if needed)
	 * 
	 * @author hotel
	 * 
	 */
	public interface onFailureExt {

		/**
		 * Custom action on failure
		 * 
		 * @param ext
		 *            Throwable causing failure
		 * @return true: run standard failure handler, false: do not run
		 * 
		 */
		boolean doSth(Throwable ext);
	}

	/**
	 * Constructor
	 */
	protected CommonCallBack() {
		iWeb = M.getPanel();
		iWeb.IncDecCounter(true);
		ext = null;
	}

	// default visibility on purpose
	void incC(final int no) {
		for (int i = 0; i < no; i++) {
			iWeb.IncDecCounter(true);
		}
	}

	/**
	 * Constructor with custom failure handler
	 * 
	 * @param e
	 *            Custom failure handler
	 */
	public CommonCallBack(final onFailureExt e) {
		iWeb = M.getPanel();
		iWeb.IncDecCounter(true);
		ext = e;
	}

	@Override
	public void onSuccess(final T arg) {
		iWeb.IncDecCounter(false);
		onMySuccess(arg);
	}

	@Override
	public void onFailure(final Throwable caught) {
		iWeb.IncDecCounter(false);
		if (ext != null) {
			if (ext.doSth(caught)) {
				return;
			}
		}
		String cMessage = "";
		if (caught.getMessage() != null) {
			cMessage += " " + caught.getMessage();
		}
		iWeb.setErrorL(cMessage);
	}
}
