/*
 *  Copyright 2013 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.editc;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IGWidget;

/**
 *
 * @author hotel
 */
public interface IChangeObject extends ICustomObject {
    
    /**
     * Signal sent after choosing object from list
     */
    String choosedString = "CHOOSED-SIGNAL-STRING";

    int NEW = 0;
    int CHANGE = 1;
    
    /**
     * Signal sent after changing checkboxes NEW or CHANGE
     */
    String signalString = "CHANGE-SIGNAL-STRING";

    /**
     * Which checkbox has been changed
     * @return NEW or CHANGE
     */
    int getWhat();

    boolean getSet();
    
    IGWidget getW();
}
