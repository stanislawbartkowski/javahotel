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
package com.jythonui.client.var;

import java.util.Stack;

import com.jythonui.shared.DialogVariables;
import com.polymerui.client.eventbus.IEventBus;

public class JythonVariables {

	private JythonVariables() {
	}

	private final static DialogVariables globalv = new DialogVariables();

	private final static Stack<ISetJythonVariables> iList = new Stack<ISetJythonVariables>();

	public static DialogVariables getGlobalv() {
		return globalv;
	}

	public static void registerVar(ISetJythonVariables i) {
		iList.push(i);
	}

	public static void deregisterVar(IEventBus e) {
		iList.removeIf(vi -> vi.getBus() == e);
	}

	public static void resetVar() {
		iList.clear();
	}

	public static DialogVariables constructVar() {
		DialogVariables v = new DialogVariables();
		// global
		v.copyVariables(globalv);
		// from left to right FIFO
		iList.forEach(i -> i.set(v));
		return v;
	}

	public static Stack<ISetJythonVariables> getS() {
		return iList;
	}

}
