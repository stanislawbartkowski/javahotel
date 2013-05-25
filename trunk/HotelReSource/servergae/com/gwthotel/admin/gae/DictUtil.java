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
package com.gwthotel.admin.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.LoadResult;
import com.gwthotel.admin.gae.entities.EDictionary;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.shared.PropDescription;

public class DictUtil {

    private DictUtil() {

    }

    public static void toProp(PropDescription dest, EDictionary e) {
        dest.setName(e.getName());
        dest.setDescription(e.getDescription());
    }

    public static void toEDict(EDictionary dest, PropDescription sou) {
        dest.setName(sou.getName());
        dest.setDescription(sou.getDescription());
    }

    public static EHotel findHotel(String name) {
        LoadResult<EHotel> p = ofy().load().type(EHotel.class).filter("name ==", name)
                .first();
        if (p == null) {
            return null;
        }
        return p.now();
    }

}
