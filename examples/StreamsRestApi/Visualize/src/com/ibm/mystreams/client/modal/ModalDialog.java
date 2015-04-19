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
package com.ibm.mystreams.client.modal;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author sbartkowski Utility class for implementation of modal dialog
 */

public class ModalDialog {

	public interface IModalDialog {
		void close();

		void show(Widget w, String title);
	}

	private ModalDialog() {
	}

	public static IModalDialog construct() {
		final DialogBox d = new DialogBox();
		return new IModalDialog() {

			@Override
			public void close() {
				d.hide();
			}

			@Override
			public void show(Widget w, String title) {
				d.setWidget(w);
				d.setText(title);
				d.show();
				d.setAnimationEnabled(true);
			}

		};
	}

}
