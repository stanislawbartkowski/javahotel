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
package com.javahotel.client.gename;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.VField;
import com.javahotel.common.toobject.IField;

/**
 * @author hotel Utility class for constructing FormField and VHeader objects
 */
public class FFactory {

    private FFactory() {
    }

    public static FormField construct(IField f, IFormLineView e) {
        IGetFieldName i = HInjector.getI().getGetFieldName();
        return new FormField(i.getName(f), e, new VField(f));
    }

    public static FormField constructRM(IField f, IFormLineView e) {
        IGetFieldName i = HInjector.getI().getGetFieldName();
        return new FormField(i.getName(f), e, new VField(f), true, false);
    }

    public static FormField constructRA(IField f, IFormLineView e) {
        IGetFieldName i = HInjector.getI().getGetFieldName();
        return new FormField(i.getName(f), e, new VField(f), true, true);
    }

    public static FormField construct(IField f) {
        IGetFieldName i = HInjector.getI().getGetFieldName();
        return new FormField(i.getName(f), null, new VField(f));
    }

    public static FormField constructRM(IField f) {
        IGetFieldName i = HInjector.getI().getGetFieldName();
        return new FormField(i.getName(f), null, new VField(f), true, false);
    }

    public static FormField constructReadOnly(IField f) {
        IGetFieldName i = HInjector.getI().getGetFieldName();
        return new FormField(i.getName(f), null, new VField(f), true, true);
    }

    public static void add(List<FormField> l, IField[] ft) {
        for (IField f : ft) {
            l.add(construct(f));
        }
    }

    public static void addI(List<IVField> l, IField[] ft) {
        for (IField f : ft) {
            l.add(new VField(f));
        }
    }

    public static List<VListHeaderDesc> constructH(IField[] dList, IField ft[]) {
        return constructH(dList, ft, null);
    }

    private static boolean isEditable(IField f, Set<IField> editable) {
        if (editable == null) {
            return false;
        }
        return editable.contains(f);
    }

    public static Set<IField> createSet(IField ft[]) {
        if (ft == null || ft.length == 0) {
            return null;
        }
        Set<IField> s = new HashSet<IField>();
        for (IField f : ft) {
            s.add(f);
        }
        return s;
    }

    public static List<VListHeaderDesc> constructH(IField[] dList, IField ft[],
            Set<IField> editable) {
        IGetFieldName i = HInjector.getI().getGetFieldName();
        List<VListHeaderDesc> v = new ArrayList<VListHeaderDesc>();
        boolean hidden = false;
        String buttonAction = null;
        if (dList != null) {
            for (IField f : dList) {
                v.add(new VListHeaderDesc(i.getName(f), new VField(f), hidden,
                        buttonAction, isEditable(f, editable)));
            }
        }
        for (IField f : ft) {
            v.add(new VListHeaderDesc(i.getName(f), new VField(f), hidden,
                    buttonAction, isEditable(f, editable)));
        }
        return v;
    }

}
