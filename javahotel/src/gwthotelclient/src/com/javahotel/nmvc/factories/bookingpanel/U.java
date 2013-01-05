/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.bookingpanel;

import java.util.Date;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeasonContext;
import com.javahotel.client.M;
import com.javahotel.client.types.VField;
import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResDayObjectStateP;

/**
 * @author hotel
 * 
 */
public class U {

    private U() {

    }

    static Date getD(IDrawPartSeasonContext sData, int i) {
        if (sData == null) {
            return null;
        }
        Date d = sData.getD(i + sData.getFirstD());
        return d;
    }

    static SafeHtml getEmpty() {
        return new SafeHtmlBuilder().appendEscaped("").toSafeHtml();
    }

    static String getRoomName(IVModelData v) {
        String s = (String) v.getF(new VField(DictionaryP.F.name));
        return s;
    }

    static BookingStateType getResState(ResDayObjectStateP p) {
        assert p != null : M.M().ResStateCannotBeNull();
        BookingStateP staP = p.getLState();
        BookingStateType staT = null;
        if (staP != null) {
            staT = staP.getBState();
        }
        return staT;
    }

    /**
     * Test if reservation cell is in booked stated
     * 
     * @param p
     *            BookingStateType (can be null)
     * @return True if booked
     */
    static boolean isBooked(BookingStateType p) {
        if (p == null) {
            return false;
        }
        return p.isBooked();
    }

}
