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
package com.jythonui.client;

import com.google.gwt.i18n.client.Messages;

/**
 * @author hotel
 * 
 */
public interface JMessages extends Messages {

    String ErrorNoValue(String key);

    String ListDoesNotHaveELem(String listId, String attrName);

    String UnknownAction(String action, String param);

    String CustomTypeNotDefine();
    
    String CannotFindCustomType(String typeName);
    
    String CannotFindFromField(String from,String fid);
    
    String ValidateAttributeNotDefined(String id);
    
    String ValidateFieldNotFound(String id,String fid);
    
    String NoFieldRelatedToError(String id, String fie);

}
