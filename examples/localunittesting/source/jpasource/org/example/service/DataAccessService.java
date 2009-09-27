/*
 * Copyright 2009 stanislawbartkowski@gmail.com
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
package org.example.service;

import javax.persistence.EntityManager;

import org.example.jpa.entities.Customer;
import org.example.jpa.entities.PMF;

public class DataAccessService {
	
	private final EntityManager ma;
	
	public DataAccessService() {
		ma = PMF.getFactory().createEntityManager();		
	}	
	
	public void addCustomer(Customer c) {
		ma.getTransaction().begin();
		ma.persist(c);
		ma.getTransaction().commit();
	}
	
	public Customer getCustomerByKey(String key) {
		Customer c = ma.find(Customer.class,key);
		return c;
	}

}
