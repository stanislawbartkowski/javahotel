/*
 * Copyright 2015 stanislawbartkowski@gmail.com
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
package com.ibm.mystreams.client;

import com.google.gwt.core.client.GWT;
import com.ibm.mystreams.client.database.DatabaseFactory;
import com.ibm.mystreams.client.database.IDatabase;

/**
 * Container for keeping some global data
 * @author sbartkowski
 *
 */
public class M {

	private M() {

	}

	private final static GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	private static IMainFrame iFrame;
	private final static IDatabase iBase = DatabaseFactory.construct();

	private final static VConstants constants = (VConstants) GWT
			.create(VConstants.class);

	public static IDatabase getIbase() {
		return iBase;
	}

	public static IMainFrame getiFrame() {
		return iFrame;
	}

	public static void setiFrame(IMainFrame iFrame) {
		M.iFrame = iFrame;
	}

	public static GreetingServiceAsync getS() {
		return greetingService;
	}

	public static VConstants L() {
		return constants;
	}

}
