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
package com.javahotel.client.user.widgets.stable;

import java.util.Date;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public interface IScrollSeason {

    int getStartNo();

    /**
     * Creates scroll panel.
     *
     * @param no
     *            Total number of lines
     * @param actC
     *            if != -1 then column number to display
     */
    void createVPanel(List<Date> dList, final int actC);
    
    /**
     * Refresh using current settings
     */
    void refresh();
    
}
