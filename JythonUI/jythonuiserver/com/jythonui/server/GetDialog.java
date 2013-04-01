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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import com.gwtmodel.mapxml.VerifyXML;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.logmess.LogMess;
import com.jythonui.server.security.ISecurity;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.JythonUIFatal;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.SecurityInfo;
import com.jythonui.shared.TypesDescr;

/**
 * @author hotel
 * 
 */
class GetDialog {

    private GetDialog() {

    }

    private static final String RESOURCES = "resources";
    private static final String XSDDIR = "xsd";
    private static final String DIALOGXSD = "dialogschema.xsd";
    private static final String TYPESXSD = "typedefschema.xsd";

    static final private Logger log = Logger.getLogger(GetDialog.class
            .getName());

    static private void putDebug(String mess) {
        log.log(Level.FINE, mess);
    }

    static private void error(String mess) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess);
    }

    private static void logError(Throwable e) {
        log.log(Level.SEVERE, "", e);
        throw new JythonUIFatal(e);
    }

    static private URL getURLSchema(String schemaname) {
        putDebug("Search schema " + schemaname);
        URL ur = ReadDialog.class.getClassLoader().getResource(
                RESOURCES + "/" + XSDDIR + "/" + schemaname);
        if (ur == null) {
            error("Schema not found, null exception expected");
        }
        return ur;
    }

    static private InputStream getXML(IJythonUIServerProperties p, String name)
            throws FileNotFoundException {
        putDebug("Search dialog " + name);
        if (p.getDialogDirectory() == null) {
            error("DialogDirectory null, null exception expected");
        }
        String dDir = p.getDialogDirectory().getPath();
        dDir = dDir + "/" + name;
        putDebug(dDir);
        InputStream s = new FileInputStream(dDir);
        return s;
    }

    static DialogFormat getDialog(IJythonUIServerProperties p, MCached mCached,
            String token, String dialogName, boolean verify) {
        DialogFormat d;
        if (Holder.isAuth() && CUtil.EmptyS(token)) {
            log.severe(LogMess.getMess(IErrorCode.ERRORCODE8,
                    ILogMess.AUTOENABLEDNOTOKEN, dialogName));
            return null;
        }
        if (mCached.isCached()) {
            try {
                d = (DialogFormat) mCached.getC().get(dialogName);
                if (d != null) {
                    return d;
                }
                // this exception is expected if there is a cache entry
                // containing value from the previous version. In this case
                // simply remove the entry.
            } catch (InvalidClassException e) {
                mCached.getC().remove(dialogName);
            }
        }
        d = getDialogDirectly(p, mCached, dialogName, verify);
        String dParentName = d.getParent();
        if (dParentName != null) {
            DialogFormat dParent = getDialog(p, mCached, token,
                    dParentName, false);
            for (ListFormat lo : dParent.getListList()) {
                if (lo.getfElem() != null
                        && lo.getfElem().getId().equals(dialogName)) {
                    return lo.getfElem();
                }
            }
            error(dParentName + " is parent in the " + dialogName
                    + " but there is no such a dialog in the parent specified");
        }
        if (d != null)
            if (mCached.isCached()) {
                mCached.getC().put(dialogName, d);
            }
        return d;
    }

    private static DialogFormat getDialogDirectly(IJythonUIServerProperties p,
            MCached mCached, String dialogName, boolean verify) {
        DialogFormat d = null;
        try {
            URL u = getURLSchema(DIALOGXSD);
            InputStream sou;
            if (verify) {
                putDebug("Verify using xsd schema " + DIALOGXSD);
                sou = getXML(p, dialogName);
                VerifyXML.verify(u, new StreamSource(sou));
            }
            sou = getXML(p, dialogName);
            d = ReadDialog.parseDocument(sou);
            if (d != null) {
                d.setId(dialogName);
                if (verify) {
                    ValidateDialogFormat.validate(d);
                }
            }
            String typesName = d.getAttr(ICommonConsts.TYPES);
            if (!CUtil.EmptyS(typesName)) {
                if (verify) {
                    u = getURLSchema(TYPESXSD);
                    sou = getXML(p, typesName);
                    VerifyXML.verify(u, new StreamSource(sou));
                }
                sou = getXML(p, typesName);
                TypesDescr types = ReadTypes.parseDocument(sou);
                d.getTypeList().add(types);
            }
            // now check elemformat for lists
            if (d.getListList() != null) {
                for (ListFormat l : d.getListList()) {
                    if (l.getElemFormat() != null) {
                        // recursive
                        DialogFormat dElem = getDialogDirectly(p, mCached,
                                l.getElemFormat(), verify);
                        boolean wasmodified = false;
                        if (dElem.getFieldList().isEmpty()) {
                            // if there is no field list in the XML
                            // then copy parent column list
                            // dElem.setFieldList(l.getColumns());
                            dElem.getFieldList().addAll(l.getColumns());
                            putDebug(l.getElemFormat()
                                    + " copy list of columns from "
                                    + dialogName);
                            wasmodified = true;
                        }
                        String[] attrList = { ICommonConsts.IMPORT,
                                ICommonConsts.METHOD };
                        for (int i = 0; i < attrList.length; i++) {
                            String a = attrList[i];
                            if (dElem.getAttr(a) == null) {
                                wasmodified = true;
                                dElem.setAttr(a, d.getAttr(a));
                                putDebug(l.getElemFormat() + " copy attribute "
                                        + a + " from " + dialogName);
                            }
                        }
                        if (wasmodified) {
                            if (dElem.getParent() == null) {
                                error(dElem.getId() + " "
                                        + ICommonConsts.PARENT
                                        + " attribute expected");
                            }
                            if (!dElem.getParent().equals(dialogName)) {
                                error(dElem.getId() + " "
                                        + ICommonConsts.PARENT + " = "
                                        + dElem.getParent() + " " + dialogName
                                        + " expected");
                            }
                            // cache again with changes
                            if (mCached.isCached())
                                mCached.getC().put(dElem.getId(), dElem);
                        }
                        l.setfElem(dElem);
                    }
                }
            }

        } catch (SAXException e) {
            logError(e);
        } catch (IOException e) {
            logError(e);
        } catch (ParserConfigurationException e) {
            logError(e);
        }
        if (d == null) {
            error(dialogName + " not found ");
        } else {
            if (mCached.isCached()) {
                mCached.getC().put(dialogName, d);
            }
        }
        return d;

    }
}
