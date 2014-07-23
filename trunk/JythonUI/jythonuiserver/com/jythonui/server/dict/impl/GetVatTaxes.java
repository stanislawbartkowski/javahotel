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
package com.jythonui.server.dict.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwtmodel.table.map.XMap;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.dict.DictEntry;
import com.jythonui.server.dict.IGetLocalizedDict;
import com.jythonui.server.dict.VatTax;
import com.jythonui.server.xml.IXMLHelper;
import com.jythonui.server.xml.IXMapFactory;

public class GetVatTaxes implements IGetLocalizedDict {

    private static final String VATFILE = "dict//vat//vat.xml";
    private static final String VATSTAG = "vattaxes";
    private static final String VATTAG = "vat";

    private final IXMLHelper xHelper;

    @Inject
    public GetVatTaxes(@Named(ISharedConsts.XMLHELPERCACHED) IXMLHelper xHelper) {
        this.xHelper = xHelper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DictEntry> getList() {

        IXMapFactory xFactory = new IXMapFactory() {

            @Override
            public XMap construct() {
                return new VatTax();
            }
        };

        return (List<DictEntry>) xHelper.getList(new String[] { VATFILE, VATSTAG,
                VATTAG }, new String[] { ISharedConsts.NAME,
                ISharedConsts.DESCRIPTION, ISharedConsts.VATLEVELPROP },
                xFactory);

    }

}
