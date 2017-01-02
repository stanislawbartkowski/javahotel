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
package com.gwtmodel.table.view.binder;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.binder.BinderWidget;

public interface ICreateBinderWidget {
	
	HTMLPanel createEmptyHtmlPanel(BinderWidget w);

	void buildHTMLPanel(HTMLPanel w, BinderWidget bw);
	
	default HTMLPanel create(BinderWidget w) {
		HTMLPanel pa = createEmptyHtmlPanel(w);
		buildHTMLPanel(pa, w);
		return pa;
	}
}
