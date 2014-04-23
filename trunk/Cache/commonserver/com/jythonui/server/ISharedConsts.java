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
package com.jythonui.server;

public interface ISharedConsts {

    String COMMONREGISTRYBEANJNDI = "java:global/JythonRegistry";

    String COMMONSEQGENJNDI = "java:global/SeqGenRegistry";

    String COMMONBEANBLOBJNDI = "java:global/BlobRegistry";

    // String RESOURCES = "resources";

    String JYTHONMESSSERVER = "jythonservermess";

    String JYTHONENVCACHE = "JYTHON-ENV-TOMCAT-CACHE";
    String JYTHONXMLHELPERCACHE = "JYTHON-XML-HELPER-CACHE";
    
    String XMLHELPERCACHED = "jythonxmlhelpercached";

    int DEFEXPIRATIONSEC = 30;

    char MALEDICT = 'M';
    char FEMALEDICT = 'F';
    char DEFAULTID = 'I';

    String CREATIONPERSONPROPERTY = "creationPerson";
    String CREATIONDATEPROPERTY = "creationDate";
    String MODIFPERSONPROPERTY = "modifPerson";
    String MODIFDATEPROPERTY = "modifDate";

}