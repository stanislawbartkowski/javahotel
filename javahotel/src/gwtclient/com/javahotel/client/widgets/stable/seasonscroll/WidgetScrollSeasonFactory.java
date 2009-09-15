/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.widgets.stable.seasonscroll;

import com.javahotel.client.IResLocator;
import com.javahotel.client.widgets.stable.IDrawPartSeason;
import com.javahotel.client.widgets.stable.IScrollSeason;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class WidgetScrollSeasonFactory {

    private WidgetScrollSeasonFactory() {
    }

    public static IScrollSeason getScrollSeason(final IResLocator pLoc,
            final IDrawPartSeason i, final int periodNo) {
   //     return new MonthSeasonScrollWidget(pLoc, i, periodNo);
        return new DaySeasonScrollWidget(pLoc, i, periodNo);
    }
}
