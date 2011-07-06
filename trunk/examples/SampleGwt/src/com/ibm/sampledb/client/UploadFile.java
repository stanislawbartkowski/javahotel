package com.ibm.sampledb.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ibm.sampledb.shared.IResourceType;

public class UploadFile extends Composite {

	private static UploadFileUiBinder uiBinder = GWT
			.create(UploadFileUiBinder.class);

	interface UploadFileUiBinder extends UiBinder<Widget, UploadFile> {
	}

	public UploadFile() {
		initWidget(uiBinder.createAndBindUi(this));
		actionClose = null;
	}

	private FormPanel f() {
		return (FormPanel) this.getWidget();
	}

	@UiField
	TextBox comment;
	@UiField
	FileUpload fileupload;
	@UiField
	Hidden empname;
	@UiField
	Button submit;

	private final IAction actionClose;

	public UploadFile(String empNo, final IAction actionClose) {
		this.actionClose = actionClose;
		initWidget(uiBinder.createAndBindUi(this));
		String action = GWT.getHostPageBaseURL() + "/upLoadHandler";
		f().setAction(action);
		f().setEncoding(FormPanel.ENCODING_MULTIPART);
		f().setMethod(FormPanel.METHOD_POST);
		empname.setName(IResourceType.EMPNO);
		empname.setValue(empNo);
		comment.setName(IResourceType.COMMENT);
		fileupload.setName(IResourceType.FILE);
		SubmitCompleteHandler ha = new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String res = event.getResults();
				if (res.contains(IResourceType.ERRORSUBMIT)) {
					Window.alert(M.getM().SubmitError());
				} else {
					Window.alert(M.getM().SubmitCompleted());
				}
				actionClose.execute();
			}
		};
		f().addSubmitCompleteHandler(ha);
	}

	@UiHandler("submit")
	void onClick(ClickEvent e) {
		String filename = fileupload.getFilename();
		if (filename.equals("")) {
			Window.alert(M.getM().EnterFileName());
			return;
		}
		if (!Window.confirm(M.getM().SubmitQuestion())) {
			return;
		}
		f().submit();
	}

	@UiHandler("resign")
	void onResignClick(ClickEvent event) {
		actionClose.execute();
	}
}
