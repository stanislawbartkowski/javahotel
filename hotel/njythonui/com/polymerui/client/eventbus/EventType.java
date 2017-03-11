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

abstract class EventType<T> implements IEvent<T> {

	private final Class c;
	protected final T t;

	abstract boolean eqT(EventType<T> b);

	EventType(Class c, T t) {
		this.c = c;
		this.t = t;
		assert c != null;
		assert t != null;
				
	}

	@Override
	public boolean eq(IEvent e) {
		Object o = e.getT();
		if (!o.getClass().equals(c))
			return false;
		return eqT((EventType<T>) e);
	}

	@Override
	public T getT() {
		return t;
	}

}
