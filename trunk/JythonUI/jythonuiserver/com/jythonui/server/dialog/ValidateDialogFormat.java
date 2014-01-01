/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.dialog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.CheckList;
import com.jythonui.shared.DateLine;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.ElemDescription;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.JythonUIFatal;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.TabPanel;

/**
 * @author hotel
 * 
 */
class ValidateDialogFormat {

    static final private Logger log = Logger
            .getLogger(ValidateDialogFormat.class.getName());

    static private void error(String mess) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess);
    }

    private ValidateDialogFormat() {
    }

    private static void idNotNull(String tag, ElemDescription e) {
        if (CUtil.EmptyS(e.getId())) {
            error(SHolder.getM().getMess(IErrorCode.ERRORCODE25,
                    ILogMess.CANNOTBEEMPTY, tag));
        }
    }

    private static <T extends ElemDescription> void validateL(String lTag,
            String eTag, List<T> eList) {
        Set<String> sId = new HashSet<String>();
        for (ElemDescription e : eList) {
            idNotNull(lTag + " " + eTag, e);
            String id = e.getId();
            if (sId.contains(id))
                error(SHolder.getM().getMess(IErrorCode.ERRORCODE26,
                        ILogMess.TAGVALUEDUPLICATED, lTag, eTag,
                        ICommonConsts.ID, id));

            sId.add(id);
        }
    }

    private static void tagExists(DialogFormat d, ElemDescription e,
            String tag, String... tagList) {
        for (String s : tagList) {
            String val = e.getAttr(s);
            if (CUtil.EmptyS(val))
                error(SHolder.getM().getMess(IErrorCode.ERRORCODE56,
                        ILogMess.TAGCANNOTBEEMPTY, d.getId(), tag, s));

        }
    }

    private static <T extends ElemDescription> void findTag(DialogFormat d,
            ElemDescription e, String findTag, String listTag, List<T> eList) {
        String id = e.getAttr(findTag);
        if (DialogFormat.findE(eList, id) != null)
            return;
        error(SHolder.getM().getMess(IErrorCode.ERRORCODE57,
                ILogMess.DIALOGCANNOTFINDINLIST, d.getId(), findTag, id,
                listTag));
    }

    static void validate(DialogFormat d) {
        validateL(ICommonConsts.LEFTMENU, ICommonConsts.BUTTON,
                d.getLeftButtonList());
        validateL(ICommonConsts.CHECKLIST, ICommonConsts.CHECKLIST,
                d.getCheckList());
        validateL(ICommonConsts.FORM, ICommonConsts.FIELD, d.getFieldList());
        for (ListFormat l : d.getListList()) {
            idNotNull(ICommonConsts.LIST, l);
            validateL(ICommonConsts.COLUMNS, ICommonConsts.COLUMN,
                    l.getColumns());
        }
        Set<String> sId = new HashSet<String>();
        for (FieldItem f : d.getFieldList()) {
            sId.add(f.getId());
        }
        for (ListFormat l : d.getListList()) {
            String id = l.getId();
            if (sId.contains(id))
                error(SHolder.getM().getMess(IErrorCode.ERRORCODE27,
                        ILogMess.TAGDUPLICATEDITENDTIFIER, l.getId(),
                        ICommonConsts.FIELD, id));
            // check for editable, boolean and signalbefore
            for (FieldItem f : l.getColumns()) {
                if (f.isColumnEditable() && f.getFieldType() == TT.BOOLEAN
                        && f.isSignalBefore()) {
                    error(SHolder.getM().getMess(IErrorCode.ERRORCODE71,
                            ILogMess.CANNOTEDITBOOLEANBEFORE, d.getId(),
                            f.getId(), ICommonConsts.SIGNALBEFORE));
                }
            }
            sId.add(id);
        }
        for (CheckList c : d.getCheckList()) {
            idNotNull(ICommonConsts.CHECKLIST, c);
            String type = c.getAttr(ICommonConsts.TYPE);
            if (CUtil.EmptyS(type))
                continue;
            if (type.equals(ICommonConsts.BOOLTYPE))
                continue;
            if (type.equals(ICommonConsts.DECIMALTYPE))
                continue;
            String errMess = SHolder.getM().getMess(IErrorCode.ERRORCODE45,
                    ILogMess.UNEXPECTEDCHECKLISTTYPE, c.getId(), type,
                    ICommonConsts.BOOLTYPE, ICommonConsts.DATETIMETYPE);
            error(errMess);
        }
        for (DateLine dl : d.getDatelineList()) {
            idNotNull(ICommonConsts.DATELINE, dl);
            validateL(ICommonConsts.COLUMNS, ICommonConsts.COLUMN,
                    dl.getColList());
            String dId = dl.getListId();
            if (CUtil.EmptyS(dId)) {
                error(SHolder.getM().getMess(IErrorCode.ERRORCODE51,
                        ILogMess.DATELINEIDCANNOTBEEMPTY, d.getId(),
                        ICommonConsts.DATELINE, ICommonConsts.DATELINEID));
            }
            FieldItem f = DialogFormat.findE(dl.getColList(), dId);
            if (f == null)
                error(SHolder.getM().getMess(IErrorCode.ERRORCODE52,
                        ILogMess.DATELINEIDCANNOTBEFOUND, d.getId(),
                        ICommonConsts.DATELINE, ICommonConsts.DATELINEID, dId,
                        ICommonConsts.COLUMNS));
            tagExists(d, dl, ICommonConsts.DATELINE,
                    ICommonConsts.DATELINEDEFAFILE);
            validateL(ICommonConsts.DATELINE, ICommonConsts.DATELINEFORMS,
                    dl.getFormList());
            findTag(d, dl, ICommonConsts.DATELINEDEFAFILE,
                    ICommonConsts.DATELINEFORMS, dl.getFormList());
        }
        for (TabPanel t : d.getTabList())
            validateL(ICommonConsts.TABPANEL, ICommonConsts.TABPANELELEM,
                    t.gettList());

    }
}
