/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not ue this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.jythonui.client.dialog.util;

import com.jythonui.client.dialog.IReadDialog;
import com.jythonui.client.util.U;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.view.util.PolymerUtil;

public class VerifyDialog {

	private VerifyDialog() {
	}

	private static class E {
		boolean valid = true;
	}

	public static boolean verify(IEventBus iBus, boolean syntaxonly) {
		IReadDialog iR = U.getIDialog(iBus);

		E e = new E();

		PolymerUtil.walkHTMLPanel(iR.getH(), (fieldid, w) -> {
			BiWidget bi = new BiWidget(w, fieldid);
			if (!bi.validate())
				e.valid = false;
		});
		return e.valid;
	}

}
