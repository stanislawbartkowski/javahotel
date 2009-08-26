/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

package com.javahotel.client.dialog;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public interface IPersistAction {

    int ADDACION = 0;
    int MODIFACTION = 1;
    int DELACTION = 2;

    int AKCACTION = 3;
    int RESACTION = 4;
    
    int ENABLEDIALOGACTION = 5;
    int DISABLEDIALOGACTION = 6;
    
    int CUSTOMACTION = 100;

}
