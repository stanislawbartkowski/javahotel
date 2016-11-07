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
package com.gwtmodel.table.view.ewidget.widgets;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class HTMLBinderWidget implements IBinderWidget {
	
	private final HTML l;
	
	public HTMLBinderWidget(HTML l) {
		this.l = l;
	}

    @Override
    public Object getValObj() {
        return l.getText();
    }

    @Override
    public void setValObj(Object o) {
        l.setHTML((String) o);
    }


    @Override
    public void setReadOnly(boolean readOnly) {
    }

    @Override
    public void setHidden(boolean hidden) {
        l.setVisible(!hidden);
    }

    @Override
    public void setInvalidMess(String errmess) {
    }

    @Override
    public void setGStyleName(String styleMess, boolean set) {
    }

	@Override
	public void setCellTitle(String title) {		
	}

	@Override
	public void setFocus(boolean focus) {	
	}

	@Override
	public boolean isInvalid() {
		return false;
	}

	@Override
	public Widget getGWidget() {
		return l;
	}

}
