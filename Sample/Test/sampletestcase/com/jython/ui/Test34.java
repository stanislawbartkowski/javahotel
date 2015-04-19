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
package com.jython.ui;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.RequestContext;

public class Test34 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test70.xml");
        assertNotNull(d);
        DialogInfo di = iServer.findDialog(new RequestContext(), "test70.xml");
        assertNotNull(di);
        ListFormat fo = d.findList("lista");
        assertNotNull(fo);
        DialogFormat e = fo.getfElem();
        assertNotNull(e);
        // dInfo.getSecurity().getlSecur().get(li.getId())
        DialogFormat dE = fo.getfElem();
        assertNotNull(dE);
        ListFormat foE = dE.findList("listan");
        assertNotNull(foE);
        DialogInfo dEE = iServer.findDialog(new RequestContext(),
                foE.getElemFormat());
        assertNotNull(dEE);
    }

}
