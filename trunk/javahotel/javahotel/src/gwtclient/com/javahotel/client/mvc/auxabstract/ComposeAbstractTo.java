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

package com.javahotel.client.mvc.auxabstract;

import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ComposeAbstractTo<T1 extends AbstractTo,T2 extends AbstractTo >
        extends AbstractTo {

    private T1 o1;
    private T2 o2;

    public ComposeAbstractTo(T1 o1,T2 o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public ComposeAbstractTo(T1 o1) {
        this.o1 = o1;
        this.o2 = null;
    }

    public ComposeAbstractTo() {
        this.o1 = null;
        this.o2 = null;
    }

    @Override
    public Object getF(IField f) {
        if (getO1().isField(f)) {
            return getO1().getF(f);
        }
        return getO2().getF(f);
    }

    @Override
    public Class getT(IField f) {
        if (getO1().isField(f)) {
            return getO1().getT(f);
        }
        return getO2().getT(f);
    }

    @Override
    public void setF(IField f, Object o) {
        if (getO1().isField(f)) {
            getO1().setF(f, o);
        }
        else {
            getO2().setF(f, o);
        }
    }

    @Override
    public IField[] getT() {
        IField[] t1 = getO1().getT();
        IField[] t2 = getO2().getT();
        return CommandUtil.addT(t1,t2);
    }

    /**
     * @return the o1
     */
    public T1 getO1() {
        return o1;
    }

    /**
     * @param o1 the o1 to set
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
     * @param o2 the o2 to set
     */
    public void setO2(T2 o2) {
        this.o2 = o2;
    }

}
