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

import java.util.List;

public interface IMailSend {

    class AttachElem {
        private final String realM;
        private final String blobId;
        private final String fileName;

        public AttachElem(String realM, String blobId, String fileName) {
            this.realM = realM;
            this.blobId = blobId;
            this.fileName = fileName;
        }

        public String getBlobId() {
            return blobId;
        }

        public String getFileName() {
            return fileName;
        }

        public String getRealM() {
            return realM;
        }
    }

    String postMail(boolean text, String recipients[], String subject,
            String message, String from, List<AttachElem> aList);

}
