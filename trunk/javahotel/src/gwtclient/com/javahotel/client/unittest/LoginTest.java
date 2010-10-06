/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.unittest;

import com.google.gwt.junit.client.GWTTestCase;
import com.gwtmodel.table.ICommand;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dispatcher.EnumAction;
import com.javahotel.client.dispatcher.EnumDialog;
import com.javahotel.client.dispatcher.IDispatch;
import com.javahotel.client.start.testWebEntryPoint;

public class LoginTest extends GWTTestCase {
	
	public String getModuleName() {
		return "com.javahotel.test";
	}
	
	private class TestDispatch implements IDispatch {

		public void dispatch(EnumDialog t, EnumAction a) {
			
		}

		public void start(IResLocator i, ICommand decorateAfterLogin) {
		}
		
	}
	
	public void testAdminLogin() {
		testWebEntryPoint w = new testWebEntryPoint(new TestDispatch());
		w.testLoad();
		
	}


}
