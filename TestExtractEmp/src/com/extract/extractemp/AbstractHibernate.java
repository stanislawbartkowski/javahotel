/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.extract.extractemp;

import java.sql.SQLException;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.extract.extractemp.IExtractEmp.IResultSet;

abstract class AbstractHibernate implements IExtractEmp {

	protected Session session;
	private SessionFactory sessionFactory;
	private final String confName;
	private final String queryName;
	
	protected AbstractHibernate(String confName,String queryName) {
		this.confName = confName;
		this.queryName = queryName;
	}

	@Override
	public void connect() throws ClassNotFoundException, SQLException {

		Configuration config = new Configuration().configure(confName);
		sessionFactory = config.buildSessionFactory();
		session = sessionFactory.openSession();
	}

	public void close() {
		session.close();
		sessionFactory.close();
	}

	private IResultSet getResultSet(final Query q) {
		ScrollableResults res = q.scroll();
		
		
		return new IResultSet() {
			
			@Override
			public boolean next() throws SQLException {
				return res.next();
			}

			@Override
			public String getString(int col) throws SQLException {
				return (String) res.get(col-1);
			}

			@Override
			public void close() throws SQLException {
				res.close();

			}

		};
	}
	
	@Override
	public IResultSet getEmp(String empName, String mgmName, String depName) throws SQLException {
		final Query query = session.getNamedQuery(queryName);
		query.setString("p_empname", empName);
		query.setString("p_deptname", depName);
		query.setString("p_mgmname", mgmName);
		return getResultSet(query);
	}


}
