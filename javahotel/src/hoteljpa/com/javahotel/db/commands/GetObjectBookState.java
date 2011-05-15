/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.commands;

import java.util.List;

import com.javahotel.common.rescache.ReadResParam;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.db.commands.bookstate.BookState;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetObjectBookState extends CommandAbstract {

	private final ReadResParam rParam;
	private List<ResDayObjectStateP> rOut;

	public GetObjectBookState(final SessionT sessionId, final HotelT hotel,
			final ReadResParam rParam) {
		super(sessionId, false, hotel);
		this.rParam = rParam;
	}

	@Override
	protected void command() {
		rOut = BookState.getRestState(iC, rParam, null);
	}

	public List<ResDayObjectStateP> getROut() {
		for (ResDayObjectStateP p : rOut) {
			String s = p.getResObject();
			String bName = p.getBookName();
			String b = p.getLState().toString();
			String msg=iC.logEvent(IMessId.RESSTATERES,bName,s,b);
			iC.getLog().getL().fine(msg);
		}
		return rOut;
	}
}
