package com.jython.serversecurity.jpa;

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

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jython.serversecurity.jpa.entities.EInstance;
import com.jython.serversecurity.jpa.entities.EObject;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;
import com.jythonui.server.BUtil;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class OObjectAdminInstance extends UtilHelper implements IAppInstanceOObject {

	private final ITransactionContextFactory iC;
	private final IGetLogMess lMess;

	@Inject
	public OObjectAdminInstance(ITransactionContextFactory iC,
			@Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess lMess) {
		this.iC = iC;
		this.lMess = lMess;
	}

	private abstract class doTransaction extends JpaTransaction {

		private doTransaction() {
			super(iC);
		}
	}

	private class getInstance extends doTransaction {

		private final String instanceName;
		private final String userName;
		Long id;

		getInstance(String instanceName, String userName) {
			this.instanceName = instanceName;
			this.userName = userName;
		}

		@Override
		protected void dosth(EntityManager em) {
			EInstance insta = null;
			Query q = em.createNamedQuery("findInstanceByName");
			q.setParameter(1, instanceName);
			try {
				insta = (EInstance) q.getSingleResult();
			} catch (NoResultException e) {
				// test if default instances
				if (instanceName.equals(ISharedConsts.INSTANCETEST)
						|| instanceName.equals(ISharedConsts.INSTANCEDEFAULT)) {
					insta = new EInstance();
					insta.setName(instanceName);
					BUtil.setCreateModif(userName, insta, true);
					em.persist(insta);
					makekeys();
					info(lMess.getMessN(ILogMess.DEFAULTINSTANCEHASBEENCREATED, instanceName));
				} else
					throw (e);
			}
			id = insta.getId();
			if (id == null) {
				String mess = lMess.getMess(IErrorCode.ERRORCODE84, ILogMess.INSTANCEIDCANNOTNENULLHERE);
				errorLog(mess);
			}

		}
	}

	@Override
	public AppInstanceId getInstanceId(String instanceName, String userName) {
		getInstance comm = new getInstance(instanceName, userName);
		comm.executeTran();
		AppInstanceId id = new AppInstanceId();
		id.setId(comm.id);
		id.setInstanceName(instanceName);
		id.setPerson(userName);
		return id;
	}

	private class getHotelId extends doTransaction {

		private final AppInstanceId instanceId;
		private final String hotelName;
		private final String userName;
		Long id;

		getHotelId(AppInstanceId instanceId, String hotelName, String userName) {
			this.instanceId = instanceId;
			this.hotelName = hotelName;
			this.userName = userName;
		}

		@Override
		protected void dosth(EntityManager em) {
			Query q = em.createNamedQuery("findObjectByName");
			q.setParameter(1, instanceId.getId());
			q.setParameter(2, hotelName);
			EObject h = null;
			try {
				h = (EObject) q.getSingleResult();
			} catch (NoResultException e) {
				// test if default instances
				String instanceName = instanceId.getInstanceName();
				if (instanceName.equals(ISharedConsts.INSTANCETEST)
						|| instanceName.equals(ISharedConsts.INSTANCEDEFAULT)) {
					h = new EObject();
					h.setInstanceId(instanceId.getId());
					h.setName(hotelName);
					BUtil.setCreateModif(userName, h, true);
					em.persist(h);
					makekeys();
					info(lMess.getMessN(ILogMess.FIRSTOBJECTCREATED, instanceName, hotelName, h.getId().toString(),
							userName));
				} else
					throw e;
			}
			id = h.getId();
		}

	}

	@Override
	public OObjectId getOObjectId(AppInstanceId instanceId, String objectName, String userName) {
		getHotelId comm = new getHotelId(instanceId, objectName, userName);
		comm.executeTran();
		if (comm.id == null)
			errorMess(L(), IErrorCode.ERRORCODE128, ILogMess.NULLGETOBJECTID, instanceId.getId().toString(), objectName,
					userName);
		OObjectId h = new OObjectId();
		h.setObject(objectName);
		h.setId(comm.id);
		h.setInstanceId(instanceId);
		h.setUserName(userName);
		return h;
	}

}
