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
package com.javahotel.common.command;

import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class CommandUtil {

    private CommandUtil() {
    }

    public static String getHash(final RType r, final CommandParam p) {

        if (p == null) {
            return r.toString();
        }
        return r.toString() + p.toHashString();
    }

    public static DictionaryP.F dF(final IField fType) {
        if (fType instanceof DictionaryP.F) {
            return (DictionaryP.F) fType;
        }
        return null;
    }

    public static void copyA(final AbstractTo sou, final AbstractTo dest,
            final IField[] f) {
        for (int i = 0; i < f.length; i++) {
            Object o = sou.getF(f[i]);
            dest.setF(f[i], o);
        }
    }

    public static IField[] addT(final IField[]... tt) {
        int size = 0;
        for (int i = 0; i < tt.length; i++) {
            size += tt[i].length;
        }
        IField[] t = new IField[size];
        int j = 0;
        for (int i = 0; i < tt.length; i++) {
            for (int ii = 0; ii < tt[i].length; ii++) {
                t[j++] = tt[i][ii];
            }
        }
        return t;
    }

    public static IField[] addTD(final IField[] t1) {
        return addT(t1, DictionaryP.F.values());
    }
}
