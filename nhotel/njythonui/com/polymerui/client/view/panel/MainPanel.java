/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not ue this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.polymerui.client.view.panel;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.client.M;
import com.polymerui.client.IConsts;
import com.polymerui.client.IGWidget;
import com.polymerui.client.util.Utils;
import com.polymerui.client.view.util.PolymerUtil;
import com.vaadin.polymer.paper.widget.PaperMenu;

class MainPanel implements IMainPanel {

	private final HTMLPanel ha;

	// <g:HTMLPanel width="5%" fieldid="progressHtml">
	// <g:Image fieldid="progressIcon" visible="false"></g:Image>
	// <g:Label fieldid="labelError" visible="true"></g:Label>
	// </g:HTMLPanel>

	private final static Map<InfoType, String> mapToElem = new HashMap<InfoType, String>();

	private final Image proI;
	private final HTMLPanel hProgress;

	private int colL = 0;

	private IContent contenti = null;

	// default_logo.png
	// String h =
	// Utils.getImageAdr(pResources.getRes(IWebPanelResources.IIMAGEPRODUCT));
	// uW.titleIcon.setUrl(h);

	static {
		mapToElem.put(InfoType.DATA, IConsts.PANELHOTELLABEL);
		mapToElem.put(InfoType.OWNER, IConsts.PANELOWNERLABEL);
		mapToElem.put(InfoType.PRODUCT, IConsts.PANELPRODUCTLABEL);
		mapToElem.put(InfoType.UPINFO, IConsts.PANELINFOLABEL);
	}

	private Image findImageWidget(String id) {
		Widget w = PolymerUtil.findandverifyWidget(M.M().MainPanelIconWidget(id), ha, id, Image.class);
		return (Image) w;
	}

	private Element getId(String id) {
		// debug
		Element e = ha.getElementById(id);
		if (e != null)
			return e;
		String errmess = M.M().HTMLPanelCannotFindWIdget("Left menu", id);
		Utils.errAlertB(errmess);
		return null;
	}

	// String JVersion = pResources.getRes(IWebPanelResources.JUIVERSION);
	// String title = CUtil.joinS('\n',
	// pResources.getRes(IWebPanelResources.VERSION),
	// LogT.getT().GWTVersion(GWT.getVersion(), JVersion));

	// String ima = pResources.getRes(IWebPanelResources.PROGRESSICON);
	// String url = Utils.getImageAdr(ima);
	// uW.progressIcon.setUrl(url);

	private void drawLogo(String logoIcon, String v) {
		Image i = findImageWidget(IConsts.PANELTITLEICON);
		String h = Utils.getImageAdr(logoIcon);
		i.setUrl(h);
		String title = CUtil.joinS('\n', IConsts.UIVersion, M.M().GWTVersion(GWT.getVersion()), v);
		i.setTitle(title);
	}

	MainPanel(HTMLPanel ha, String logoIcon, String v) {
		this.ha = ha;
		drawLogo(logoIcon, v);
		proI = (Image) PolymerUtil.findandverifyWidget(M.M().MainPanelIconWidget(IConsts.PANELPROGRESSICON), ha,
				IConsts.PANELPROGRESSICON, Image.class);
		String url = Utils.getImageAdr(IConsts.PROGRESSIMAGEDEFAULT);
		proI.setUrl(url);
		hProgress = (HTMLPanel) PolymerUtil.findandverifyWidget(M.M().MainPanelIconWidget(IConsts.PANELPROGRESSHTML),
				ha, IConsts.PANELPROGRESSHTML, HTMLPanel.class);

		// PANELPROGRESSHTML
	}

	private void setProgIcon(boolean visible) {
		proI.setVisible(visible);
	}

	private void setErrorMess(String errMess) {
		setProgIcon(false);
		if (errMess == null) {
			hProgress.removeStyleName("error");
			hProgress.setTitle("");
		} else {
			hProgress.addStyleName("error");
			hProgress.setTitle(errMess);
		}
	}

	@Override
	public void setErrorL(String errmess) {
		setErrorMess(errmess);
	}

	@Override
	public Widget getWidget() {
		return ha;
	}

	@Override
	public void drawInfo(InfoType e, String s) {
		if (e == InfoType.TITLE) {
			Window.setTitle(s);
			return;
		}
		String pName = mapToElem.get(e);
		if (pName == null) {
			Utils.errAlertB(M.M().MainPanelNotImplemented(e.name()));
			return;
		}
		Widget w = PolymerUtil.findandverifyWidget(M.M().MainPanelActionName(e.name(), pName), ha, pName, Label.class);
		Label l = (Label) w;
		l.setText(s);
	}

	private void setReplay(int replNo) {
		if (replNo == 1) {
			setErrorMess(null);
			hProgress.removeStyleName("error");
			setProgIcon(true);
		} else if (replNo == 0)
			setProgIcon(false);
	}

	@Override
	public void IncDecCounter(boolean inc) {
		if (inc)
			colL++;
		else
			colL--;
		setReplay(colL);
	}

	@Override
	public PaperMenu getLeftMenu() {
		PaperMenu p = (PaperMenu) PolymerUtil.findandverifyWidget(M.M().MainPanelIconWidget("leftmenu"), ha, "leftmenu",
				PaperMenu.class);
		return p;

	}

	@Override
	public void replaceContent(IContent i) {
		HTMLPanel pane = (HTMLPanel) PolymerUtil.findandverifyWidget(M.M().MainPanelIconWidget("mainpanel"), ha,
				"mainpanel", HTMLPanel.class);
		pane.clear();
		pane.add(i.getH());
		contenti = i;
	}

	@Override
	public IContent getCurrentContent() {
		return contenti;
	}

}
