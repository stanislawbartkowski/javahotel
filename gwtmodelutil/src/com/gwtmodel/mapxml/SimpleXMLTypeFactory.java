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
package com.gwtmodel.mapxml;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gwtmodel.table.common.CUtil;

/**
 * @author hotel Default implementation of IXMLType Factory. Can be used 'as is'
 *         or extended
 */
public class SimpleXMLTypeFactory implements IXMLTypeFactory {

    protected final SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");

    protected boolean isInt(String xType) {
        return xType.equals(INT) || xType.equals(INTEGER);
    }

    @Override
    public Object contruct(String xType, String s) {
        if (CUtil.EmptyS(xType)) {
            return s;
        }
        if (xType.equals(DATE)) {
            try {
                return fo.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        if (xType.equals(DECIMAL)) {
            return new BigDecimal(s);
        }
        if (isInt(xType)) {
            return new Integer(s);
        }
        if (xType.equals(LONG)) {
            return new Long(s);
        }
        return s;
    }

    @Override
    public String toS(String xType, Object o) {
        if (CUtil.EmptyS(xType)) {
            return (String) o;
        }
        if (xType.equals(DATE)) {
            Date d = (Date) o;
            return fo.format(d);
        }
        if (xType.equals(DECIMAL)) {
            BigDecimal b = (BigDecimal) o;
            return b.toString();
        }
        if (xType.equals(LONG)) {
            Long l = (Long) o;
            return l.toString();
        }
        if (isInt(xType)) {
            Integer i = (Integer) o;
            return i.toString();
        }
        return (String) o;
    }

    @Override
    public String getLinesTag() {
        return "//Lines";
    }

    @Override
    public String getLineTag() {
        return "Line";
    }

}
