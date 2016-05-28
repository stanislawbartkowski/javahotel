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
package com.gwtmodel.table.view.ewidget.gwt;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.LabelBase;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormChangeListener;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.ITouchListener;

class LabelFor implements IFormLineView {

    private final Widget w;

    public static native Element createLabelElement(Document doc, String forL,
            String content) /*-{
                            var s = doc.createElement("span");                
                            var e = doc.createElement("label");
                            e.htmlFor = forL;
                            var t = document.createTextNode(content);
                            e.appendChild(t);
                            s.appendChild(e);
                            return s;
                            }-*/;

    LabelFor(IVField v, String la) {
        w = new LabelBase(createLabelElement(Document.get(), v.getId(), la)) {
        };
    }

    @Override
    public IVField getV() {
        return null;
    }

    @Override
    public Object getValObj() {
        return null;
    }

    @Override
    public void setValObj(Object o) {
    }

    @Override
    public Widget getGWidget() {
        return w;
    }

    @Override
    public void addChangeListener(IFormChangeListener cListener) {
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    @Override
    public void setHidden(boolean hidden) {
    }

    @Override
    public void setInvalidMess(String errmess) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGStyleName(String styleMess, boolean set) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setOnTouch(ITouchListener lTouch) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getHtmlName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttr(String attrName, String attrValue) {
        // TODO Auto-generated method stub

    }

	@Override
	public void setCellTitle(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSuggestList(List<String> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocus(boolean focus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInvalid() {
		// TODO Auto-generated method stub
		return false;
	}

}
