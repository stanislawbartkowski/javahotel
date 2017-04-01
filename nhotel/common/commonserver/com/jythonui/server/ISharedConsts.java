/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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

public interface ISharedConsts {

	String COMMONREGISTRYBEANJNDI = "java:global/JythonRegistry";

	String COMMONSEQGENJNDI = "java:global/SeqGenRegistry";

	String COMMONBEANBLOBJNDI = "java:global/BlobRegistry";

	String COMMONOBJECTADMINJNDI = "java:global/ObjectAdmin";
	String COMMONAPPINSTANCEJNDI = "java:global/AppInstance";

	String COMMONNOTESTORAGEJNDI = "java:global/NoteStorage";
	String COMMONNOJOURNALJNDI = "java:global/JournalStorage";

	String JYTHONMESSSERVER = "jythonservermess";

	String JYTHONENVCACHE = "JYTHON-ENV-TOMCAT-CACHE";
	String JYTHONXMLHELPERCACHE = "JYTHON-XML-HELPER-CACHE";
	String CACHEREALMOBJECTINSTANCE = "JYTHON-CACHEREALM-OBJECT-INSTANCE";

	String XMLHELPERCACHED = "jythonxmlhelpercached";

	int DEFEXPIRATIONSEC = 30;

	char MALEDICT = 'M';
	char FEMALEDICT = 'F';
	char DEFAULTID = 'I';

	String CREATIONPERSONPROPERTY = "creationPerson";
	String CREATIONDATEPROPERTY = "creationDate";
	String MODIFPERSONPROPERTY = "modifPerson";
	String MODIFDATEPROPERTY = "modifDate";

	String NAME = "name";
	String DESCRIPTION = "descr";
	String VATLEVELPROP = "level";

	String INSTANCEDEFAULT = "AppInstanceDefault";
	String INSTANCETEST = "AppInstanceTest";

	String SINGLEOBJECTHOLDER = "SingleObjectHolder";
	String PERSONSONLYSECURITY = "PersonsOnlySecurity";

	String MAILFROM = "NoteFrom";
	String MAILSENTRESULT = "NoteResult";
	String MAILCONTENT = "NoteContent";

	String MAILATTACHFILENAME = "NoteAttachFileName";
	String MAILATTACHREALM = "NoteAttachRealm";
	String MAILATTACHKEY = "NoteAttachKey";

	String OBJECTPROP = "objectid";
	String PATTPROP = "autompatt";

	String MAILNOTEPATT = "MAIL (Y) / (M) / (N)";
	String MAILNOTEREALM = "MAIL NOTES REALM";

	int MAILCONTENTSIZE = 1000;

	String SUGGESTIONREALM = "SUGGESTION-REALM";
	String REMEMBERREALM = "REMEMBER-REALM";

	String JOURNALPATT = "JOURNAL (Y) / (M) / (N)";
	String JOURNALTYPE = "JOURNALTYPE";
	String JOURNALTYPESPEC = "JOURNALTYPESPEC";
	String JOURNALELEM1 = "JOURNALELEM1";
	String JOURNALELEM2 = "JOURNALELEM2";
}
