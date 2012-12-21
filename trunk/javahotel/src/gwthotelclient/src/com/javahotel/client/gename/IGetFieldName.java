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
package com.javahotel.client.gename;

import java.util.List;

import com.javahotel.client.types.DataType;
import com.javahotel.common.toobject.IField;

/**
 * @author hotel
 *
 */
public interface IGetFieldName {
    
    String getName(IField f);
    
    String getName(DataType d, IField f);
    
    List<String> getStandPriceNames();

}
