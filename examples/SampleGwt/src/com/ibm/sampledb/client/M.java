package com.ibm.sampledb.client;

import com.google.gwt.core.client.GWT;

class M {

	static private final Message message = GWT.create(Message.class);

	static private final SampleServiceAsync sampleService = GWT
			.create(SampleService.class);

	static Message getM() {
		return message;
	}

	static SampleServiceAsync getSampleService() {
		return sampleService;
	}

}
