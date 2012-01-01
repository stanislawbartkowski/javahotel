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
package com.javahotel.client.abstractto;

import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class Compose3AbstractTo<T1 extends AbstractTo, T2 extends AbstractTo, T3 extends AbstractTo>
        extends AbstractTo {

    private T1 o1;
    private T2 o2;
    private T3 o3;

    public Compose3AbstractTo(T1 o1, T2 o2, T3 o3) {
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
    }

    @Override
    public Object getF(IField f) {
        if (o1.isField(f)) {
            return o1.getF(f);
        }
        if (o2.isField(f)) {
            return o2.getF(f);
        }

        return o3.getF(f);
    }

    @Override
    public boolean isField(final IField f) {
        return o1.isField(f) || o2.isField(f) || o3.isField(f);
    }

    @Override
    public Class<?> getT(IField f) {
        if (o1.isField(f)) {
            return o1.getT(f);
        }
        if (o2.isField(f)) {
            return o2.getT(f);
        }
        return o3.getT(f);
    }

    @Override
    public void setF(IField f, Object o) {
        if (o1.isField(f)) {
            o1.setF(f, o);
            return;
        }
        if (o2.isField(f)) {
            o2.setF(f, o);
            return;
        }
        o3.setF(f, o);
    }

    @Override
    public IField[] getT() {
        IField[] t1 = o1.getT();
        IField[] t2 = o2.getT();
        IField[] t3 = o3.getT();
        return CommandUtil.addT(t1, t2, t3);
    }

    /**
     * @return the o1
     */
    public T1 getO1() {
        return o1;
    }

    /**
     * @return the o3
     */
    public T3 getO3() {
        return o3;
    }

    /**
     * @param o3
     *            the o3 to set
     */
    void setO3(T3 o3) {
        this.o3 = o3;
    }

    /**
     * @param o1
     *            the o1 to set
     */
    public void setO1(T1 o1) {
        this.o1 = o1;
    }

    /**
     * @return the o2
     */
    public T2 getO2() {
        return o2;
    }

    /**
     * @param o2
     *            the o2 to set
     */
    public void setO2(T2 o2) {
        this.o2 = o2;
    }

}
