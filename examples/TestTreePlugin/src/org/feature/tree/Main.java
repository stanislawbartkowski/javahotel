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

package org.feature.tree;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Main program for org.feature.tree package. Can be used as separate SWT
 * application for the testing purspose. Not used in plugin.
 * 
 * @author sbartkowski
 * 
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("TreeExample");
		shell.setLayout(new FillLayout());
		FeatureTree.TreeItem root = new FeatureTree.TreeItem("root", false);
		FeatureTree tree = new FeatureTree(root);
		for (int i = 0; i < 5; i++) {
			FeatureTree.TreeItem child = new FeatureTree.TreeItem("child" + i,
					false);
			root.AddChild(child);
			for (int j = 1; j < 7; j++) {
				FeatureTree.TreeItem feature = new FeatureTree.TreeItem(
						"feature" + i + ":" + j, false);
				child.AddChild(feature);
			}
		}

		new TreeDialog(tree, null).open(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}

}
