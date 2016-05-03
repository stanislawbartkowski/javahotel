/*
 *  Copyright 2016 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.injector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.gwtmodel.table.TLogMessages;
import com.gwtmodel.table.Utils;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author hotel
 */
public class LogT {

    private LogT() {
    }

    private static final Logger log = Logger.getLogger("gwt.gwtmodel.table");
    private static final Logger logS = Logger
            .getLogger("gwt.gwtmodel.table.slot");
    private static final Logger logT = Logger
            .getLogger("gwt.gwtmodel.table.listtable");
    private final static TLogMessages sMess;

    static {
        // TODO: log off
        logS.setLevel(Level.OFF);

    }

    public interface StrongTemplateCell extends SafeHtmlTemplates {

        @Template("<strong>{0}</strong>")
        SafeHtml input(String suma);
    }

    private final static StrongTemplateCell templateClass = GWT
            .create(StrongTemplateCell.class);

    public static StrongTemplateCell getStrongCell() {
        return templateClass;
    }

    static {
        sMess = (TLogMessages) GWT.create(TLogMessages.class);
    }

    public static Logger getL() {
        return log;
    }

    public static Logger getLS() {
        return logS;
    }

    public static Logger getLT() {
        return logT;
    }

    public static TLogMessages getT() {
        return sMess;
    }

    public static void errorLogE(String info, Exception e) {
        log.log(Level.SEVERE, info, e);
        Utils.errAlert(info, e);
    }
}
