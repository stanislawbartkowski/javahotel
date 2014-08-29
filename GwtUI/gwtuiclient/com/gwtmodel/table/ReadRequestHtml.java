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
package com.gwtmodel.table;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.gwtmodel.table.injector.LogT;

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
        private final String url;

        RequestCallbackM(ISetRequestText iSet, String url) {
            this.iSet = iSet;
            this.url = url;
        }

        private static final int STATUS_CODE_OK = 200;

        @Override
        public void onError(Request request, Throwable exception) {
            if (exception instanceof RequestTimeoutException) {
                // handle a request timeout
            } else {
                // handle other request errors
            }
            String s = exception.getMessage();
            Utils.errAlert(LogT.getT().CannotReadThisUrl(url), s);
            iSet.setText(s);
        }

        @Override
        public void onResponseReceived(Request request, Response response) {
            String text = response.getText();
            if (STATUS_CODE_OK == response.getStatusCode()) {
                iSet.setText(text);
            } else {
                Utils.errAlert(
                        LogT.getT().CannotReadThisUrlCode(url,
                                response.getStatusCode()));
                iSet.setText(text);
            }
        }
    }

    public static void doGet(String url, ISetRequestText iSet) {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            Request response = builder.sendRequest(null, new RequestCallbackM(
                    iSet, url));
        } catch (RequestException e) {
            iSet.setText(e.getMessage());
        }
    }
}
