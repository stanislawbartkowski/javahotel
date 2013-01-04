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

package com.mygwt.client.impl.mail;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.factories.IJavaMailAction;
import com.gwtmodel.table.mail.ListOfMailProperties;
import com.gwtmodel.table.mail.MailResult;
import com.gwtmodel.table.mail.MailToSend;
import com.gwtmodel.table.mailcommon.CListOfMailProperties;
import com.gwtmodel.table.mailcommon.CMailToSend;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.mygwt.client.RemoteService;

public class MailOp extends AbstractSlotContainer implements IJavaMailAction {

    private class ReadElem implements AsyncCallback<CListOfMailProperties> {

        @Override
        public void onFailure(Throwable caught) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSuccess(CListOfMailProperties arg) {
            ListOfMailProperties ma = new ListOfMailProperties(
                    arg.getmList(true), arg.getErrMess());
            publish(IJavaMailAction.SENDLISTMAILPROPERTIES, ma);
        }
    }

    private class GetMailList implements ISlotListener {

        public void signal(ISlotSignalContext slContext) {

            RemoteService.getA().getListOfMailBoxes(new ReadElem());
        }
    }

    private class MailRes implements AsyncCallback<String> {

        @Override
        public void onSuccess(String arg) {
            MailResult res = new MailResult(arg);
            publish(IJavaMailAction.SEND_RESULT, res);
        }

        @Override
        public void onFailure(Throwable caught) {
            // TODO Auto-generated method stub

        }
    }

    private class SendMail implements ISlotListener {

        public void signal(ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            MailToSend ma = (MailToSend) o;
            CMailToSend cma = ma.construct();
            RemoteService.getA().sendMail(cma, new MailRes());
        }
    }

    public MailOp() {
        registerSubscriber(IJavaMailAction.ACTIONGETLISTMAILPROPERTIES,
                new GetMailList());
        registerSubscriber(IJavaMailAction.SEND_MAIL, new SendMail());
    }
}
