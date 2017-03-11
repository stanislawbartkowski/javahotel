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
package com.polymerui.client.eventbus.impl;

import java.util.ArrayList;
import java.util.List;

import com.polymerui.client.eventbus.IEvent;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.eventbus.IInfo;
import com.polymerui.client.eventbus.ISubscriber;

public class EventBus implements IEventBus {

	private class B {
		B(IEvent e, ISubscriber i) {
			this.e = e;
			this.i = i;
		}

		final IEvent e;
		final ISubscriber i;
	}

	private final List<B> list = new ArrayList<B>();

	private class II {
		II(IEvent e, IInfo i) {
			this.e = e;
			this.i = i;
		}

		final IEvent e;
		final IInfo i;
	}

	private final List<II> ilist = new ArrayList<II>();

	@Override
	public <I> void publish(IEvent e, I i) {
		list.stream().filter(b -> b.e.eq(e)).forEach(p -> p.i.raise(p.e, i));
	}

	@Override
	public <I> void subscribe(IEvent e, ISubscriber<I> i) {
		assert e != null;
		assert i != null;
		list.add(new B(e, i));
	}

	@Override
	public <I> IInfo<I> request(IEvent e) {
		IInfo<I> i = findI(e);
		assert i != null;
		return i;
	}

	private <T> IInfo<T> findI(IEvent e) {
		return ilist.stream().filter(b -> b.e.eq(e)).findFirst().map(b -> b.i).orElse(null);
	}

	@Override
	public <I> void registerInfoProvider(IEvent e, IInfo<I> i) {
		assert findI(e) == null;
		ilist.add(new II(e, i));
	}

}
