/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwthotel.admin.xmltomap;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwthotel.admin.IXMLToMap;
import com.gwthotel.admin.xmlhelper.ReadXMLHelper;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.jythonui.server.getmess.IGetLogMess;

public class XMLToMap implements IXMLToMap {

    private final IGetLogMess lMess;

    @Inject
    public XMLToMap(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess) {
        this.lMess = lMess;
    }

    @Override
    public PropDescription readXML(String xml, String rootTag, String elemTag) {
        ReadXMLHelper<PropDescription> reader = new ReadXMLHelper<PropDescription>(
                lMess, new String[] { null, rootTag, elemTag, xml }, null) {

            @Override
            protected PropDescription constructT() {
                @SuppressWarnings("serial")
                PropDescription prop = new PropDescription() {

                };
                return prop;
            }

        };
        List<PropDescription> l = reader.getList();
        return l.get(0);
    }

}
