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
package com.gwtmodel.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

public class FileUtil {

    public static String getFile(String name) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(name));
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder.toString();
    }

    public static File getResourceDir(Class cl) {
        String me = cl.getName().replace(".", "/") + ".class";
        URL u = cl.getClassLoader().getResource(me);
        String p = u.getFile();
        File f = new File(p);
        String s = f.getParent();
        return new File(s + File.separatorChar + "resources");
    }

    public static URL getResourceURL(Class cl, String pa) {
        URL u;
        u = cl.getClassLoader().getResource("resources/" + pa);
        return u;
    }

    public static Schema getSchema(Class cl, String pa)
            throws SAXException {
        URL u = getResourceURL(cl, pa + ".xsd");
        SchemaFactory fa = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema se = fa.newSchema(u);
        return se;
    }

    public static void validate(Class cl, String xmlName,
            String schemaName) throws SAXException, IOException {
        Schema se = getSchema(cl, schemaName);
        Validator validator = se.newValidator();
        validator.validate(new StreamSource(xmlName));

    }

    public static Map<String, String> PropertyToMap(Properties pro) {
        Map<String, String> map = new HashMap<String, String>();
        for (Entry<Object, Object> e : pro.entrySet()) {
            String key = (String) e.getKey();
            String val = (String) e.getValue();
            map.put(key, val);
        }
        return map;
    }

    public static Properties MapToProperty(Map<String, String> ma) {
        Properties prop = new Properties();
        for (Entry<String, String> e : ma.entrySet()) {
            prop.put(e.getKey(), e.getValue());
        }
        return prop;
    }

    private static boolean isFSep(char ch) {
        if (ch == '/') {
            return true;
        }
        if (ch == '\'') {
            return true;
        }
        return false;
    }

    public static String addNameToPath(String path, String fName) {
        StringBuilder bu = new StringBuilder(path);
        int la = bu.length() - 1;
        char ch = bu.charAt(la);
        if (!isFSep(ch)) {
            return path + File.separator + fName;
        }
        return path + fName;
    }
}
