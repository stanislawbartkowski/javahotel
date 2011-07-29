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
package com.javahotel.client.gename;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.VField;
import com.javahotel.common.toobject.IField;

/**
 * @author hotel
 * 
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

    public static List<VListHeaderDesc> constructH(IField[] dList, IField ft[]) {
        IGetFieldName i = HInjector.getI().getGetFieldName();
        List<VListHeaderDesc> v = new ArrayList<VListHeaderDesc>();
        if (dList != null) {
            for (IField f : dList) {
                v.add(new VListHeaderDesc(i.getName(f), new VField(f)));
            }
        }
        for (IField f : ft) {
            v.add(new VListHeaderDesc(i.getName(f), new VField(f)));
        }
        return v;
    }

}
