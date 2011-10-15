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
package com.javahotel.db.commands;

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PersistType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.db.context.ICommandContext;

public class TestPersistObject {

    public static void testPersist(ICommandContext iC, ReturnPersist ret,
            PersistType t, Object a, DictType d) {
        switch (t) {
        case REMOVE:
            return;
        case CHANGE:
            return;
        }
        DictExists.ErrKom e = DictExists.existsAlready(iC, a, d);
        if (e == null) {
            return;
        }
        ret.setErrorMessage(e.getErrInfo());
        ret.setViewName(e.getViewName());
    }

}
