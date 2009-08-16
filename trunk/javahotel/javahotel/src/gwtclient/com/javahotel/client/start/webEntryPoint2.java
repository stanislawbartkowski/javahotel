package com.javahotel.client.start;

import com.google.gwt.core.client.EntryPoint;
import com.javahotel.client.dispatcher.DispatchFactory;

public class webEntryPoint2 extends AbstractWebEntry implements EntryPoint {

	/** Creates a new instance of webEntryPoint */
	public webEntryPoint2() {
		super(false, DispatchFactory.createDispatch());
	}

	public void onModuleLoad() {
		load();
	}
}
