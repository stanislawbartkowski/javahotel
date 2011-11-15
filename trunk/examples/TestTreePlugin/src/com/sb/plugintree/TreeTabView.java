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
package com.sb.plugintree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.feature.tree.FeatureTree;
import org.feature.tree.TreeDialog;

// updateLaunchConfigurationDialog();

/**
 * Main logic for feature tree tab. Warning: feature tree is handled by another
 * package which can be used separately
 * 
 * @author sbartkowski
 * 
 */

public class TreeTabView extends JavaLaunchTab {

	private final static String TREE_ACCESS = " com.sb.plugintree.TREE_ACCESS";

	private final FeatureTree ftree;
	private final TreeDialog tDialog;

	public TreeTabView() {
		FeatureTree.TreeItem root = new FeatureTree.TreeItem("root", true);
		// Creates tree of feature.
		// Can be customized e.g - by reading feature tree from external XML
		// resource file
		ftree = new FeatureTree(root);
		for (int i = 0; i < 5; i++) {
			FeatureTree.TreeItem child = new FeatureTree.TreeItem("child " + i,
					true);
			root.AddChild(child);
			for (int j = 1; j < 7; j++) {
				FeatureTree.TreeItem feature = new FeatureTree.TreeItem(
						"feature " + i + ":" + j, true);
				child.AddChild(feature);
			}
		}

		// external dialog for handling feature tree
		TreeDialog.ITreeUpdateListener i = new TreeDialog.ITreeUpdateListener() {

			@Override
			public void doSomething() {
				updateLaunchConfigurationDialog();
			}
		};
		tDialog = new TreeDialog(ftree, i);

	}

	@SuppressWarnings("restriction")
	@Override
	public void createControl(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, 1, 1,
				GridData.CENTER);
		setControl(comp);
		Group namecomp = SWTFactory.createGroup(comp, "Feature view", 4,
				SWT.NONE, GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		tDialog.open(namecomp);
	}

	@Override
	public String getName() {
		return "Feature tree";
	}

	/**
	 * Recursive function. Set 'check' in the feature tree and escalates to all
	 * children. Important: pre-order traversal method is implemented (parent
	 * first then children). Because only nodes values are persisted (not tree
	 * structure) then the order of saving should match the order of restoring
	 * 
	 * @param i
	 *            Position in the list
	 * @param item
	 *            Item with new check value
	 * @param vList
	 *            List of values (true/false)
	 * @return Next index
	 */
	private int setNextValue(int i, FeatureTree.TreeItem item,
			List<String> vList) {
		if (i >= vList.size()) {
			return i;
		}
		item.setChecked(Boolean.valueOf(vList.get(i)));
		i++;
		for (FeatureTree.TreeItem child : item.getNodes()) {
			i = setNextValue(i, child, vList);
		}
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		try {
			List<String> li = config.getAttribute(TREE_ACCESS,
					new ArrayList<String>());
			setNextValue(0, ftree.getRoot(), li);
			tDialog.refreshValueToView();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy arg0) {
		List<String> li = new ArrayList<String>();
		tDialog.refreshValues();
		saveNextValue(ftree.getRoot(), li);
		arg0.setAttribute(TREE_ACCESS, li);
	}

	/**
	 * Recursive function. Opposite to setNextValue. Creates list of values
	 * basing on the current content of feature (tree) widget. Important:
	 * pre-order traversal method is implemented.
	 * 
	 * @param item
	 *            Current item to read value from
	 * @param li
	 *            List of values.
	 */
	private void saveNextValue(FeatureTree.TreeItem item, List<String> li) {
		li.add(Boolean.toString(item.isChecked()));
		for (FeatureTree.TreeItem child : item.getNodes()) {
			saveNextValue(child, li);
		}
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy arg0) {

	}

}
