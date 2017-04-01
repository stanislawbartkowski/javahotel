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
package com.polymerui.client.eventbus;

abstract class AbstractTypeString extends AbstractEventType {

	private final String id;

	AbstractTypeString(String id, Class cl) {
		super(cl);
		this.id = id;
		assert id != null;
	}

	@Override
	public boolean eq(IEvent e) {
		if (!super.eq(e))
			return false;
		AbstractTypeString et = (AbstractTypeString) e;
		return id.equals(et.id);
	}

	public String getT() {
		return id;
	}

}
