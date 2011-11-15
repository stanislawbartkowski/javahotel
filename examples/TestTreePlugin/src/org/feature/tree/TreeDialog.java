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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * SWT widget for handling the feature tree
 * 
 */
public class TreeDialog {

	public interface ITreeUpdateListener {
		void doSomething();
	}

	/** Tree : SWT Widget. */
	private Tree check;
	/** FeatureTree : model for Tree. */
	private final FeatureTree root;

	/** Listener for change. */
	private final ITreeUpdateListener iListener;

	public TreeDialog(FeatureTree root, ITreeUpdateListener iListener) {
		this.root = root;
		this.iListener = iListener;
	}

	private void setParentAndChildren(TreeItem tparent,
			FeatureTree.TreeItem parent) {
		tparent.setText(parent.getDisplayName());
		for (FeatureTree.TreeItem child : parent.getNodes()) {
			TreeItem tchild = new TreeItem(tparent, SWT.NONE);
			setParentAndChildren(tchild, child);
		}
	}

	private void setValueParentAndChildren(TreeItem tparent,
			FeatureTree.TreeItem parent) {
		tparent.setChecked(parent.isChecked());
		for (int i = 0; i < parent.getNodes().size(); i++) {
			setValueParentAndChildren(tparent.getItems()[i], parent.getNodes()
					.get(i));
		}
	}

	public void open(Composite composite) {

		check = new Tree(composite, SWT.CHECK | SWT.BORDER);
		check.addListener(SWT.Selection, new Listener() {

			/**
			 * Recursive function: modify node and pass down to all children
			 */
			private void checkChildren(TreeItem item) {
				TreeItem[] children = item.getItems();
				for (TreeItem child : children) {
					child.setChecked(item.getChecked());
					checkChildren(child);
				}
			}

			public void handleEvent(Event event) {
				if (event.detail != SWT.CHECK) {
					return;
				}
				if (iListener != null) {
					iListener.doSomething();
				}
				TreeItem item = (TreeItem) event.item;
				checkChildren(item);
			}
		});
		GridData data = new GridData(GridData.FILL_BOTH);
		check.setLayoutData(data);
		// root
		TreeItem troot = new TreeItem(check, SWT.NONE);
		// pre-order walk (parent first, then children from left to right) */
		setParentAndChildren(troot, root.getRoot());
		setValueParentAndChildren(troot, root.getRoot());
	}

	private void refreshNode(TreeItem tparent, FeatureTree.TreeItem fparent) {
		// we assume one to one relationship between TreeItem (SWT) and
		// FeautureTree (data structure)
		fparent.setChecked(tparent.getChecked());
		TreeItem[] tchildren = tparent.getItems();
		List<FeatureTree.TreeItem> fchilder = fparent.getNodes();
		for (int i = 0; i < tchildren.length; i++) {
			refreshNode(tchildren[i], fchilder.get(i));
		}

	}

	public void refreshValueToView() {
		setValueParentAndChildren(check.getTopItem(), root.getRoot());
	}

	public void refreshValues() {
		TreeItem troot = check.getItem(0);
		FeatureTree.TreeItem froot = root.getRoot();
		// Important: pre-order walk, it should reflect the way how the tree was
		// created
		refreshNode(troot, froot);
	}

}
