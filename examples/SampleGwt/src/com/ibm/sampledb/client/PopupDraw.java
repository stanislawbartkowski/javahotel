package com.ibm.sampledb.client;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

class PopupDraw {

	private final Widget w;
	private final boolean modal;
	private PopupPanel p;

	PopupDraw(Widget w, boolean modal) {
		this.w = w;
		this.modal = modal;
	}

	public void draw(int x, int y) {
		if (modal) {
			p = new PopupPanel(false, true);
		} else {
			p = new PopupPanel(true);
		}
		p.setPopupPosition(x, y);
		p.setWidget(w);
		p.show();
	}
	
	public void hide() {
		p.hide();		
	}

}
