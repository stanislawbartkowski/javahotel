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
package com.javahotel.dbres.entityconstr;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.javahotel.dbres.resources.GetProp;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.dbutil.log.GetLogger;

public class EntityConstrFactory {

    private EntityConstrFactory() {
    }

    private static EntityConstrData createE() throws SAXException, IOException {
        EntityConstrData da = new EntityConstrData();
        InputStream i = GetProp.getXMLFile(IMess.CONTRXMLDEF);
        XMLReader xr = XMLReaderFactory.createXMLReader();
        ConstrHandler co = new ConstrHandler(da);
        xr.setContentHandler(co);
        xr.parse(new InputSource(i));
        return da;
    }

    public static IEntityConstr createEntityConstr(GetLogger log) {
        try {
            return createE();
        } catch (SAXException e) {
            log.getL().log(Level.SEVERE, "", e);
        } catch (IOException e) {
            log.getL().log(Level.SEVERE, "", e);
        }
        return null;
    }

}
