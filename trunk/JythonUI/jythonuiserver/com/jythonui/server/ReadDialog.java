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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.ElemDescription;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.JythonUIFatal;
import com.jythonui.shared.ListFormat;

/**
 * @author hotel
 * 
 *         Reads XML with dialog description and return DialogFormat class
 */
class ReadDialog {

    /** Logger. */
    static final private Logger log = Logger.getLogger(ReadDialog.class
            .getName());

    /**
     * Logs error message and throws unchecked exception at the same time.
     * 
     * @param mess
     *            Error message
     */
    static private void errorLog(String mess) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess);
    }

    /**
     * @author hotel
     * 
     *         SAX handler
     */
    private static class MyHandler extends DefaultHandler {

        /** DialogFormat class being built. */
        private DialogFormat dFormat = null;

        /** Tags recognized for a particular element. */
        /* It duplicated to some extend xsd schema which also forces XML format. */
        private final String[] dialogTag = { ICommonConsts.BEFORE,
                ICommonConsts.DISPLAYNAME, ICommonConsts.IMPORT,
                ICommonConsts.METHOD, ICommonConsts.PARENT, ICommonConsts.TYPES };
        private final String[] buttonTag = { ICommonConsts.ID,
                ICommonConsts.DISPLAYNAME, ICommonConsts.ACTIONTYPE,
                ICommonConsts.ACTIONPARAM, ICommonConsts.ACTIONPARAM1,
                ICommonConsts.IMPORT, ICommonConsts.METHOD };
        private final String[] fieldTag = { ICommonConsts.ID,
                ICommonConsts.TYPE, ICommonConsts.AFTERDOT,
                ICommonConsts.ACTIONID, ICommonConsts.DISPLAYNAME,
                ICommonConsts.NOTEMPTY, ICommonConsts.READONLY,
                ICommonConsts.HIDDEN, ICommonConsts.READONLYADD,
                ICommonConsts.READONLYCHANGE, ICommonConsts.SIGNALCHANGE,
                ICommonConsts.HELPER, ICommonConsts.HELPERREFRESH };
        private final String[] listTag = { ICommonConsts.ID,
                ICommonConsts.DISPLAYNAME, ICommonConsts.ELEMFORMAT,
                ICommonConsts.STANDBUTT };
        /** Currently recognized set of tags. */
        /*
         * Important: it is assumed that tags describing element goes first
         * before subelements.
         */
        /* This invariant is enforced by xsd schema. */
        private final String[] allowedActions = { ICommonConsts.JMAINDIALOG,
                ICommonConsts.JUPDIALOG, ICommonConsts.JCLOSEDIALOG,
                ICommonConsts.JOKMESSAGE, ICommonConsts.JERRORMESSAGE,
                ICommonConsts.JYESNOMESSAGE };
        private String[] currentT;
        private StringBuffer buf;
        private ElemDescription bDescr = null;
        private List<ButtonItem> bList = null;
        private List<FieldItem> fList = null;

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            buf = new StringBuffer();
            boolean getAttribute = false;
            if (qName.equals(ICommonConsts.DIALOG)) {
                dFormat = new DialogFormat();
                bDescr = dFormat;
                currentT = dialogTag;
                getAttribute = true;
            }
            if (dFormat == null) {
                return;
            }
            if (qName.equals(ICommonConsts.LIST)) {
                currentT = listTag;
                bDescr = new ListFormat();
                fList = new ArrayList<FieldItem>();
                getAttribute = true;
            }
            if (qName.equals(ICommonConsts.LEFTMENU)
                    || qName.equals(ICommonConsts.BUTTONS)
                    || qName.equals(ICommonConsts.ACTIONS)) {
                bList = new ArrayList<ButtonItem>();
                return;
            }
            if (qName.equals(ICommonConsts.FORM)) {
                fList = new ArrayList<FieldItem>();
                return;
            }
            if (qName.equals(ICommonConsts.BUTTON)
                    || qName.equals(ICommonConsts.ACTION)) {
                bDescr = new ButtonItem();
                currentT = buttonTag;
                getAttribute = true;
                // pass to getting attributes (no return)
            }
            if (qName.equals(ICommonConsts.COLUMNS)) {
                ListFormat li = (ListFormat) bDescr;
                List<ListFormat> foList = dFormat.getListList();
                foList.add(li);
                return;
            }
            if (qName.equals(ICommonConsts.FIELD)
                    || qName.equals(ICommonConsts.COLUMN)) {
                bDescr = new FieldItem();
                currentT = fieldTag;
                getAttribute = true;
                // pass to getting attributes (no return)
            }
            if (getAttribute && bDescr != null) {
                SaxUtil.readAttr(bDescr, attributes, currentT);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (dFormat == null) {
                return;
            }
            if (qName.equals(ICommonConsts.BUTTON)
                    || qName.equals(ICommonConsts.ACTION)) {
                ButtonItem bI = (ButtonItem) bDescr;
                if (bI.isAction()) {
                    boolean found = false;
                    for (int i = 0; i < allowedActions.length; i++) {
                        if (bI.getAction().equals(allowedActions[i])) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        // prepared error message
                        String list = null;
                        for (int i = 0; i < allowedActions.length; i++) {
                            if (list == null)
                                list = allowedActions[i];
                            else
                                list = list + " , " + allowedActions[i];
                        } // for
                        errorLog(bI.getAction()
                                + " unrecognized action. Expected action names: "
                                + list);
                    }
                }
                if (bList != null) {
                    bList.add(bI);
                }
                return;
            }
            if (qName.equals(ICommonConsts.FIELD)
                    || qName.equals(ICommonConsts.COLUMN)) {
                FieldItem aI = (FieldItem) bDescr;
                if (aI.getId() == null) {
                    errorLog(ICommonConsts.FIELD + " empty " + ICommonConsts.ID
                            + " value");
                }
                if (fList != null) {
                    fList.add(aI);
                }
                return;
            }
            if (qName.equals(ICommonConsts.LEFTMENU)) {
                dFormat.getLeftButtonList().addAll(bList);
                bList = null;
                return;
            }
            if (qName.equals(ICommonConsts.BUTTONS)) {
                dFormat.getButtonList().addAll(bList);
                bList = null;
                return;
            }
            if (qName.equals(ICommonConsts.ACTIONS)) {
                dFormat.getActionList().addAll(bList);
                bList = null;
                return;
            }
            if (qName.equals(ICommonConsts.COLUMNS)) {
                ListFormat li = dFormat.getListList().get(dFormat.getListList().size()-1);
                li.getColumns().addAll(fList);
                return;
            }

            if (qName.equals(ICommonConsts.FORM)) {
                dFormat.getFieldList().addAll(fList);
                fList = null;
                return;
            }

            SaxUtil.readVal(bDescr, qName, currentT, buf);
        }

        @Override
        public void characters(char ch[], int start, int length)
                throws SAXException {
            buf.append(ch, start, length);
        }

    }

    static DialogFormat parseDocument(InputStream sou)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        saxParser = factory.newSAXParser();
        MyHandler ma = new MyHandler();
        saxParser.parse(sou, ma);
        return ma.dFormat;
    }

}
