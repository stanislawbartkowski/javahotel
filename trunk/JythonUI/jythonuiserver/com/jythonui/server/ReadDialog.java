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
 */
class ReadDialog {

    static final private Logger log = Logger.getLogger(ReadDialog.class
            .getName());

    static private void errorLog(String mess) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess);
    }

    private static class MyHandler extends DefaultHandler {

        private DialogFormat dFormat = null;
        private final String[] dialogTag = { ICommonConsts.BEFORE,
                ICommonConsts.DISPLAYNAME, ICommonConsts.IMPORT,
                ICommonConsts.METHOD, ICommonConsts.PARENT };
        private final String[] buttonTag = { ICommonConsts.ID,
                ICommonConsts.DISPLAYNAME };
        private final String[] fieldTag = { ICommonConsts.ID,
                ICommonConsts.TYPE, ICommonConsts.AFTERDOT,
                ICommonConsts.DISPLAYNAME, ICommonConsts.NOTEMPTY,
                ICommonConsts.READONLY, ICommonConsts.HIDDEN,
                ICommonConsts.READONLYADD, ICommonConsts.READONLYCHANGE };
        private final String[] listTag = { ICommonConsts.ID,
                ICommonConsts.DISPLAYNAME, ICommonConsts.ELEMFORMAT };
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
            if (qName.equals(ICommonConsts.LEFTMENU)) {
                bList = new ArrayList<ButtonItem>();
                return;
            }
            if (qName.equals(ICommonConsts.FORM)) {
                fList = new ArrayList<FieldItem>();
                return;
            }
            if (qName.equals(ICommonConsts.BUTTON)) {
                bDescr = new ButtonItem();
                currentT = buttonTag;
                getAttribute = true;
                // pass to getting attributes (no return)
            }
            if (qName.equals(ICommonConsts.COLUMNS)) {
                ListFormat li = (ListFormat) bDescr;
                li.setColumns(fList);
                List<ListFormat> foList = dFormat.getListList();
                if (foList == null) {
                    foList = new ArrayList<ListFormat>();
                    dFormat.setListList(foList);
                }
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
                for (int i = 0; i < attributes.getLength(); i++) {
                    String key = attributes.getQName(i);
                    for (int k = 0; k < currentT.length; k++)
                        if (key.equals(currentT[k])) {
                            String val = attributes.getValue(i);
                            bDescr.setAttr(key, val);
                            break;
                        }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (dFormat == null) {
                return;
            }
            if (qName.equals(ICommonConsts.BUTTON)) {
                ButtonItem bI = (ButtonItem) bDescr;
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
                dFormat.setLeftButtonList(bList);
                bList = null;
                return;
            }
            if (qName.equals(ICommonConsts.FORM)) {
                dFormat.setActionList(fList);
                fList = null;
                return;
            }
            if (bDescr != null) {
                for (int i = 0; i < currentT.length; i++) {
                    if (currentT[i].equals(qName)) {
                        bDescr.setAttr(qName, buf.toString());
                        return;
                    }
                }
            }
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
