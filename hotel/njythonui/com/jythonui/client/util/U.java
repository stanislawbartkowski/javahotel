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
package com.jythonui.client.util;

import com.google.gwt.user.client.ui.Widget;
import com.jythonui.client.dialog.IReadDialog;
import com.polymerui.client.eventbus.EventDialogGetHTML;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.eventbus.IInfo;

public class U {

	private U() {

	}

	public static <T extends Widget> T castP(Widget w, Class<T> cl) {

		if (w.getClass().equals(cl))
			return (T) w;
		return null;
	}

	public static IReadDialog getIDialog(IEventBus i) {
		IInfo<IReadDialog> r = i.request(new EventDialogGetHTML());
		assert r != null;
		return r.get();
	}

}
