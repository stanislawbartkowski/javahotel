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
package com.javahotel.nmvc.factories;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IDataFormConstructorAbstractFactory;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.factories.impl.CustomerForm;

public class FormFactory extends HelperFactory implements
        IDataFormConstructorAbstractFactory {

    public CType construct(IDataType dType) {
        if (dType instanceof DataType) {
            DataType da = (DataType) dType;
            if (da.isDictType()) {
                switch (da.getdType()) {
                case CustomerList:
                    return new IDataFormConstructorAbstractFactory.CType(
                            new CustomerForm());
                }
            }
        }
        return new IDataFormConstructorAbstractFactory.CType();
    }

}
