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
package com.javahotel.db.copy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.util.StringU;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.Customer;
import com.javahotel.db.hotelbase.jpa.RHotel;
import com.javahotel.db.hotelbase.types.IHotelDictionary;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.IId;
import com.javahotel.db.jtypes.ToLD;
import com.javahotel.db.patternreg.GetNextSym;
import com.javahotel.dbjpa.copybean.CopyBean;
import com.javahotel.dbjpa.copybean.GetFieldHelper;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.GetProp;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.types.IDictionary;
import com.javahotel.types.ILd;
import com.javahotel.types.INumerable;
import com.javahotel.types.LId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class CopyHelper {

    static public void checkPersonDateOp(final ICommandContext iC, final Object o) {
        Method me = null;
        String naF = GetFieldHelper.getF(IMess.DATEOPFIELD);
        try {
            me = GetFieldHelper.getMe(o, naF);
        } catch (NoSuchMethodException ex) {
            // not field, expected
        }
        if (me != null) {
            Date da = (Date) GetFieldHelper.getVal(o, me, iC.getLog());
            if (da == null) {
                da = DateUtil.getToday();
                GetFieldHelper.setterVal(o, da, IMess.DATEOPFIELD, Date.class,
                        iC.getLog());
            }
        }

        me = null;
        naF = GetFieldHelper.getF(IMess.PERSONOPFIELD);
        try {
            me = GetFieldHelper.getMe(o, naF);
        } catch (NoSuchMethodException ex) {
            // not field, expected
        }
        if (me != null) {
            String person = (String) GetFieldHelper.getVal(o, me, iC.getLog());
            if (person == null) {
                person = iC.getHP().getUser();
                GetFieldHelper.setterVal(o, person, IMess.PERSONOPFIELD,
                        String.class, iC.getLog());
            }
        }
    }

    static void copyDict2(final ICommandContext iC,
            final IHotelDictionary sou, final DictionaryP dest,
            final String[] fields) {
        CopyBean.copyBean(sou, dest, iC.getLog(), fields);
        copyHotel(sou, dest);
    }

    static void copyHotel(final IHotelDictionary sou,
            final DictionaryP dest) {
        RHotel r = sou.getHotel();
        dest.setHotel(r.getName());
    }

    static void copyRes2(final ICommandContext iC,
            final IHotelDictionary sou, final DictionaryP dest,
            final String[] malist, String colField, Class<?> memCla) {
        copyDict2(iC, sou, dest, malist);
        copyRes2Collection(iC, sou, dest, colField, memCla);
    }

    static void copyRes2Collection(final ICommandContext iC, final Object sou,
            final AbstractTo dest, final String colField, final Class<?> memCla) {
        List<?> col =
                (List<?>) GetFieldHelper.getterVal(sou, colField, iC.getLog());
        List<Object> de = new ArrayList();
        if (col != null) {
            for (Object o : col) {
                try {
                    Object des = memCla.newInstance();
                    // recursive
                    CommonCopyBean.copyB(iC, o, des);
                    de.add(des);
                } catch (InstantiationException ex) {
                    iC.logFatalE(ex);
                } catch (IllegalAccessException ex) {
                    iC.logFatalE(ex);
                }
            }
        }
        GetFieldHelper.setterVal(dest, de, colField, List.class,
                iC.getLog());
    }

    static void copyDict1(final ICommandContext iC,
            final DictionaryP sou, final IHotelDictionary dest,
            final String[] fields) {
        CopyBean.copyBean(sou, dest, iC.getLog(), fields);
//        RHotel r = CommonHelper.getH(iC, new HotelT(sou.getHotel()));
        dest.setHotel(iC.getRHotel());
    }

    static void copyBeanIId(final ICommandContext iC, final IId sou, final ILd dest,
            final String[] fList) {
        CopyBean.copyBean(sou, dest, iC.getLog(), fList);
        HId id = sou.getId();
        dest.setId(ToLD.toLId(id));
    }

    static void copyBeanINumerable(final ICommandContext iC,
            final INumerable sou, final INumerable dest,
            final String[] fList) {
        CopyBean.copyBean(sou, dest, iC.getLog(), fList);
        Integer id = sou.getLp();
        dest.setLp(id);
    }

    static abstract class IICopyHelper extends AIICopyHelper {

        IICopyHelper(final String[] f) {
            super(f);
        }

        IICopyHelper(final String[] f, final String[] omitf) {
            super(f, omitf);
        }

        @Override
        protected void dcopy(final ICommandContext iC, final Object sou,
                final Object dest) {
        }
    };

    static abstract class INumerableCopyHelper extends AINumerableCopyHelper {

        @Override
        protected void dcopy(final ICommandContext iC, final Object sou,
                final Object dest) {
        }

        protected INumerableCopyHelper(final String[] f) {
            super(f);
        }
    };

    static abstract class AIICopyHelper implements CopyBeanToP.ICopyHelper {

        private final String[] f;
        private final String[] omitf;

        abstract protected void dcopy(final ICommandContext iC,
                final Object sou, final Object dest);

        AIICopyHelper(final String[] f) {
            this.f = f;
            omitf = null;
        }

        AIICopyHelper(final String[] f, final String[] omitf) {
            this.f = f;
            this.omitf = omitf;
        }

        public boolean eq(final Object o1, final Object o2) {
            ILd sou1 = (ILd) o1;
            IId sou2 = (IId) o2;
            LId id1 = sou1.getId();
            if (id1 == null) {
                return false;
            }
            HId id2 = sou2.getId();
            return ToLD.eq(id1,id2);
        }

        public void copy(final ICommandContext iC, final Object sou,
                final Object dest) {
            CopyBean.copyBean(sou, dest, iC.getLog(), f, omitf);
            dcopy(iC, sou, dest);
        }
    };

    static abstract class AINumerableCopyHelper
            implements CopyBeanToP.ICopyHelper {

        abstract protected void dcopy(final ICommandContext iC,
                final Object sou, final Object dest);
        private final String[] f;

        AINumerableCopyHelper(final String[] f) {
            this.f = f;
        }

        public boolean eq(final Object o1, final Object o2) {
            INumerable sou1 = (INumerable) o1;
            INumerable sou2 = (INumerable) o2;
            Integer id1 = sou1.getLp();
            if (id1 == null) {
                return false;
            }
            Integer id2 = sou2.getLp();
            return id1.equals(id2);
        }

        public void copy(final ICommandContext iC, final Object sou,
                final Object dest) {
            CopyBean.copyBean(sou, dest, iC.getLog(), f);
            checkPersonDateOp(iC, dest);
            INumerable sou1 = (INumerable) sou;
            INumerable dest1 = (INumerable) dest;
            Integer lp = sou1.getLp();
            dest1.setLp(lp);
            dcopy(iC, sou, dest);
        }
    };

    static void copyCustomer(final ICommandContext iC, final LId l,
            final Object dest) {
        if (l == null) {
            iC.logFatal(IMessId.NULLCUSTOMER);
        }
        Customer cust = iC.getJpa().getRecord(Customer.class, l);
        GetFieldHelper.setterVal(dest, cust, "customer", Customer.class,
                iC.getLog());
    }


    static void copyCustomer(final ICommandContext iC, final AbstractTo sou,
            final Object dest) {
        LId l = (LId) GetFieldHelper.getterVal(sou, "customer",
                iC.getLog());
        copyCustomer(iC,l,dest);
    }

    static void setPattName(final ICommandContext iC, final IDictionary dic,
            final String pattId, final String pattP) {
        if (!StringU.isEmpty(dic.getName())) {
            return;
        }
        String pId = GetProp.getProp(pattId);
        String patt = GetProp.getProp(pattP);
        Date toDay = DateUtil.getToday();
        String sym = GetNextSym.NextSym(iC, pId, toDay, patt);
        dic.setName(sym);
    }
    
    static void copyID(ILd sou, IId dest) {
    	LId id1 = sou.getId();
    	HId id2 = null;
    	if (id1 != null) {
    		id2 = new HId(id1);    		
    	}
    	dest.setId(id2);
    }
    
    static void copyID(IId sou, ILd dest) {
    	HId id1 = sou.getId();
    	LId id2 = null;
    	if (id1 != null) {
    		id2 = new LId(id1.getL());    		
    	}
    	dest.setId(id2);
    }
}
