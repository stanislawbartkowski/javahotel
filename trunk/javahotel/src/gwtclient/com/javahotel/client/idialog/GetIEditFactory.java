/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.idialog;

import java.util.List;
import java.util.Map;

import com.javahotel.client.CommonUtil;
import com.javahotel.client.IResLocator;
import com.javahotel.client.idialogutil.AddBoxValues;
import com.javahotel.client.ifield.ICreatedValue;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GetIEditFactory {

    private static class getValueText extends ExtendTextBox {

        getValueText(final IResLocator pLoc, boolean password) {
            super(pLoc, password);
        }
    }

    private static class getValueCheck extends ExtendTextBox {

        getValueCheck(final IResLocator pLoc, boolean checkenable) {
            super(pLoc, false, checkenable);
        }
    }

    private static class getNumField extends ExtendTextBox {

        getNumField(final IResLocator pLoc) {
            super(pLoc, false);
        }

        @Override
        public boolean validateField() {
            String s = getVal();
            int i = CommonUtil.getNum(s);
            return i != CommonUtil.BADNUMBER;
        }
    }

    public static ILineField getLBEditI(final IResLocator pLoc) {
        return new GetValueLB(pLoc);
    }

    public static ILineField getPasswordEditI(final IResLocator pLoc) {
        return new getValueText(pLoc, true);
    }

    public static void AddValues(final IResLocator pLoc,
            final ILineField e, final IField f,
            final List<? extends AbstractTo> col) {
        GetValueLB lB = (GetValueLB) e;
        AddBoxValues.addValues(pLoc, f, null, lB, col);
    }

    public static ILineField getTextEditI(final IResLocator pLoc) {
        return new getValueText(pLoc, false);
    }

    public static ILineField getTextCheckEditI(final IResLocator pLoc,
            boolean checkenable) {
        return new getValueCheck(pLoc, checkenable);
    }

    public static ILineField getNumEditI(final IResLocator pLoc) {
        return new getNumField(pLoc);
    }

    public static ILineField getListValuesBox(final IResLocator pLoc,
            final RType r, final CommandParam p, final IField f,
            final ICreatedValue iV) {
        GetValueLB lB = new GetValueLB(pLoc);
        AddBoxValues.addValues(pLoc, r, p, f, iV, lB);
        return lB;
    }

    public static ILineField getListValuesBox(final IResLocator pLoc,
            final RType r, final CommandParam p, final IField f) {
        return getListValuesBox(pLoc, r, p, f, null);
    }

//    public static ILineField getTextCalendard(final IResLocator pLoc) {
//        return new DateCalendar(pLoc);
//    }
    
    public static ILineField getTextCalendard(final IResLocator pLoc) {
        return new DDateCalendar(pLoc);
    }

    public static ILineField getNumberCalculator(final IResLocator pLoc) {
        return new NumberCalculator(pLoc);
    }

    public static ELineDialogMulChoice getMChoice(final IResLocator pLoc,
            final CommandParam p) {
        return new ELineDialogMulChoice(pLoc, p, true);
    }

    public static ELineDialogMulChoice getMChoice(final IResLocator pLoc,
            final CommandParam p, final boolean enable) {
        return new ELineDialogMulChoice(pLoc, p, enable);
    }

    public static ELineDialogMulChoice getMChoice(final IResLocator pLoc,
            final CommandParam p, final boolean enable,
            final boolean readystart) {
        return new ELineDialogMulChoice(pLoc, p, enable,readystart);
    }

    public static ILineField getEnumMap(final IResLocator pLoc, Map<String,String> ma) {
        return new GetEnumLB(pLoc, ma);
    }

    public static ILineField getLabelEdit(final IResLocator pLoc,
            final String lab) {
        return new LabelEdit(pLoc, lab);
    }
}
