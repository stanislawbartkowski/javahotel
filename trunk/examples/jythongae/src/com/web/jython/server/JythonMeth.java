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
package com.web.jython.server;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyStringMap;
import org.python.util.PythonInterpreter;

/**
 * @author hotel
 * 
 */
class JythonMeth {

    static final private Logger log = Logger.getLogger(JythonMeth.class
            .getName());

    static void putMessage(String mess) {
        Date d = new Date();
        log.info(d.toGMTString() + ":" + mess);
    }

    static String getrVal() {
        putMessage("Moment 1");
        String encoding = System.getProperty("file.encoding");
        putMessage("Encoding:" + encoding);
        System.getProperties().remove("file.encoding");
        PythonInterpreter interp = new PythonInterpreter();
        URL ur = JythonMeth.class.getClassLoader().getResource("resource");
        String sRe = ur.getFile();

        interp.exec("import sys");
        interp.exec("print sys.path");
        interp.exec("sys.path.append('" + sRe + "')");

        interp.exec("import sys");
        interp.exec("print sys.path");

        putMessage("Moment 2");
        interp.exec("import mypack");
        putMessage("Moment 3");

        interp.exec("from mypack import myprint");
        putMessage("Moment 4");
        interp.exec("myprint.myprint()");
        putMessage("Moment 5");
        interp.exec("GG = myprint.getVal()");
        PyObject sy = interp.get("GG");
        PyString u = (PyString) sy;
        String ss = u.getString();
        putMessage(ss);

        Map<Object, PyObject> m = new HashMap<Object, PyObject>();
        PyObject keyS = new PyString("value1");
        m.put("key1", keyS);
        keyS = new PyString("value2");
        m.put("key2", keyS);
        keyS = new PyString("value3");
        m.put("key3", keyS);
        PyStringMap pyMap = new PyStringMap(m);
        interp.set("GG", pyMap);
        interp.exec("myprint.myprintMap(GG)");

        return ss;
    }

}
