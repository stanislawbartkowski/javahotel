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
package com.jythonui.server;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.python.core.Py;
import org.python.core.PyBoolean;
import org.python.core.PyDictionary;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
import org.python.core.PyIterator;
import org.python.core.PyList;
import org.python.core.PyLong;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;

import com.gwtmodel.table.common.TT;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.JythonUIFatal;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

/**
 * @author hotel
 * 
 */
class RunJython {

    static final private Logger log = Logger.getLogger(RunJython.class
            .getName());

    static private void putDebug(String mess) {
        log.log(Level.FINE, mess);
    }

    private static final PyObject JLISTMAP = toString(ICommonConsts.JLISTMAP);

    static private void error(String mess) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess);
    }

    private static Map<PyObject, PyObject> toPythonMap(DialogVariables v) {
        Map<PyObject, PyObject> m = new HashMap<PyObject, PyObject>();
        for (String s : v.getFields()) {
            FieldValue val = v.getValue(s);
            PyObject valP = null;
            if (val.getValue() == null) {
                valP = Py.None;
            } else
                switch (val.getType()) {
                case STRING:
                    String valS = val.getValueS();
                    if (valS == null) {
                        error(s + " string value cannot be null");
                    }
                    valP = new PyString(valS);
                    break;
                case BOOLEAN:
                    Boolean b = val.getValueB();
                    if (b == null) {
                        error(s + " boolean type but not null value expected");
                    }
                    valP = new PyBoolean(b.booleanValue());
                    break;
                case INT:
                    Integer i = val.getValueI();
                    valP = new PyInteger(i);
                    break;
                case LONG:
                    Long l = val.getValueL();
                    valP = new PyLong(l);
                    break;
                case BIGDECIMAL:
                    BigDecimal bi = val.getValueBD();
                    valP = new PyFloat(bi.doubleValue());
                    break;
                case DATE:
                    java.sql.Date dt = new java.sql.Date(val.getValueD()
                            .getTime());
                    PyObject da = Py.newDate(dt);
                    valP = da;
                    break;
                case DATETIME:
                    PyObject ti = Py.newDatetime(val.getValueT());
                    valP = ti;
                    break;
                default:
                    error(s + " type:" + val.getType().toString()
                            + " not implemented yet");
                    break;
                }
            m.put(toString(s), valP);

        }
        return m;
    }

    private static DialogVariables toVariables(RowIndex rI, RowContent row) {
        DialogVariables v = new DialogVariables();
        for (int i = 0; i < rI.rowSize(); i++) {
            FieldItem f = rI.getI(i);
            v.setValue(f.getId(), row.getRow(i));
        }
        return v;
    }

    private static PyObject toString(String s) {
        return new PyString(s);
    }

    private static void addListToMap(Map<PyObject, PyObject> pMap,
            DialogFormat d, DialogVariables v) {
        Map<String, ListOfRows> rowList = v.getRowList();
        if (rowList.isEmpty()) {
            return;
        }
        Map<PyObject, PyObject> m = new HashMap<PyObject, PyObject>();
        Iterator<Entry<String, ListOfRows>> iter = rowList.entrySet()
                .iterator();
        while (iter.hasNext()) {
            Entry<String, ListOfRows> e = iter.next();
            Collection<PyDictionary> c = new ArrayList<PyDictionary>();
            ListFormat fList = d.findList(e.getKey());
            if (fList == null) {
                error(e.getKey() + " list not found in dialog definition");
            }
            RowIndex rI = new RowIndex(fList.getColumns());
            for (RowContent row : e.getValue().getRowList()) {
                DialogVariables var = toVariables(rI, row);
                Map<PyObject, PyObject> ma = toPythonMap(var);
                PyDictionary pMa = new PyDictionary(ma);
                c.add(pMa);
            }
            PyList pList = new PyList(c);
            m.put(toString(e.getKey()), pList);
        }
        PyDictionary pyMap = new PyDictionary(m);
        pMap.put(JLISTMAP, pyMap);
    }

    private static void extractList(PyDictionary pyMap, DialogVariables vOut,
            DialogFormat d) {
        PyObject item = pyMap.iteritems();
        PyIterator iter = (PyIterator) item;
        PyObject next;
        while ((next = iter.__iternext__()) != null) {
            PyTuple tu = (PyTuple) next;
            Object key = tu.get(0);
            Object val = tu.get(1);
            String listId = (String) key;
            PyList pList = (PyList) val;
            if (d.getListList() == null) {
                error(listId + " dialog " + d.getId()
                        + " does not contain any list definition");
            }
            ListFormat lForm = d.findList(listId);
            if (lForm == null) {
                error(ICommonConsts.JLISTMAP + " cannot find list: " + listId);
            }
            RowIndex rI = new RowIndex(lForm.getColumns());
            ListOfRows lRows = new ListOfRows();
            @SuppressWarnings("rawtypes")
            ListIterator i = pList.listIterator();
            while (i.hasNext()) {
                Object e = i.next();
                PyDictionary vMap = (PyDictionary) e;
                DialogVariables v = new DialogVariables();
                toDialogVariables(lForm.getColumns(), v, vMap);
                RowContent row = rI.constructRow();
                for (String s : v.getFields()) {
                    FieldValue valF = v.getValue(s);
                    rI.setRowField(row, s, valF);
                }
                lRows.addRow(row);
            }
            vOut.setRowList(listId, lRows);
        }

    }

    private static void toDialogVariables(List<FieldItem> fList,
            DialogVariables v, PyDictionary pyMap) {
        PyObject item = pyMap.iteritems();
        PyIterator iter = (PyIterator) item;
        PyObject next;
        while ((next = iter.__iternext__()) != null) {
            PyTuple tu = (PyTuple) next;
            Object key = tu.get(0);
            Object val = tu.get(1);
            if (key.equals(ICommonConsts.JLISTMAP)) {
                continue;
            }

            String keyS = (String) key;
            TT fType = null;
            FieldValue valFF = v.getValue(keyS);
            FieldItem ffItem = null;
            if (fList != null)
                ffItem = DialogFormat.findE(fList, keyS);
            int afterdot = ICommonConsts.DEFAULTAFTERDOT;
            if (valFF != null) {
                fType = valFF.getType();
                afterdot = valFF.getAfterdot();
            } else {
                if (ffItem != null) {
                    fType = ffItem.getFieldType();
                    afterdot = ffItem.getAfterDot();
                }
            }
            FieldValue f = new FieldValue();
            if (fType != null) {
                if (val == null) {
                    f.setValue(fType, null, afterdot);
                } else
                    switch (fType) {
                    case STRING:
                        f.setValue((String) val);
                        break;
                    case BOOLEAN:
                        f.setValue((Boolean) val);
                        break;
                    case INT:
                        f.setValue((Integer) val);
                        break;
                    case LONG:
                        Long lV = null;
                        if (val instanceof Integer) {
                            lV = new Long((Integer) val);
                        } else if (val instanceof BigInteger) {
                            BigInteger bi = (BigInteger) val;
                            lV = bi.longValue();
                        } else {
                            error(keyS + " expected Integer or BigInteger here");
                        }
                        f.setValue(lV);
                        break;
                    case BIGDECIMAL:
                        BigDecimal bV = null;
                        if (val instanceof Integer) {
                            bV = new BigDecimal((Integer) val);
                        } else if (val instanceof BigInteger) {
                            bV = new BigDecimal((BigInteger) val);
                        } else if (val instanceof Double) {
                            bV = new BigDecimal((Double) val);
                        } else {
                            error(keyS + " expected Integer or BigInteger here");
                        }
                        BigDecimal bx = bV.setScale(afterdot,
                                BigDecimal.ROUND_HALF_UP);
                        f.setValue(bx, afterdot);
                        break;
                    case DATE:
                        java.sql.Date dt = (java.sql.Date) val;
                        Date dat = new Date(dt.getTime());
                        f.setValue(dat);
                        break;
                    case DATETIME:
                        Timestamp ti;
                        if (val instanceof java.sql.Date) {
                            java.sql.Date dti = (java.sql.Date) val;
                            ti = new Timestamp(dti.getTime());
                        } else {
                            ti = (Timestamp) val;
                        }
                        f.setValue(ti);
                        break;
                    default:
                        error(keyS + " type:" + fType.toString()
                                + " not implemented yet");
                    }
            } else {
                if (val instanceof String) {
                    String valS = (String) val;
                    f.setValue(valS);
                } else if (val instanceof Boolean) {
                    Boolean valB = (Boolean) val;
                    f.setValue(valB);
                } else if (val instanceof Integer) {
                    Integer valI = (Integer) val;
                    f.setValue(valI);
                } else if (val instanceof Long) {
                    Long valL = (Long) val;
                    f.setValue(valL);
                } else {
                    error(keyS
                            + " the type for this variable is not implemented");
                }
            }
            v.setValue(keyS, f);
        }
    }

    static void executeJython(IJythonUIServerProperties p, MCached mCached,
            DialogVariables v, DialogFormat d, String actionId) {
        System.getProperties().remove("file.encoding");
        String importJ = d.getJythonImport();
        putDebug("import jython = " + importJ);
        String methodJ = d.getJythonMethod();
        putDebug("method jython = " + methodJ);
        if (methodJ == null) {
            error("methodJ is null - null pointer expected");
        }

        PythonInterpreter interp;
        if (mCached.isCached()) {
            // Checked by experience that default PythonIntepreter constructor
            // keeps compiled packages, so we cannot modify jython source code
            // dynamically, server restart is necessary
            // TODO: check in docs if it is expected behavior or side effect
            interp = new PythonInterpreter();
        } else {
            // Constructor with parameters forces package recompiling
            // but there is a performance penalty
            interp = new PythonInterpreter(null, new PySystemState());
        }
        interp.cleanup();
        URL ur = p.getPackageDirectory();
        String sRe = ur.getFile();
        interp.exec("import sys");
        // interp.exec("print sys.path");
        putDebug("append sys.path = " + sRe);
        interp.exec("sys.path.append('" + sRe + "')");
        if (importJ != null) {
            interp.exec(importJ);
        }
        // interp.exec("from testpack import testclass");
        Map<PyObject, PyObject> pMap = toPythonMap(v);
        addListToMap(pMap, d, v);
        PyDictionary pyMap = new PyDictionary(pMap);

        interp.set("GG", pyMap);
        interp.set("AA", actionId);

        String s = MessageFormat.format(methodJ, "AA", "GG");
        putDebug(s);
        interp.exec(s);
        toDialogVariables(d.getFieldList(), v, pyMap);
        if (pyMap.has_key(JLISTMAP)) {
            PyObject o = pyMap.__getitem__(JLISTMAP);
            extractList((PyDictionary) o, v, d);
        }

    }

}
