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
package com.ibm.mystreams.client.util;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ibm.mystreams.client.M;
import com.ibm.mystreams.shared.RestRes;

/**
 * 
 * @author sbartkowski
 * 
 *         Common abstract for callback function, displays and removes progress
 *         icon
 */

abstract public class CommonCallBack implements AsyncCallback<RestRes> {

	protected CommonCallBack() {
		M.getiFrame().setStatus(M.L().runningLabel());
	}

	@Override
	public void onFailure(Throwable caught) {
		M.getiFrame().setError(caught.getLocalizedMessage());
	}

	@Override
	public void onSuccess(RestRes result) {
		M.getiFrame().setStatus(null);
		if (result.isOk())
			doResult(result.getMess());
		else
			doErrorResult(result.getMess());
	}

	public void doErrorResult(String errmess) {
		Window.alert(errmess);
	}

	public abstract void doResult(String jSon);

}
