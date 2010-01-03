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
package com.javahotel.nmvc.persist.dict;

import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class CallSuccess {

    private CallSuccess() {
    }

    static void callI(IPersistResult res, int action, AbstractTo a, Object arg) {
        IPersistResult.PersistResultContext re =
                new IPersistResult.PersistResultContext(action, a,
                (ReturnPersist) arg);
        res.success(re);
    }
}
