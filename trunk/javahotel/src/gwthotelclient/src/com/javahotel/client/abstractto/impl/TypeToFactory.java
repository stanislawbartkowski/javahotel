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
package com.javahotel.client.abstractto.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.AbstractListT;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.rdef.IFormLineView;
import com.javahotel.client.IResLocator;
import com.javahotel.client.abstractto.BookPaymentField;
import com.javahotel.client.abstractto.IAbstractFactory;
import com.javahotel.client.abstractto.IAbstractType;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.CustomerType;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.IdentDocType;
import com.javahotel.common.command.PersonTitle;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.ServiceType;
import com.javahotel.common.toobject.VatDictionaryP;
import com.javahotel.nmvc.ewidget.EWidgetFactory;

/**
 * @author hotel
 * 
 */
public class TypeToFactory implements IAbstractType {

    private static final Map<IField, T> ma = new HashMap<IField, T>();
    private static final T decimalT = new T(BigDecimal.class,
            FieldDataType.constructBigDecimal());
    private static final T dateT = new T(Date.class,
            FieldDataType.constructDate());
    private static final T longT = new T(Long.class,
            FieldDataType.constructLong());
    private static final T intT = new T(Long.class,
            FieldDataType.constructInt());
    private static final T stringT = new T(String.class,
            FieldDataType.constructString());
    private final static FieldDataType.ICustomType staDictionary = new DictionaryToS<DictionaryP>(
            DictType.RoomStandard, DictionaryP.class);
    private static final T stringDT = new T(String.class,
            FieldDataType.constructString(staDictionary,
                    new RoomStandardFactory()));

    private static final T stringSeaT = new T(String.class,
            FieldDataType.constructString(new SeasonListFactory()));
    private static final T stringServT = new T(String.class,
            FieldDataType.constructString(new ServiceListFactory()));
    private static final T stringPriceT = new T(String.class,
            FieldDataType.constructString(new PriceListFactory()));
    private static final T stringRoomT = new T(String.class,
            FieldDataType.constructString(new RoomListFactory()));
    // SeasonListFactory
    private static final getMap ge = new getMap() {

        @Override
        public Map<String, String> getM(IResLocator rI) {
            return rI.getLabels().Services();
        }
    };
    private static final EnumTypeToS<ServiceType> e = new EnumTypeToS<ServiceType>(
            new GetMap(ge), ServiceType.DOSTAWKA);
    private static final T stringServiceT = new T(String.class,
            FieldDataType.constructEnum(e));
    private static final getMap gec = new getMap() {

        @Override
        public Map<String, String> getM(IResLocator rI) {
            return rI.getLabels().CustomerType();
        }
    };
    private static final EnumTypeToS<CustomerType> ec = new EnumTypeToS<CustomerType>(
            new GetMap(gec), CustomerType.Company);
    private static final getMap gept = new getMap() {

        @Override
        public Map<String, String> getM(IResLocator rI) {
            return rI.getLabels().PTitles();
        }
    };
    private static final EnumTypeToS<PersonTitle> ept = new EnumTypeToS<PersonTitle>(
            new GetMap(gept), PersonTitle.Mr);
    private static final getMap gedoct = new getMap() {

        @Override
        public Map<String, String> getM(IResLocator rI) {
            return rI.getLabels().DocTypes();
        }
    };
    private static final EnumTypeToS<IdentDocType> edoct = new EnumTypeToS<IdentDocType>(
            new GetMap(gedoct), IdentDocType.DriverLicense);
    private static final T stringDocT = new T(String.class,
            FieldDataType.constructEnum(edoct));
    private static final T stringCustomerT = new T(String.class,
            FieldDataType.constructEnum(ec));
    private static final T stringPersonT = new T(String.class,
            FieldDataType.constructEnum(ept));
    private final static FieldDataType.ICustomType iVat = new DictionaryToS<VatDictionaryP>(
            DictType.VatDict, VatDictionaryP.class);
    private static final T stringVatT = new T(String.class,
            FieldDataType.constructString(iVat, new VatListFactory()));

    private static final T paymentT = new T(BigDecimal.class,
            FieldDataType.constructCustom(FieldDataType.T.BIGDECIMAL,
                    FieldDataType.constructEmptyCustomValue()));

    private static class GetMap implements AbstractListT.IGetMap {

        private final getMap g;

        GetMap(getMap g) {
            this.g = g;
        }

        @Override
        public Map<String, String> getM() {
            IResLocator rI = HInjector.getI().getI();
            return g.getM(rI);
        }
    }

    private interface getMap {

        Map<String, String> getM(IResLocator rI);
    }

    private static class EnumTypeToS<T extends Enum> extends AbstractListT
            implements FieldDataType.IEnumType {

        private final Class cl;
        private final T t;

        EnumTypeToS(AbstractListT.IGetMap g, T t) {
            super(g);
            this.cl = t.getClass();
            this.t = t;
        }

        @Override
        public boolean IsNullEnum(Enum e) {
            return e == null;
        }

        @Override
        public Enum toEnum(String e) {
            if (CUtil.EmptyS(e)) {
                return null;
            }
            String val = getValue(e);
            T se = (T) t.valueOf(cl, val);
            return se;
        }

        @Override
        public String assertS(Object sou) {
            if (sou.getClass().equals(cl)) {
                return null;
            }
            return FUtils.constructAssertS(sou, cl);
        }

        @Override
        public List<String> getValues() {
            return getListVal();
        }
    }

    private static abstract class DictListFactory implements
            FieldDataType.IFormLineViewFactory {

        private final DictType d;
        private final IField f;

        DictListFactory(DictType d, IField f) {
            this.d = d;
            this.f = f;
        }

        DictListFactory(DictType d) {
            this.d = d;
            this.f = DictionaryP.F.name;
        }

        @Override
        public IFormLineView construct(IVField v) {
            IResLocator rI = HInjector.getI().getI();
            EWidgetFactory heFactory = HInjector.getI().getEWidgetFactory();
            CommandParam p = rI.getR().getHotelCommandParam();

            p.setDict(d);
            IFormLineView i = heFactory.getListValuesBox(v, p, f);
            return i;
        }
    }

    private static class RoomListFactory extends DictListFactory {

        RoomListFactory() {
            super(DictType.RoomObjects);
        }

    }

    private static class SeasonListFactory extends DictListFactory {
        SeasonListFactory() {
            super(DictType.OffSeasonDict);
        }
    }

    private static class VatListFactory extends DictListFactory {
        VatListFactory() {
            super(DictType.VatDict);
        }
    }

    private static class ServiceListFactory extends DictListFactory {
        ServiceListFactory() {
            super(DictType.ServiceDict);
        }
    }

    private static class PriceListFactory extends DictListFactory {
        PriceListFactory() {
            super(DictType.PriceListDict);
        }
    }

    private static class RoomStandardFactory extends DictListFactory {
        RoomStandardFactory() {
            super(DictType.RoomStandard);
        }
    }

    private static class DictionaryToS<T extends DictionaryP> implements
            FieldDataType.ICustomType {

        private final DictType da;
        private final Class<?> cl;

        DictionaryToS(DictType da, Class<?> cl) {
            this.da = da;
            this.cl = cl;
        }

        @Override
        public Object fromCustom(Object sou) {
            if (sou == null) {
                return "";
            }
            @SuppressWarnings("unchecked")
            T va = (T) sou;
            return va.getName();
        }

        @Override
        public Object toCustom(Object sou) {
            if (sou == null) {
                return null;
            }
            IAbstractFactory i = HInjector.getI().getAbstractFactory();
            T t = (T) i.construct(new DataType(da));
            String s = (String) sou;
            t.setName(s);
            return t;
        }

        @Override
        public String assertS(Object sou) {
            if (sou.getClass().equals(cl)) {
                return null;
            }
            return FUtils.constructAssertS(sou, cl);
        }

        @Override
        public boolean isNullCustom(Object sou) {
            DictionaryP d = (DictionaryP) sou;
            return CUtil.EmptyS(d.getName());
        }
    }

    static {
        ma.put(OfferServicePriceP.F.highseasonprice, decimalT);
        ma.put(OfferServicePriceP.F.highseasonweekendprice, decimalT);
        ma.put(OfferServicePriceP.F.lowseasonprice, decimalT);
        ma.put(OfferServicePriceP.F.lowseasonweekendprice, decimalT);

        ma.put(OfferSeasonP.F.startp, dateT);
        ma.put(OfferSeasonP.F.endp, dateT);

        ma.put(BookingP.F.checkIn, dateT);
        ma.put(BookingP.F.checkOut, dateT);
        ma.put(BookingP.F.noPersons, intT);
        ma.put(BookingP.F.season, stringSeaT);

        ma.put(BookRecordP.F.customerPrice, decimalT);
        // ma.put(BookRecordP.F.oPrice, decimalT);

        ma.put(PaymentP.F.amount, decimalT);
        ma.put(PaymentP.F.datePayment, dateT);

        ma.put(GuestP.F.checkIn, dateT);
        ma.put(GuestP.F.checkOut, dateT);

        ma.put(AddPaymentP.F.payDate, dateT);
        ma.put(AddPaymentP.F.noSe, longT);
        ma.put(AddPaymentP.F.customerPrice, decimalT);
        ma.put(AddPaymentP.F.customerSum, decimalT);

        ma.put(ResObjectP.F.standard, stringDT);
        ma.put(ResObjectP.F.noperson, intT);
        ma.put(ResObjectP.F.maxperson, intT);

        ma.put(VatDictionaryP.F.vat, decimalT);

        // ServiceDictionaryP.F.servtype

        ma.put(ServiceDictionaryP.F.servtype, stringServiceT);
        ma.put(ServiceDictionaryP.F.vat, stringVatT);

        ma.put(CustomerP.F.cType, stringCustomerT);
        ma.put(CustomerP.F.pTitle, stringPersonT);
        ma.put(CustomerP.F.docType, stringDocT);

        ma.put(AdvancePaymentP.F.validationDate, dateT);
        ma.put(AdvancePaymentP.F.amount, decimalT);

        ma.put(VatDictionaryP.F.vat, decimalT);

        ma.put(BookElemP.F.checkIn, dateT);
        ma.put(BookElemP.F.checkOut, dateT);
        ma.put(BookElemP.F.resObject, stringRoomT);
        ma.put(BookElemP.F.service, stringServT);

        ma.put(OfferPriceP.F.season, stringSeaT);

        ma.put(BookPaymentField.customerPrice, paymentT);
        ma.put(BookPaymentField.offerPrice, paymentT);

        ma.put(BookRecordP.F.oPrice, stringPriceT);
    }

    @Override
    public T construct(IField i) {
        T t = ma.get(i);
        if (t == null) {
            return stringT;
        }
        return t;
    }

}
