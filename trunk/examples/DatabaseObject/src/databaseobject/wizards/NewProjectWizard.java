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
package databaseobject.wizards;


import java.net.URI;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import databaseobject.messages.Messages;
import databaseobject.util.ProjectSupport;


/**
 * Class raised if 'New project' was launched
 * @author sbartkowski
 *
 */
public class NewProjectWizard extends Wizard implements INewWizard {

	private WizardNewProjectCreationPage pageOne;

	@Override
	public boolean performFinish() {
		String name = pageOne.getProjectName();
		URI location = null;
		if (!pageOne.useDefaults()) {
			location = pageOne.getLocationURI();
		} 
		ProjectSupport.createProject(name, location);
		return true;
	}

	public NewProjectWizard() {
		setWindowTitle(Messages.NewProjectWizard_0);
	}

	@Override
	public void addPages() {
		super.addPages();

		pageOne = new WizardNewProjectCreationPage(
				Messages.NewProjectWizard_1);
		pageOne.setTitle(Messages.NewProjectWizard_2);
		pageOne.setDescription(Messages.NewProjectWizard_3);

		addPage(pageOne);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}
	

}
