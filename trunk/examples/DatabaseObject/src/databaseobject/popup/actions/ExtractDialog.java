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

import org.feature.tree.FeatureTree;
import org.feature.tree.TreeDialog;
import org.feature.tree.TreeDialog.ITreeDoubleCheckListener;

/**
 * Class enclosing ExtractObjectsDialog
 * @author sbartkowski
 *
 */
class ExtractDialog extends ExtractObjectsDialog {

	private final FeatureTree packages;
	private final ITreeDoubleCheckListener dListener;
	private TreeDialog t;

	ExtractDialog(Shell parent, int style, FeatureTree packages,
			ITreeDoubleCheckListener dListener, IButtonSelected extractAction) {
		super(parent, style, extractAction);
		this.packages = packages;
		this.dListener = dListener;
	}

	@Override
	void modifyContent() {
		t = new TreeDialog(tree, packages, null, dListener);
		t.open();
	}

	@Override
	void refreshValues() {
		t.refreshValues();
	}

}
