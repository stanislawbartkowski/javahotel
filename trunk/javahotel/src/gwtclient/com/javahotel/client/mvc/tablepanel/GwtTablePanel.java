/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.tablepanel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class GwtTablePanel implements ITablePanel {

	private final VerticalPanel vp = new VerticalPanel();
	private Panel pa;

	public void addPanels(Widget up, Widget table, Widget add) {
		if (up != null) {
			vp.add(up);
		}
		vp.add(table);
		if (add == null) {
			pa = vp;
		}
		if (add != null) {
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(vp);
			hp.add(add);
			pa = hp;
		}
	}

	public IMvcWidget getMWidget() {
		return new DefaultMvcWidget(pa);
	}
}
