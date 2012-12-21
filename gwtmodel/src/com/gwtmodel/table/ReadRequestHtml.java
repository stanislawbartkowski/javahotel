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
package com.gwtmodel.table;

import com.google.gwt.http.client.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ReadRequestHtml {

    private ReadRequestHtml() {
    }

    public interface ISetRequestText {

        void setText(String s);
    }

    private static class RequestCallbackM implements RequestCallback {

        private final ISetRequestText iSet;

        RequestCallbackM(ISetRequestText iSet) {
            this.iSet = iSet;
        }
        private static final int STATUS_CODE_OK = 200;

        public void onError(Request request, Throwable exception) {
            if (exception instanceof RequestTimeoutException) {
                // handle a request timeout
            } else {
                // handle other request errors
            }
            String s = exception.getMessage();
            iSet.setText(s);
        }

        public void onResponseReceived(Request request, Response response) {
            String text = response.getText();
            if (STATUS_CODE_OK == response.getStatusCode()) {
                iSet.setText(text);
            } else {
                iSet.setText(text);
            }
        }
    }

    public static void doGet(String url, ISetRequestText iSet) {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            Request response = builder.sendRequest(null, new RequestCallbackM(
                    iSet));
        } catch (RequestException e) {
            iSet.setText(e.getMessage());
        }
    }
}
