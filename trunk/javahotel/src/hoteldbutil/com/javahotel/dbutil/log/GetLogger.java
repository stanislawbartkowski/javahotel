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
package com.javahotel.dbutil.log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetLogger {

    public GetLogger(final String logName) {
        log = Logger.getLogger(logName);
    }

    private final Logger log;

    public void disable() {
        log.setLevel(Level.OFF);
    }

    public void close() {
        Handler[] ha = log.getHandlers();
        for (int i = 0; i < ha.length; i++) {
            ha[i].flush();
        }
    }

    public Logger getL() {
        return log;
    }
}