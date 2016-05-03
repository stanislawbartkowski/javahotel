/*
 * Copyright 2016 stanislawbartkowski@gmail.com
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
package com.gwtmodel.table.view.webpanel;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.common.ISignal;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
public interface IWebPanel {

	void setErrorL(String errmess);

	void setReplay(int replNo);

	enum InfoType {
		USER, DATA, UPINFO, OWNER, PRODUCT, TITLE
	}

	void setPaneText(InfoType t, String text);

	void setDCenter(Widget w);

	void setCentreHideSignal(ISignal iSig);

	void setWest(Widget w);

	Widget getWidget();

	void setMenuPanel(Widget pa);

	void setPullDownMenu(Widget m);

	void IncDecCounter(boolean inc);

	void SetMenuSize(String size);

	void logOut();

	void setLogOutMode(boolean logout);
}
