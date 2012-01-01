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
package com.javahotel.dbutil.prop;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import com.javahotel.dbutil.log.GetLogger;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GetProperties {

    private final String LOCALPROP;
    private final String FILEPROP;
    private final String EMBPROPNAME;

    public GetProperties(final String lP, final String fP, final String eN) {

        this.LOCALPROP = lP;
        this.FILEPROP = fP;
        this.EMBPROPNAME = eN;
    }

    class ReadTestFilePers implements IGetPropertiesFactory {

        private final String propFile;

        ReadTestFilePers(final String p) {
            propFile = p;
        }

        public Map<String, String> getPersistProperties(GetLogger log) {
            try {
                FileInputStream f = null;
                String msg = MessageFormat.format("Read external file with properties {0}", propFile);
                log.getL().info(msg);
                f = new FileInputStream(propFile);
                Properties pro = new Properties();

                pro.load(f);
                return PropertiesToMap.toM(pro,null);
            } catch (IOException ex) {
                log.getL().log(Level.SEVERE,"",ex);
                return null;
            }
        }
    }

    class ReadTestPers implements IGetPropertiesFactory {

        public Map<String, String> getPersistProperties(GetLogger log) {
            String pName = "META-INF/" + EMBPROPNAME + ".properties";
            log.getL().info("Read embedded properties " + pName);
            return ReadProperties.getProperties(pName, log);
        }
    }

    public IGetPropertiesFactory getPropFactory(GetLogger log) throws Exception {

        boolean embedded = GetParam.getContextBoolean(LOCALPROP, false, true);
        if (embedded) {
            log.getL().info("Use embedded properties.");
            return new ReadTestPers();
        } else {
            String fileName = GetParam.getContextString(FILEPROP);
            log.getL().info("Use external file " + fileName);
            return new ReadTestFilePers(fileName);
        }
    }
}

