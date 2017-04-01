/*
 * Copyr ight 2016 stanislawbartkowski@gmail.com 
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
package com.polymerui.client.view.util;

import java.util.Iterator;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.binder.IAttrName;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.client.M;
import com.polymerui.client.IConsts;
import com.polymerui.client.util.Utils;

public class PolymerUtil {

	private PolymerUtil() {
	}

	public static String convert(String s) {
		StringBuffer b = new StringBuffer(s);
		while (true) {
			int i = b.indexOf(IConsts.RESBEG);
			if (i == -1)
				break;
			int k = b.indexOf(IConsts.RESEND, i);
			if (k == -1)
				break;
			String res = b.substring(i + 1, k);
			// together with $
			String v = M.S().getString(res);
			b.replace(i, k + 2, v);
		}
		return b.toString();
	}

	public interface IVisitor {
		void visit(String fieldid, Widget w);
	}

	public static void walkHTMLPanel(HasWidgets ha, IVisitor i) {
		Iterator<Widget> iW = ha.iterator();
		// iterate through widget
		while (iW.hasNext()) {
			Widget ww = iW.next();

			String id = Utils.getWidgetAttribute(ww, IAttrName.FIELDID);
			if (!CUtil.EmptyS(id))
				i.visit(id, ww);
			if (ww instanceof HasWidgets)
				walkHTMLPanel((HasWidgets) ww, i);
		}
	}

	private static class FoundI implements IVisitor {

		private final String searchId;
		private Widget foundW;

		FoundI(String searchId) {
			this.searchId = searchId;
		}

		@Override
		public void visit(String fieldid, Widget w) {
			if (fieldid.equals(searchId))
				foundW = w;
		}

	}

	public static Widget findWidgetByFieldId(HasWidgets ha, String fieldid) {

		FoundI i = new FoundI(fieldid);

		walkHTMLPanel(ha, i);

		return i.foundW;
	}

	public static void verifyWidgetType(String description, Widget w, Class<? extends Widget>... classes) {

		for (Class<? extends Widget> c : classes)
			if (w.getClass().equals(c))
				return;
		String desc = null;
		for (Class<? extends Widget> c : classes)
			if (desc == null)
				desc = c.getName();
			else
				desc = desc + " , " + c.getName();
		Utils.errAlertB(M.M().ActionSupportedOnlyForWidget(description, w.getClass().getName(), desc));
	}

	public static Widget findandverifyWidget(String description, HTMLPanel ha, String id,
			Class<? extends Widget>... classes) {
		Widget w = findWidgetByFieldId(ha, id);
		if (w == null)
			Utils.errAlertB(M.M().CannotFindElementForAction(description, id));
		else
			verifyWidgetType(description, w, classes);
		return w;
	}

}
