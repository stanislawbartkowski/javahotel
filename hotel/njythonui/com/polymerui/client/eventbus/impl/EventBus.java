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
	
	
	@Override
	public void publish(IEvent e) {
		list.stream().filter(b -> b.e.eq(e)).forEach(p -> p.i.raise(p.e));		
	}

	@Override
	public void subscribe(IEvent e, ISubscriber i) {
		assert e != null;
		assert i != null;
		list.add(new B(e,i));		
	}

}
