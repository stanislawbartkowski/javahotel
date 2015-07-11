/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.defa;

import java.util.List;

import javax.inject.Inject;

import com.gwtmodel.table.common.CUtil;
import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.IResolveNameFromToken;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.registry.IStorageRegistry;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.shared.RequestContext;

public class StorageRealmRegistryFactory implements IStorageRegistryFactory {

	private final IStorageRealmRegistry iRegistry;

	private final IResolveNameFromToken iToken;

	private final ISecurity iSec;

	@Inject
	public StorageRealmRegistryFactory(IStorageRealmRegistry iRegistry, IResolveNameFromToken iToken, ISecurity iSec) {
		this.iRegistry = iRegistry;
		this.iToken = iToken;
		this.iSec = iSec;
	}

	private class StorageRealmRegistry implements IStorageRegistry {

		private final String realm;
		private final boolean object;
		private final boolean user;

		private String getrealm() {
			if (!object && !user)
				return realm;
			RequestContext r = Holder.getRequest();
			String token = r.getToken();
			if (token == null)
				return realm;
			String prefix = null;
			if (object && iSec.getCustom(token) != null) {
				OObjectId o = iToken.getObject(token);
				prefix = o.getObject();
			}
			if (user)
				prefix = CUtil.concatSP(prefix, iSec.getUserName(token));
			return CUtil.concatSP(prefix, realm);
		}

		StorageRealmRegistry(String realm, boolean object, boolean user) {
			this.realm = realm;
			this.object = object;
			this.user = user;
		}

		@Override
		public void putEntry(String key, byte[] value) {
			iRegistry.putEntry(getrealm(), key, value);
		}

		@Override
		public byte[] getEntry(String key) {
			return iRegistry.getEntry(getrealm(), key);
		}

		@Override
		public void removeEntry(String key) {
			iRegistry.removeEntry(getrealm(), key);
		}

		@Override
		public List<String> getKeys() {
			return iRegistry.getKeys(getrealm());
		}

	}

	@Override
	public IStorageRegistry construct(String realm, boolean object, boolean user) {
		return new StorageRealmRegistry(realm, object, user);
	}

}
