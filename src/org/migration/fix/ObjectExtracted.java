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
package org.migration.fix;

import java.util.ArrayList;
import java.util.List;

import org.migration.extractor.ObjectExtractor;

public class ObjectExtracted {

	private final ObjectExtractor.IObjectExtracted o;

	private final List<ObjectExtractor.IObjectExtracted> added = new ArrayList<ObjectExtractor.IObjectExtracted>();

	public ObjectExtracted(ObjectExtractor.IObjectExtracted o) {
		this.o = o;
	}

	public ObjectExtractor.IObjectExtracted getO() {
		return o;
	}

	public List<ObjectExtractor.IObjectExtracted> getAdded() {
		return added;
	}		

}
