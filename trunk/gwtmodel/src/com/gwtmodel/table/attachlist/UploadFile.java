 /*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.attachlist;

/**
 *
 * @author perseus
 */
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UploadFile extends Composite {
    
    interface ISubmitRes {
        void execute(String res);
    }
    
    private class SCompleted implements SubmitCompleteHandler {
        
        ISubmitRes i;

        @Override
        public void onSubmitComplete(SubmitCompleteEvent event) {
            i.execute(event.getResults());
        }
        
    }

    private static UploadFileUiBinder uiBinder = GWT.create(UploadFileUiBinder.class);
    @UiField(provided = true)
    Widget comment;
    @UiField(provided = true)
    Widget fileupload;
    @UiField
    Label labelcomment;
    @UiField
    Label labelfile;
    
    private SCompleted s = new SCompleted();

    interface UploadFileUiBinder extends UiBinder<Widget, UploadFile> {
    }

    public UploadFile(Widget comment, Widget fileupload) {
        this.comment = comment;
        this.fileupload = fileupload;
        initWidget(uiBinder.createAndBindUi(this));
        String action = GWT.getHostPageBaseURL() + "/upLoadHandler";
        f().setAction(action);
        f().setEncoding(FormPanel.ENCODING_MULTIPART);
        f().setMethod(FormPanel.METHOD_POST);
        f().addSubmitCompleteHandler(s);
    }

    private FormPanel f() {
        return (FormPanel) this.getWidget();
    }

    void submit(ISubmitRes i) {
        s.i = i;
        f().submit();
    }
}