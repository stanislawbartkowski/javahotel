/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwtmodel.table;

import java.util.List;

/**
 *
 * @author perseus
 */
public class DataListTypeFactory {

    public static IDataListType construct(List<IVModelData> dList) {
        return new DataListType(dList, null);

    }

    public static IDataListType construct(List<IVModelData> dList, IVField comboFie) {
        return new DataListType(dList, comboFie);

    }
}
