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
package com.gwthotel.admin.vattax;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwthotel.admin.IGetVatTaxes;
import com.gwthotel.admin.VatTax;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.map.XMap;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.resource.IReadResourceFactory;
import com.jythonui.server.xml.IXMLHelper;
import com.jythonui.server.xml.IXMLToXMap;
import com.jythonui.server.xml.IXMapFactory;

public class GetVatTaxes implements IGetVatTaxes {

    private static final String VATFILE = "vat//vat.xml";
    private static final String VATSTAG = "vattaxes";
    private static final String VATTAG = "vat";

    private final IXMLHelper xHelper;

    @Inject
    public GetVatTaxes(@Named(ISharedConsts.XMLHELPERCACHED) IXMLHelper xHelper) {
        // super(lMess, new String[] { ROLES, ROLESTAG, ROLETAG }, new String[]
        // {
        // IHotelConsts.NAME, IHotelConsts.DESCRIPTION }, iFactory);
        this.xHelper = xHelper;
    }

    void xGetVatTaxes(@Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess lMess,
            IReadResourceFactory iFactory, IXMLToXMap xMap) {
        // super(lMess, new String[] { VATFILE, VATSTAG, VATTAG }, new String[]
        // {
        // IHotelConsts.NAME, IHotelConsts.DESCRIPTION,
        // IHotelConsts.VATLEVELPROP }, iFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VatTax> getList() {

        IXMapFactory xFactory = new IXMapFactory() {

            @Override
            public XMap construct() {
                return new VatTax();
            }
        };

        return (List<VatTax>) xHelper.getList(new String[] { VATFILE, VATSTAG, VATTAG },
               new String[] { IHotelConsts.NAME, IHotelConsts.DESCRIPTION,
                        IHotelConsts.VATLEVELPROP }, xFactory);

    }

}
