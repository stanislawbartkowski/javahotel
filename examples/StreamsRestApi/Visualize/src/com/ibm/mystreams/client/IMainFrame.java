/*
 * Copyright 2014 stanislawbartkowski@gmail.com
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

import com.google.gwt.user.client.ui.Widget;

/**
 * Adapter for dealing with the whole frame
 * @author sbartkowski
 *
 */
public interface IMainFrame {

	void setStatus(String mess);

	void setError(String err);

	void refreshListOfHosts();

	void refreshListOfInstances();
	
	enum POS {UPLEFT,UPRIGHT,DOWN};
	
	void setCentre(POS pos, Widget w);

}
