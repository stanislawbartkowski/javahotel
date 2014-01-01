/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.daytimetable;

import java.util.Date;

/**
 * @author hotel
 * 
 */
public interface IDrawPartSeasonContext {

    /**
     * Get date related to position c
     * 
     * @param c
     *            relative position to the beginning
     * @return Date
     */
    Date getD(int c);

    /**
     * Get position for the first date in the window
     * 
     * @return Position
     */
    int getFirstD();

    /**
     * Get position of the last day in the window
     * 
     * @return Position
     */
    int getLastD();
        
}
