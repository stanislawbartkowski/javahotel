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
package com.javahotel.db.start;

import com.javahotel.dbjpa.ejb3.JpaManagerData;
import com.javahotel.dbres.log.HLog;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Stateful
public class StartBean implements IStart {

    public void start() {
        HLog.getL().getL().info("Start Bean");
    }

    @PreDestroy
    public void destroy() {
        HLog.getL().getL().info("Destroy, undeploy Bean");
        HLog.getL().close();
        JpaManagerData.clearAll();
    }
}
