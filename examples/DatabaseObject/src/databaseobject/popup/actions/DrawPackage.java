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

import org.eclipse.swt.widgets.Shell;


import databaseobject.util.CreateObjectTree;

/**
 * Package enclosing DrawPackageDialog 
 * @author sbartkowski
 *
 */

public class DrawPackage extends DrawPackageDialog {

	private final CreateObjectTree.PackageObject pa;
	
	public DrawPackage(Shell parentShell, CreateObjectTree.PackageObject pa ) {
		super(parentShell);
		this.pa = pa;
	}

	@Override
	void modifContent() {
		// set content with package specification and body
		spectext.setText(pa.getSpec());
		bodytext.setText(pa.getBody());
		
	}

}
