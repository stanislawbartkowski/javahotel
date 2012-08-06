/*
 *  Copyright 2012 stanislawbartkowski@gmail.com
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
package com.gwtmodel.table;

import com.google.gwt.i18n.client.Constants;
import java.util.Map;

/**
 * 
 * @author hotel
 */
public interface TLabels extends Constants {

    String DateFormNotValid();

    String NumberNotValid();

    String From();

    String To();

    String Description();

    String NotFound();

    String NothingEntered();

    String AddNewRecord();

    String ChangeRecord();

    String SendingMail();

    String MailFrom();

    String SendingHeader();

    String SendingQuestion();

    String MailBox();

    String MailSubject();

    String MailContent();

    String MailTo();

    String EmptyFieldMessage();

    String SearchButton();

    String ClearParam();

    String SearchFromBeginning();

    String SearchNext();

    String Yes();

    String No();

    String ClearParametersQuestion();

    String SetFilter();

    String RemoveFilter();

    String Filter();

    String Accept();

    String Resign();

    String Choose();

    String Ok();

    String Print();

    String Remove();

    String DateLaterError();

    String Attachment();

    String DownloadAttachment();

    String ClockToDownload();

    String ChooseFromList();

    String YouResignStandard();

    String ChooseNow();

    Map<String, String> ActionName();

    String Download();

    String Send();

    String DateEqualError();

    String[] ScrollDays();

    String[] ScrollMonths();

    String GotoMonth();

    String BetweenFieldsRange();

    String EqualSign();

    String FiltrOnlyTable();

    String MenuForTable();

    String ChangeToTable();

    String ChangeToTree();
    
    String OnlyForTable();
    
    String OnlyForTree();
    
    String CannotDisplayAsTree();
    
    String CannotSwitchToTreeWhileFilter();

}