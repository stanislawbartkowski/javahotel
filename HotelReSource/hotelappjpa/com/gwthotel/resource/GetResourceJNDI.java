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
package com.gwthotel.resource;

import com.jythonui.server.envvar.IGetResourceJNDI;

public class GetResourceJNDI implements IGetResourceJNDI {

    private final static String RESOURCEDIR = "hotelapp/resource";
    private final static String CACHED = "hotelapp/cached";
    private final static String EJBHOST = "hotelapp/ejbhost";
    private final static String EJBPORT = "hotelapp/ejbport";


    @Override
    public String getResourceDir() {
        return RESOURCEDIR;
    }

    @Override
    public String getCachedValue() {
        return CACHED;
    }

    @Override
    public String getEJBHost() {
        return EJBHOST;
    }

    @Override
    public String getEJBPort() {
        return EJBPORT;
    }

    @Override
    public String getMailName() {
        return null;
    }

}
