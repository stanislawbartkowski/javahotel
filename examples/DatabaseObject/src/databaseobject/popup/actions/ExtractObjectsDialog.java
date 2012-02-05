/*
 * Copyright 2012 stanislawbartkowski@gmail.com
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
package databaseobject.popup.actions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import databaseobject.messages.Messages;

/**
 * Dialog controlled by visual editor
 * @author sbartkowski
 *
 */
abstract public class ExtractObjectsDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Tree tree;
	private final IButtonSelected extractAction;
	private Display display;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ExtractObjectsDialog(Shell parent, int style,IButtonSelected extractAction) {
		super(parent, style);
		this.extractAction = extractAction;
		setText(Messages.ExtractObjectsDialog_0);
	}
	
	abstract void modifyContent();
	abstract void refreshValues();

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		display = getParent().getDisplay();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		shell.setSize(453, 376);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshValues();
				if (extractAction.action()) {
					shell.close();
				}
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.left = new FormAttachment(0, 20);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText(Messages.ExtractObjectsDialog_1);
		
		tree = new Tree(shell, SWT.BORDER | SWT.CHECK);
		fd_btnNewButton.top = new FormAttachment(0, 300);
		FormData fd_tree = new FormData();
		fd_tree.bottom = new FormAttachment(btnNewButton, -6);
		fd_tree.left = new FormAttachment(0, 10);
		fd_tree.top = new FormAttachment(0, 9);
		fd_tree.right = new FormAttachment(0, 435);
		tree.setLayoutData(fd_tree);
		
		Label lblNewLabel = new Label(shell, SWT.WRAP);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(tree, 6);
		fd_lblNewLabel.left = new FormAttachment(btnNewButton, 56);
		fd_lblNewLabel.right = new FormAttachment(100, -10);
		fd_lblNewLabel.bottom = new FormAttachment(100, -10);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText(Messages.ExtractObjectsDialog_2);
		modifyContent();
	}
}
