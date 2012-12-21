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
package com.javahotel.common.dateutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwtmodel.table.common.PeriodT;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
abstract public class GetPeriodsTemplate {

    protected final Date startD;
    protected final List<Date> dLine;
    protected final List<PeriodT> coP;

    protected GetPeriodsTemplate(final Date startD,
            final List<Date> dLine, final List<PeriodT> coP) {
        this.startD = startD;
        this.dLine = dLine;
        this.coP = coP;
    }

    abstract protected int startF(final Date dd);

    abstract protected void addElem(PeriodT pr, int wi);

    abstract protected void endF();

    public void drawD(final int startno, final int ldraw) {
        Date prevd = startD;
        int currentno = -1;
        for (final Date dd : dLine) {
            currentno++;
            if (currentno > ldraw) {
                break;
            }
            if (currentno >= startno) {
                int no = startF(dd);
                assert no != 0 : "Width cannot be 0";

                List<PeriodT> cou = GetPeriods.get(new PeriodT(prevd, dd, null), coP);
                int pnoD = DateUtil.noLodgings(prevd, dd);
                List<Integer> pCo = new ArrayList<Integer>();
                for (PeriodT pr : cou) {
                    int noD = DateUtil.noLodgings(pr.getFrom(), pr.getTo());
                    pCo.add(new Integer(noD));
                }
                int[] pixe = CountPixel.countP(no, pnoD, pCo);
                int iP = 0;

                for (PeriodT pr : cou) {
                    int wi = pixe[iP++];
                    addElem(pr, wi);
                }
                endF();
            }
            prevd = DateUtil.copyDate(dd);
            DateUtil.NextDay(prevd);
        }
    }
}
