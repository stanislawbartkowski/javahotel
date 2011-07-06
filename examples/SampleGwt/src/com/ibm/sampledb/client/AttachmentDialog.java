/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.sampledb.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ibm.sampledb.shared.GetField;
import com.ibm.sampledb.shared.GetField.FieldValue;
import com.ibm.sampledb.shared.GetRowsInfo;
import com.ibm.sampledb.shared.IResourceType;
import com.ibm.sampledb.shared.OneRecord;

public class AttachmentDialog extends Composite {

	private static AttachmentDialogUiBinder uiBinder = GWT
			.create(AttachmentDialogUiBinder.class);

	@UiField
	Label title;

	@UiField
	Button upload;

	@UiField
	Button download;

	@UiField(provided = true)
	SimplePager pager = new SimplePager(TextLocation.LEFT,
			(Resources) GWT.create(SimplePager.Resources.class), false, 0, true);

	@UiField(provided = true)
	final CellTable<OneRecord> table = new CellTable<OneRecord>();

	interface AttachmentDialogUiBinder extends
			UiBinder<Widget, AttachmentDialog> {
	}

	private final OneRecord iR;
	private final GetRowsInfo rInfo;
	private final RecordPresentation rPresentation;

	class PutResource implements AsyncCallback<GetRowsInfo> {

		@Override
		public void onFailure(Throwable caught) {
			String mess = M.getM().ErrorMessage();
			Window.alert(mess);
		}

		@Override
		public void onSuccess(GetRowsInfo result) {
			rPresentation.setRInfo(result);
		}

	}

	private void drawFailure(Throwable caught) {
		String mess = M.getM().ErrorMessage();
		Window.alert(mess);
	}

	class DrawList implements AsyncCallback<List<OneRecord>> {

		@Override
		public void onFailure(Throwable caught) {
			drawFailure(caught);
		}

		@Override
		public void onSuccess(List<OneRecord> result) {
			rPresentation.drawList(result);
		}
	}

	private void drawList() {
		FieldValue empno = GetField.getValue(IResourceType.EMPNO, rInfo, iR);
		M.getSampleService().getAttachmentList(empno.getsField(),
				new DrawList());
	}

	public AttachmentDialog(OneRecord iR, GetRowsInfo rInfo) {
		initWidget(uiBinder.createAndBindUi(this));
		this.iR = iR;
		this.rInfo = rInfo;
		String l = M.getM().AttachmentInfo();
		FieldValue empno = GetField.getValue(IResourceType.EMPNO, rInfo, iR);
		FieldValue fName = GetField.getValue(IResourceType.FIRSTNME, rInfo, iR);
		FieldValue lName = GetField.getValue(IResourceType.LASTNAME, rInfo, iR);
		l += empno.getsField() + " " + fName.getsField() + " "
				+ lName.getsField();
		title.setText(l);
		rPresentation = new RecordPresentation(table, pager, true);
		M.getSampleService().getInfo(IResourceType.ATTACHMENTS,
				new PutResource());
		drawList();
	}

	private class Close implements IAction {

		private PopupDraw p;

		@Override
		public void execute() {
			drawList();
			p.hide();
		}

		public void setP(PopupDraw p) {
			this.p = p;
		}

	}

	@UiHandler("upload")
	void onUploadClick(ClickEvent event) {
		FieldValue empno = GetField.getValue(IResourceType.EMPNO, rInfo, iR);
		Close cl = new Close();
		PopupDraw p = new PopupDraw(new UploadFile(empno.getsField(), cl), true);
		cl.setP(p);
		p.draw(event.getClientX(), event.getClientY());
	}

	private FieldValue getSelected() {
		OneRecord selected = rPresentation.getSelected();
		if (selected == null) {
			Window.alert(M.getM().SelectOne());
			return null;

		}
		FieldValue id = GetField.getValue(IResourceType.ATTACHID,
				rPresentation.getrInfo(), selected);
		return id;
	}

	@UiHandler("remove")
	void onRemoveClick(ClickEvent event) {
		FieldValue id = getSelected();
		if (id == null) { return; }
		FieldValue filename = GetField.getValue(IResourceType.FILENAME,
				rPresentation.getrInfo(), rPresentation.getSelected());
		if (!Window.confirm(filename.getsField() + " "
				+ M.getM().RemoveQuestion())) {
			return;
		}
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				drawFailure(caught);
			}

			@Override
			public void onSuccess(Void result) {
				drawList();
			}

		};
		M.getSampleService().removeAttachment(id.getiField(), callback);

	}
	
	private String getLink(FieldValue id) {
		String link = GWT.getHostPageBaseURL() + "/downLoadHandler?" + IResourceType.ATTACHID + "=" + id.getiField();
		return link;
	}
	
    private class DownLoad extends Composite {
    	
    	private final VerticalPanel vp = new VerticalPanel();
    	
    	
    	DownLoad(FieldValue id) {
    		String link = getLink(id); 
//    		Anchor a = new Anchor("Download","/downloadServlet?fileId=123");
    		Anchor a = new Anchor("Download",link);
    		vp.add(a);
    		initWidget(vp);
    	}
    }
	
	@UiHandler("download")
	void onDownloadClick(ClickEvent event) {
		FieldValue id = getSelected();
		if (id == null) { return; }
		String link = getLink(id);
//		Window.open(link, "123", "");
		PopupDraw p = new PopupDraw(new DownLoad(id), false);
		Close cl = new Close();
		cl.setP(p);
		p.draw(event.getClientX(), event.getClientY());		
	}
}
