/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.types;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.login.LoginData;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCustom;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.ValidateUtil;
import com.javahotel.client.abstractto.IAbstractFactory;
import com.javahotel.client.abstractto.InvoicePVData;
import com.javahotel.client.injector.HInjector;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PersistType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.ServiceDictionaryP;

public class DataUtil {

    private DataUtil() {
    }

    public static ISlotCustom constructValidateAgain(IDataType dType) {
        return new CustomStringDataTypeSlot("HOTEL-VALIDATE-AGAIN", dType);
    }

    public static IVModelData construct(IDataType dType) {
        DataType daType = (DataType) dType;
        if (daType.isAllPersons()) {
            return new LoginData();
        }
        if (daType.isDictType(DictType.InvoiceList)) {
            return new InvoicePVData();
        }
        IAbstractFactory i = HInjector.getI().getAbstractFactory();
        AbstractTo a = i.construct(daType);
        if (daType.isAddType()) {
            switch (daType.getAddType()) {
            case BookRoom:
            case BookNoRoom:
            case Payment:
                return VModelDataFactory.constructLp(a);
            }
        }
        return VModelDataFactory.construct(a);
    }

    public static IDataListType construct(List<? extends AbstractTo> dList) {
        List<IVModelData> dvList = new ArrayList<IVModelData>();
        for (AbstractTo a : dList) {
            dvList.add(VModelDataFactory.construct(a));
        }
        return DataListTypeFactory.construct(dvList);
    }

    public static PersistType persistTo(PersistTypeEnum persisEnumType) {
        switch (persisEnumType) {
        case ADD:
            return PersistType.ADD;
        case MODIF:
            return PersistType.CHANGE;
        case REMOVE:
            return PersistType.REMOVE;
        }
        return null;
    }

    public static List<String> fromDicttoString(
            List<? extends DictionaryP> dList) {
        ArrayList<String> sList = new ArrayList<String>();
        if (dList == null) {
            return sList;
        }
        for (DictionaryP d : dList) {
            String s = d.getName();
            sList.add(s);
        }
        return sList;
    }

    public static <T extends DictionaryP> void fromStringToDict(
            List<String> sList, List<T> dList, List<T> persistList) {
        for (String s : sList) {
            for (T d : dList) {
                if (s.equals(d.getName())) {
                    persistList.add(d);
                }
            }
        }
    }

    public static <T extends AbstractTo> List<T> construct(
            IDataListType dataListType) {
        List<T> dList = new ArrayList<T>();

        for (IVModelData v : dataListType.getList()) {
            HModelData vData = (HModelData) v;
            @SuppressWarnings("unchecked")
            T t = (T) vData.getA();
            dList.add(t);
        }
        return dList;
    }

    public static <T extends AbstractTo> T getData(ISlotSignalContext slContext) {
        return getData(slContext.getVData());
    }

    public static <T extends AbstractTo> T getData(IDataType dType,
            ISlotable iSlo, GetActionEnum e) {
        IVModelData v = iSlo.getSlContainer().getGetterIVModelData(dType, e);
        return getData(v);
    }

    public static <T extends AbstractTo> T getData(IVModelData mData) {
        HModelData vData = (HModelData) mData;
        @SuppressWarnings("unchecked")
        T cust = (T) vData.getA();
        return cust;
    }

    public static void addToSet(Set<IVField> set, List<FormField> fList,
            IField fi) {
        VField vi = new VField(fi);
        for (FormField f : fList) {
            IVField v = f.getFie();
            if (v.eq(vi)) {
                set.add(v);
            }
        }
    }

    /**
     * Gets date from FormView
     * 
     * @param iSlo
     *            ISlotable containing path to FormView
     * @param dType
     *            IDataType
     * @param f
     *            IField of the field to retrieve
     * @return Date or null (if not exist)
     */
    public static Object getO(ISlotable iSlo, IDataType dType, IField f) {
        IFormLineView i = iSlo.getSlContainer().getGetterFormLine(dType,
                new VField(f));
        if (i == null) {
            return null;
        }
        return i.getValObj();
    }

    /**
     * Sets BigDecimal to FormView
     * 
     * @param iSlo
     *            ISlotable containing path to FormView
     * @param dType
     *            IDataType
     * @param fie
     *            IField identifier
     * @param b
     *            BigDecimal to be displayed
     */
    public static void setBigDecimal(ISlotable iSlo, IDataType dType,
            IField fie, BigDecimal b) {
        IFormLineView i = iSlo.getSlContainer().getGetterFormLine(dType,
                new VField(fie));
        if (i == null) {
            return;
        }
        i.setValObj(b);
    }

    /**
     * Copy list of fields from AbstractTo to the second AbstractTp
     * 
     * @param from
     *            Source AbstractTo
     * @param to
     *            Destination AbstractTo
     * @param listFrom
     *            List of fields to copy from
     * @param listTo
     *            List of fields to copy to
     */
    public static void copyField(AbstractTo from, AbstractTo to,
            IField[] listFrom, IField[] listTo) {
        for (int i = 0; i < listFrom.length; i++) {
            Object o = from.getF(listFrom[i]);
            to.setF(listTo[i], o);
        }
    }

    /**
     * Converts IField array to IField list
     * 
     * @param ft
     *            Array
     * @return List
     */
    public static List<IVField> toList(IField[] ft) {
        List<IVField> eLList = new ArrayList<IVField>();
        for (IField f : ft) {
            eLList.add(new VField(f));
        }
        return eLList;
    }

    public static boolean isEmpty(AbstractTo to, IField[] fList) {
        IVModelData v = VModelDataFactory.construct(to);
        List<IVField> vList = new ArrayList<IVField>();
        for (IField f : fList) {
            vList.add(new VField(f));
        }
        return ValidateUtil.isEmpty(v, vList);

    }

    private static class Ite<T extends AbstractTo> implements Iterator<T> {

        private final Iterator<IVModelData> ite;

        Ite(List<IVModelData> vList) {
            this.ite = vList.iterator();
        }

        @Override
        public boolean hasNext() {
            return ite.hasNext();
        }

        @Override
        public T next() {
            IVModelData v = ite.next();
            if (v == null) {
                return null;
            }
            return DataUtil.getData(v);
        }

        @Override
        public void remove() {
            ite.remove();
        }

    }

    private static <T extends AbstractTo> Iterable<T> getI(
            final List<IVModelData> vList) {
        return new Iterable<T>() {

            @Override
            public Iterator<T> iterator() {
                return new Ite(vList);
            }

        };
    }

    public static <T extends AbstractTo> Iterable<T> getI(IDataListType dList) {
        return getI(dList.getList());
    }

    public static List<ServiceDictionaryP> createListOfServices(ResObjectP r,
            RoomStandardP sa, boolean roomOnly) {

        List<ServiceDictionaryP> li = new ArrayList<ServiceDictionaryP>();
        for (ServiceDictionaryP s : sa.getServices()) {
            if (!s.getServType().isRoomBooking()) {
                if (roomOnly) {
                    continue;
                }
                li.add(s);
                continue;
            }
            if (s.getNoPerson().compareTo(r.getMaxPerson()) > 0) {
                continue;
            }
            li.add(s);
        }
        return li;
    }

}
