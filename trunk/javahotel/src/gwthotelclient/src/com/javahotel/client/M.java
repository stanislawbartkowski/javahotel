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
package com.javahotel.client;

import com.google.gwt.core.client.GWT;

/**
 * @author hotel
 *
 */
public class M {
    
    private M() {
    }
    
    private static final HoLabel sLab = (HoLabel) GWT.create(HoLabel.class);
    private static final HoMessages sMess = (HoMessages) GWT.create(HoMessages.class);
    
    public static HoLabel L() {
        return sLab;
    }
    
    public static HoMessages M() {
        return sMess;
    }


}