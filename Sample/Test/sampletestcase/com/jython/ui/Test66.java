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
package com.jython.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.binder.BinderWidget;
import com.jythonui.shared.binder.WidgetTypes;

public class Test66 extends TestHelper {
	
	@Test
	public void test1() {
        DialogFormat d = findDialog("test113.xml");
        assertNotNull(d);
        assertTrue(d.isPolymer());
        BinderWidget ba = d.getBinderW();
        assertNotNull(ba);
        assertEquals(WidgetTypes.UiBinder,ba.getType());
        assertEquals(1,ba.getwList().size());
        System.out.println(ba.getContentHtml());
        assertTrue(CUtil.EmptyS(ba.getContentHtml()));
        BinderWidget h = ba.getwList().get(0);
        assertEquals(WidgetTypes.HTMLPanel,h.getType());
        System.out.println(h.getContentHtml());
        String dt = h.getContentHtml().trim();
        System.out.println(dt);
        // without ui:o
        assertEquals("<H1>Title</H1>",dt);
	}

}
