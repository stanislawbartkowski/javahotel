/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.user.season;

import java.util.Date;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.M;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.toobject.OfferSeasonPeriodP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class SeasonUtil {

    private SeasonUtil() {
    }

    public static String getName(final PeriodT t) {

        String sId = "HIGH";
        if (t != null) {
            sId = t.toString();
        }
        return (String) M.L().SeasonNames().get(sId);
    }

    public static String getDateName(Date pe) {
        String na = (pe.getMonth() + 1) + "/" + pe.getDate();
        return na;
    }

    public static String getTodayStyle() {
        return "today";
    }

    public static String getStyleForDay(PeriodT p) {
        OfferSeasonPeriodP pp = (OfferSeasonPeriodP) p.getI();
        String sTyle = "day_high_season";
        if (pp != null) {
            switch (pp.getPeriodT()) {
            case LOW:
                sTyle = "day_low_season";
                break;
            case SPECIAL:
                sTyle = "day_special_season";
                break;
            case LOWWEEKEND:
                sTyle = "day_lowweekend_season";
                break;
            case HIGHWEEKEND:
                sTyle = "day_highweekend_season";
                break;
            default:
                break;
            }
        }
        return sTyle;
    }

    public static Widget createPeriodPopUp(PeriodT pe) {
        VerticalPanel h = new VerticalPanel();
        String s = getName(pe);
        // do not remove
        h.add(new Label(s));
        String s1 = DateFormatUtil.toS(pe.getFrom());
        String s2 = DateFormatUtil.toS(pe.getTo());
        h.add(new Label(s1 + " - " + s2));
        int noD = DateUtil.noLodgings(pe.getFrom(), pe.getTo());
        h.add(new Label(M.M().noSleeps(noD)));
        return h;

    }
}
