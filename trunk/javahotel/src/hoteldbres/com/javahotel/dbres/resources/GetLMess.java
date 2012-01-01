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
package com.javahotel.dbres.resources;

import com.javahotel.dbres.log.HLog;
import com.javahotel.dbutil.prop.ReadProperties;

import java.util.Map;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GetLMess {

    private static final Map<String, String> prop;
    private static final String PROP = IMess.RESOURCEFOLDER + "/logmess.properties";
    public static final String CANNOTREMOVE = "CANNOTREMOVE";
    public static final String CANNOTFINDPERSON = "CANNOTFINDPERSON";
    public static final String CANNOTFINDHOTEL = "CANNOTFINDHOTEL";
    

    static {
        prop = ReadProperties.getProperties(PROP, HLog.getL());
    }

    private GetLMess() {
    }

    public static String getM(final String mId) {
        return prop.get(mId);
    }
}
