/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
public class WSize {

	private final int top;
	private final int left;
	private final int height;
	private final int width;
	private final Widget w;

	public WSize(int top, int left, int height, int width) {
		this.top = top;
		this.left = left;
		this.height = height;
		this.width = width;
		this.w = null;
	}

	public WSize(Widget w) {
		this.top = w.getAbsoluteTop();
		this.left = w.getAbsoluteLeft();
		this.height = w.getOffsetHeight();
		this.width = w.getOffsetWidth();
		this.w = w;
	}

	public WSize(IGWidget w) {
		this(w.getGWidget());
	}

	public WSize(com.google.gwt.dom.client.Element e) {
		this.top = e.getAbsoluteTop();
		this.left = e.getAbsoluteLeft();
		this.height = e.getOffsetHeight();
		this.width = e.getOffsetWidth();
		this.w = null;
	}

	public int getTop() {
		return top;
	}

	public int getLeft() {
		return left;
	}

	public int getBottom() {
		return top + height;
	}

	public int getRight() {
		return left + width;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Widget getW() {
		return w;
	}

}
