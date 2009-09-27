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
package test.example.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.example.jpa.entities.Customer;
import org.example.service.DataAccessService;
import org.junit.Test;

public class TestCase1 extends TestCaseHelper {
	
	@Test
	public void Test1() {
		Customer c = new Customer();
		DataAccessService dao = new DataAccessService();
		c.setId("google");
		c.setDesciption("I like Google App Engine");
		dao.addCustomer(c);
		// now test it !
		Customer c1 = dao.getCustomerByKey("google");
		assertNotNull(c1);
		assertEquals("google",c1.getId());
		assertEquals("I like Google App Engine",c1.getDesciption());
	}

}
