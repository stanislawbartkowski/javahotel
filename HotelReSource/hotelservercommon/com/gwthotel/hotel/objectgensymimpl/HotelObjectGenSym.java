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
package com.gwthotel.hotel.objectgensymimpl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IHotelObjectGenSym;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.PropDescription;
import com.gwtmodel.table.common.CUtil;
import com.jython.serversecurity.OObjectId;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.storage.gensym.ISymGenerator;
import com.jythonui.shared.JythonUIFatal;

public class HotelObjectGenSym implements IHotelObjectGenSym {

    private final ISymGenerator iGen;

    private final IGetLogMess lMess;

    private static final Logger log = Logger.getLogger(HotelObjectGenSym.class
            .getName());

    private void severe(String errId, String errmess, String sym) {
        String mess = lMess.getMess(errId, errmess, sym);
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess);

    }

    public HotelObjectGenSym(ISymGenerator iGen, IGetLogMess lMess) {
        this.iGen = iGen;
        this.lMess = lMess;
    }

    private String genKey(OObjectId hotelid, String oName) {
        return hotelid.getObject() + "-GENSYM-" + oName;
    }

    @Override
    public void genSym(OObjectId hotelid, PropDescription o, HotelObjects t) {

        if (!CUtil.EmptyS(o.getName()))
            return;
        String oName = t.toString();
        if (!o.isGensymbol()) {
            severe(IHError.HERROR019, IHMess.NAMEEMPTYBUTAUTOMNOTSET, oName);
        }
        String patt = o.getAutomPattern();
        if (CUtil.EmptyS(patt)) {
            severe(IHError.HERROR020, IHMess.NAMEEMPTYBUTAUTOMSETNUTNOTPATTERN,
                    oName);
        }
        String key = genKey(hotelid, oName);
        String sym = iGen.genSym(hotelid.getInstanceId().getInstanceName(),
                key, patt);
        o.setName(sym);
    }

    @Override
    public void clearAll(OObjectId hotelid) {

        for (HotelObjects o : HotelObjects.values()) {
            String key = genKey(hotelid, o.toString());
            iGen.clear(hotelid.getInstanceId().getInstanceName(), key);
        }

    }

}
