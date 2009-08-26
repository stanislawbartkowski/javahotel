/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.webhelper;

import com.javahotel.commoncache.CollCache;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class SessionCache {

	private static CollCache<SessionT> ca;

	static {
		ca = new CollCache(HelperResources.WEBHELPERCACHE);
	}

	static SessionT getSessionT(final String sess) {
		SessionT te = ca.get(sess);
		if (te == null) {
			HelperResources.logE(HelperResources.NOTAUTHENTICATED, sess);
			return null;
		}
		return te;
	}

	static void addSession(final String sess, final SessionT se) {
		ca.addT(sess, se);
	}

	static void removeSession(final String sess) {
		ca.removeT(sess);
	}

}
