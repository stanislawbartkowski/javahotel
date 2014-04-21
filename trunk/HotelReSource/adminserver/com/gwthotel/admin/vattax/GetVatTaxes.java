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

import javax.inject.Inject;
import javax.inject.Named;

import com.gwthotel.admin.IGetVatTaxes;
import com.gwthotel.admin.VatTax;
import com.gwthotel.admin.xmlhelper.ReadXMLHelper;
import com.gwthotel.shared.IHotelConsts;
import com.jython.ui.shared.resource.IReadResourceFactory;
import com.jythonui.server.getmess.IGetLogMess;

public class GetVatTaxes extends ReadXMLHelper<VatTax> implements IGetVatTaxes {

    private static final String VATFILE = "vat//vat.xml";
    private static final String VATSTAG = "vattaxes";
    private static final String VATTAG = "vat";

    @Inject
    public GetVatTaxes(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess,
            IReadResourceFactory iFactory) {
        super(lMess, new String[] { VATFILE, VATSTAG, VATTAG }, new String[] {
                IHotelConsts.NAME, IHotelConsts.DESCRIPTION,
                IHotelConsts.VATLEVELPROP }, iFactory);
    }

    @Override
    protected VatTax constructT() {
        return new VatTax();
    }

}
