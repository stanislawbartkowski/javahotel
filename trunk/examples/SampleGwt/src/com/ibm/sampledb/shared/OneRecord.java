/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.sampledb.shared;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class OneRecord implements Serializable {
	
	/**
	 * Placeholder to enforce serialization of this types in the Map 
	 */
	@SuppressWarnings("unused")
	private Integer i;
	@SuppressWarnings("unused")
	private BigDecimal b;
	@SuppressWarnings("unused")
	private Timestamp t;
	
	private Map<String,Object> fields = new HashMap<String,Object>();
	
	public void setField(String key, Object value) {
		fields.put(key, value);
	}
	
	public Object getField(String key) {
		return fields.get(key);
	}		

}
