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
package com.jythonui.server.objectgensymimpl;

import com.gwtmodel.table.common.CUtil;
import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.crud.ICrudObjectGenSym;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.storage.gensym.ISymGenerator;
import com.jythonui.shared.PropDescription;

public class CrudObjectGenSym extends UtilHelper implements ICrudObjectGenSym {

    private final ISymGenerator iGen;

    private final IGetLogMess lMess;

    // no inject
    public CrudObjectGenSym(ISymGenerator iGen, IGetLogMess lMess) {
        this.iGen = iGen;
        this.lMess = lMess;
    }

    private String genKey(OObjectId hotelid, String oName) {
        return hotelid.getObject() + "-GENSYM-" + oName;
    }

    @Override
    public void genSym(OObjectId hotelid, PropDescription o, String oName) {

        if (!CUtil.EmptyS(o.getName()))
            return;
        if (!o.isGensymbol()) {
            errorMess(lMess, IErrorCode.ERRORCODE113,
                    ILogMess.NAMEEMPTYBUTAUTOMNOTSET, oName);
        }
        String patt = o.getAutomPattern();
        if (CUtil.EmptyS(patt)) {
            errorMess(lMess, IErrorCode.ERRORCODE114,
                    ILogMess.NAMEEMPTYBUTAUTOMSETNUTNOTPATTERN, oName);
        }
        String key = genKey(hotelid, oName);
        String sym = iGen.genSym(hotelid.getInstanceId().getInstanceName(),
                key, patt);
        o.setName(sym);
    }

    @Override
    public void reset(OObjectId hotelid, String oName) {
        String key = genKey(hotelid, oName);
        iGen.clear(hotelid.getInstanceId().getInstanceName(), key);
    }

}
