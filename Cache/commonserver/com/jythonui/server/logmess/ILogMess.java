/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.logmess;

public interface ILogMess {

	String CANNOTFINDRESOURCEFILE = "CANNOTFINDRESOURCEFILE";
	String ERRORWHILEREADINGRESOURCEFILE = "ERRORWHILEREADINGRESOURCEFILE";
	String REALMINITIALZATION = "REALMINITIALZATION";
	String AUTHENTICATEUSER = "AUTHENTICATEUSER";
	String AUTHENTICATENOUSER = "AUTHENTICATENOUSER";
	String AUTHENTICATEINCORECTPASSWORD = "AUTHENTICATEINCORECTPASSWORD";
	String AUTHENTOCATELOCKED = "AUTHENTOCATELOCKED";
	String AUTHENTICATEOTHERERROR = "AUTHENTICATEOTHERERROR";
	String OKAUTHENTICATED = "OKAUTHENTICATED";
	String LOGOUTSUCCESS = "LOGOUTSUCCESS";
	String AUTOENABLEDNOTOKEN = "AUTOENABLEDNOTOKEN";
	String ELEMDOESNOTMATCHPARENT = "ELEMDOESNOTMATCHPARENT";
	String JEXLERROR = "JEXLERROR";
	String JEXLERRORNOTBOOLEAN = "JEXLERRORNOTBOOLEAN";
	String JEXLERROREVALUATION = "JEXLERROREVALUATION";
	String DIALOGXMLPARSERROR = "DIALOGXMLPARSERROR";
	String DIALOGNOTFOUND = "DIALOGNOTFOUND";
	String SCHEMANOTFOUND = "SCHEMANOTFOUND";
	String DIALOGDIRECTORYNULL = "DIALOGDIRECTORYNULL";
	String PUTOBJECTREGISTRYERROR = "PUTOBJECTREGISTRYERROR";
	String GETOBJECTREGISTRYERROR = "GETOBJECTREGISTRYERROR";
	String PUTINCACHEAGAIN = "PUTINCACHEAGAIN";
	String INVALIDTOKEN = "INVALIDTOKEN";
	String CANNOTAUTHENTICATEAGAIN = "CANNOTAUTHENTICATEAGAIN";
	String CANNOTCASTENTRY = "CANNOTCASTENTRY";
	String CANNOTBEEMPTY = "CANNOTBEEMPTY";
	String TAGVALUEDUPLICATED = "TAGVALUEDUPLICATED";
	String TAGDUPLICATEDITENDTIFIER = "TAGDUPLICATEDITENDTIFIER";
	String COLUMNNOTDEFINED = "COLUMNNOTDEFINED";
	String TYPENOTIMPLEMENTED = "TYPENOTIMPLEMENTED";
	String VALUECANNOTBENULL = "VALUECANNOTBENULL";
	String CANNOTFINDCHECKLIST = "CANNOTFINDCHECKLIST";
	String TYPEMAPNOTIMPLEMENTS = "TYPEMAPNOTIMPLEMENTS";
	String LISTNOTFOUND = "LISTNOTFOUND";
	String STRINGVALUECANNOTBENULL = "STRINGVALUECANNOTBENULL";
	String BOOLEANVALUECANNOTBENULL = "BOOLEANVALUECANNOTBENULL";
	String EMPTYLISTDEFINITION = "EMPTYLISTDEFINITION";
	String INTEGERORBITINTEGEREXPECTED = "INTEGERORBITINTEGEREXPECTED";
	String METHODNOTDEFINED = "METHODNOTDEFINED";
	String ERRORWHILEREADINGCONTEXT = "ERRORWHILEREADINGCONTEXT";
	String CANNOTFINDRESOURCEVARIABLE = "CANNOTFINDRESOURCEVARIABLE";
	String ERRORWHILELOADINGBUNDLERESOURCE = "ERRORWHILELOADINGBUNDLERESOURCE";
	String UNEXPECTEDCHECKLISTTYPE = "UNEXPECTEDCHECKLISTTYPE";
	String UNRECOGNIZEDACTION = "UNRECOGNIZEDACTION";
	String EMPTYIDVALUE = "EMPTYIDVALUE";
	String DATELINENOTDEFINED = "DATELINENOTDEFINED";
	String SEQUENCEEXPECTED = "SEQUENCEEXPECTED";
	String DATELINEIDCANNOTBEEMPTY = "DATELINEIDCANNOTBEEMPTY";
	String DATELINEIDCANNOTBEFOUND = "DATELINEIDCANNOTBEFOUND";
	String JPATRANSACTIONEXCEPTION = "JPATRANSACTIONEXCEPTION";
	String FILENOTFOUND = "FILENOTFOUND";
	String FILEIOEXCEPTION = "FILEIOEXCEPTION";
	String TAGCANNOTBEEMPTY = "TAGCANNOTBEEMPTY";
	String DIALOGCANNOTFINDINLIST = "DIALOGCANNOTFINDINLIST";
	String DATELINEGETDATACTION = "DATELINEGETDATACTION";
	String XMLTRANSFORMERNODIALOG = "XMLTRANSFORMERNODIALOG";
	String XMLTRANSFORMERERROR = "XMLTRANSFORMERERROR";
	String XMLTRANFORMREADERRORCANNOTFIND = "XMLTRANFORMREADERRORCANNOTFIND";
	String XMLTRANSFORMPARSELISTNOTFOUNDERROR = "XMLTRANSFORMPARSELISTNOTFOUNDERROR";
	String XMLTRANSFORMPARSELISTIDNOTDEFINED = "XMLTRANSFORMPARSELISTIDNOTDEFINED";
	String XMLSETCONTENTBUTCONTENTNOTAVAILABLE = "XMLSETCONTENTBUTCONTENTNOTAVAILABLE";
	String ACIONIDCANNOTBENULL = "ACIONIDCANNOTBENULL";
	String CANNOTEDITBOOLEANBEFORE = "CANNOTEDITBOOLEANBEFORE";
	String SEMAPHOREEXCEPTION = "SEMAPHOREEXCEPTION";
	String SEMAPHOREEXPIRETIME = "SEMAPHOREEXPIRETIME";
	String SEMAPHOREWAIT = "SEMAPHOREWAIT";
	String DUPLICATEDREGISTRYENTRY = "DUPLICATEDREGISTRYENTRY";
	String ERRORWHILEREADINGXML = "ERRORWHILEREADINGXML";
	String PARENTFILEEXPECTED = "PARENTFILEEXPECTED";
	String PARENTCANNOTBETHESAME = "PARENTCANNOTBETHESAME";
	String STANDBUTTONNOTINACTION = "STANDBUTTONNOTINACTION";
	String ERRORWHILEUPLOADING = "ERRORWHILEUPLOADING";
	String DOWNLOADCANNOTFINDBLOB = "DOWNLOADCANNOTFINDBLOB";
	String ERRORDOWNLOADCANNOTFINDBLOB = "ERRORDOWNLOADCANNOTFINDBLOB";
	String LOOKFORENVVARIABLE = "LOOKFORENVVARIABLE";
	String ENVVARIABLENOTFOUND = "ENVVARIABLENOTFOUND";
	String ENVVARIABLEFOUND = "ENVVARIABLEFOUND";
	String XMLREADERROR = "XMLREADERROR";
	String DEFAULTINSTANCEHASBEENCREATED = "DEFAULTINSTANCEHASBEENCREATED";
	String INSTANCEIDCANNOTNENULLHERE = "INSTANCEIDCANNOTNENULLHERE";
	String CLEANALLADMIN = "CLEANALLADMIN";
	String INSTANCEBYIDCANNOTBEFOUND = "INSTANCEBYIDCANNOTBEFOUND";
	String OBJECTCANNOTBEFOUND = "OBJECTCANNOTBEFOUND";
	String OBJECTBYIDNOTFOUND = "OBJECTBYIDNOTFOUND";
	String PERSONNAMEISEXPECTEDBUTNOTFOUND = "PERSONNAMEISEXPECTEDBUTNOTFOUND";
	String PERSONIDISEXPECTEDBUTNOTFOUND = "PERSONIDISEXPECTEDBUTNOTFOUND";
	String INSTANCENOTFOUND = "INSTANCENOTFOUND";
	String CANNOTCUSTOMANDHELPER = "CANNOTCUSTOMANDHELPER";
	String JEXLERRORNOTSTRING = "JEXLERRORNOTSTRING";
	String EMPTYCHARTLISTDEFINITION = "EMPTYCHARTLISTDEFINITION";
	String CHARTNOTFOUND = "CHARTNOTFOUND";
	String AUTHUSERDOESNOTEXIST = "AUTHUSERDOESNOTEXIST";
	String AUTHHOTELISNULL = "AUTHHOTELISNULL";
	String AUTHCANNOTGETROLES = "AUTHCANNOTGETROLES";
	String AUTHUSERDOESNOTHAVEROLEINHOTEL = "AUTHUSERDOESNOTHAVEROLEINHOTEL";
	String MAILNOTDELIVERED = "MAILNOTDELIVERED";
	String MAILDELIVEREDONLYTOSOME = "MAILDELIVEREDONLYTOSOME";
	String MAILDELIVERERROR = "MAILDELIVERERROR";
	String MAILERRORPROPERTIES = "MAILERRORPROPERTIES";
	String ERRORWHILEREADINDADDPROPERTIES = "ERRORWHILEREADINDADDPROPERTIES";
	String ERRORWHILEREADMAILLIST = "ERRORWHILEREADMAILLIST";
	String ERRORINFILENAMETOURL = "ERRORINFILENAMETOURL";
	String MAILDELIVERERRORSERVERLOGS = "MAILDELIVERERRORSERVERLOGS";
	String OBJECTBYNAMECANNOTBEFOUND = "OBJECTBYNAMECANNOTBEFOUND";
	String BEANCANNOTSETPROPERTY = "BEANCANNOTSETPROPERTY";
	String BEANCANNOTGETPROPERTY = "BEANCANNOTGETPROPERTY";
	String NAMEEMPTYBUTAUTOMNOTSET = "NAMEEMPTYBUTAUTOMNOTSET";
	String NAMEEMPTYBUTAUTOMSETNUTNOTPATTERN = "NAMEEMPTYBUTAUTOMSETNUTNOTPATTERN";
	String OBKJECTIDCANNOTBENULL = "OBKJECTIDCANNOTBENULL";
	String MAILBOXPROPERTYNOTDEFINED = "MAILBOXPROPERTYNOTDEFINED";
	String FINALIZECONTEXT = "FINALIZECONTEXT";
	String MAILCONTENTTOOLONG = "MAILCONTENTTOOLONG";
	String CANNOTINITIATEOBJECT = "CANNOTINITIATEOBJECT";
	String ERRORWHILEOPENINGXSTLFILE = "ERRORWHILEOPENINGXSTLFILE";
	String ERRORWHILEREADINGXSDFILE = "ERRORWHILEREADINGXSDFILE";
	String ERRORWHILEPARSINGXMLFILEWITHSCHEMA = "ERRORWHILEPARSINGXMLFILEWITHSCHEMA";
	String ERRORRESERERVATIONPAYMENTDETAIL = "ERRORRESERERVATIONPAYMENTDETAIL";
	String CUSTOMTYPENOTRECOGNIZED = "CUSTOMTYPENOTRECOGNIZED";
	String CUSTOMTYPENOTRECOGNIZEDLIST = "CUSTOMTYPENOTRECOGNIZEDLIST";
	String ERRORWHILETRANSFORMINGTOXML="ERRORWHILETRANSFORMINGTOXML";
	String ITEMCANNOTBESUGGEST="ITEMCANNOTBESUGGEST";
}
