/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gwtmodel.table.common.ConvertTT;
import com.jamesmurty.utils.XMLBuilder;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

public class XMLTransformer extends UtilHelper implements IXMLTransformer {

    private void errorL(String errId, String messId, Throwable e, String... par) {
        errorMess(gMess, errId, messId, (Exception) e, par);
    }

    private final IJythonUIServer iS;
    private final IGetLogMess gMess;

    @Inject
    public XMLTransformer(IJythonUIServer iS,
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess) {
        this.iS = iS;
        this.gMess = gMess;
    }

    private static XMLBuilder addValue(XMLBuilder builder, String id,
            FieldValue val) {
        String s = ConvertTT.toS(val.getValue(), val.getType(),
                val.getAfterdot());
        if (s == null)
            return builder;
        return builder.e(id).t(s).up();
    }

    private static FieldValue toFieldValue(FieldItem f, String s) {
        Object o = ConvertTT.toO(f.getFieldType(), s);
        FieldValue val = new FieldValue();
        val.setValue(f.getFieldType(), o, f.getAfterDot());
        return val;
    }

    @Override
    public String toXML(String dialogName, DialogVariables v) {
        DialogInfo dial = iS.findDialog(null, dialogName);
        if (dial == null) {
            errorL(IErrorCode.ERRORCODE61, ILogMess.XMLTRANSFORMERNODIALOG,
                    null, dialogName);
        }
        XMLBuilder builder = null;
        try {
            builder = XMLBuilder.create(ICommonConsts.XMLROOT).e(
                    ICommonConsts.FORM);
            for (FieldItem f : dial.getDialog().getFieldList()) {
                FieldValue val = v.getValue(f.getId());
                builder = addValue(builder, f.getId(), val);
            } // for
            builder = builder.up();
            for (ListFormat fo : dial.getDialog().getListList()) {
                ListOfRows ro = v.getList(fo.getId());
                if (ro == null)
                    continue;
                builder = builder.e(ICommonConsts.LIST).a(ICommonConsts.ID,
                        fo.getId());
                List<FieldItem> cols = fo.getColumns();
                for (RowContent row : ro.getRowList()) {
                    builder = builder.e(ICommonConsts.COLUMN);
                    for (int i = 0; i < cols.size(); i++) {
                        FieldValue val = row.getRow(i);
                        builder = addValue(builder, cols.get(i).getId(), val);
                    }
                    builder = builder.up();
                }
                builder = builder.up();
            }
            Properties outputProperties = new Properties();
            outputProperties.put(javax.xml.transform.OutputKeys.INDENT, "yes");
            return builder.root().asString(outputProperties);
        } catch (ParserConfigurationException | FactoryConfigurationError
                | TransformerException e) {
            errorL(IErrorCode.ERRORCODE62, ILogMess.XMLTRANSFORMERERROR, e,
                    dialogName);
        }
        return null;
    }

    private class MyHandler extends DefaultHandler {
        private final DialogFormat d;
        private final DialogVariables v;
        private StringBuffer buf;
        private boolean isFormNow;
        private String lineId;
        private ListOfRows lRows;
        private RowContent row;
        private ListFormat fo;
        private RowIndex rI;

        MyHandler(DialogFormat d, DialogVariables v) {
            this.d = d;
            this.v = v;
            isFormNow = false;
            lineId = null;
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            buf = new StringBuffer();
            if (qName.equals(ICommonConsts.FORM)) {
                isFormNow = true;
                return;
            }
            if (qName.equals(ICommonConsts.LIST)) {
                lineId = attributes.getValue(ICommonConsts.ID);
                if (lineId == null)
                    errorL(IErrorCode.ERRORCODE68,
                            ILogMess.XMLTRANSFORMPARSELISTIDNOTDEFINED, null,
                            d.getId(), ICommonConsts.LIST, ICommonConsts.ID);
                lRows = new ListOfRows();
                fo = d.findList(lineId);
                if (fo == null)
                    errorL(IErrorCode.ERRORCODE67,
                            ILogMess.XMLTRANSFORMPARSELISTNOTFOUNDERROR, null,
                            d.getId(), lineId);
                rI = new RowIndex(fo.getColumns());
                return;
            }
            if (lineId != null && qName.equals(ICommonConsts.COLUMN)) {
                row = new RowContent();
                row.setRowSize(fo.getColumns().size());
                return;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (qName.equals(ICommonConsts.FORM)) {
                isFormNow = false;
                return;
            }
            if (isFormNow) {
                String id = qName;
                FieldItem fItem = d.findFieldItem(id);
                if (fItem == null) {
                    errorL(IErrorCode.ERRORCODE65,
                            ILogMess.XMLTRANFORMREADERRORCANNOTFIND, null,
                            d.getId(), id);
                }
                FieldValue val = toFieldValue(fItem, buf.toString());
                v.setValue(id, val);
                return;
            }
            if (lineId != null && qName.equals(ICommonConsts.LIST)) {
                v.getRowList().put(lineId, lRows);
                lineId = null;
                return;
            }
            if (lineId != null && qName.equals(ICommonConsts.COLUMN)) {
                lRows.addRow(row);
                return;
            }
            if (lineId != null) {
                FieldItem f = fo.getColumn(qName);
                FieldValue val = toFieldValue(f, buf.toString());
                rI.setRowField(row, qName, val);
                return;
            }
        }

        @Override
        public void characters(char ch[], int start, int length)
                throws SAXException {
            buf.append(ch, start, length);
        }

    }

    @Override
    public void fromXML(String dialogName, DialogVariables v, String xml) {
        DialogInfo dial = iS.findDialog(null, dialogName);
        if (dial == null) {
            errorL(IErrorCode.ERRORCODE63, ILogMess.XMLTRANSFORMERNODIALOG,
                    null, dialogName);
        }
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        try {
            saxParser = factory.newSAXParser();
            InputSource sou = new InputSource(new StringReader(xml));
            MyHandler ha = new MyHandler(dial.getDialog(), v);
            saxParser.parse(sou, ha);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            errorL(IErrorCode.ERRORCODE64, ILogMess.XMLTRANSFORMERERROR, e,
                    dialogName);
        }

    }
}
