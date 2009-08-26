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
package com.javahotel.db.authentication.impl;

import com.javahotel.db.authentication.jpa.Hotel;
import com.javahotel.db.authentication.jpa.Person;
import com.javahotel.db.command.CommandTemplate;
import com.javahotel.db.util.CommonHelper;
import com.javahotel.dbjpa.ejb3.JpaEntity;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.resources.GetLMess;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
abstract class CommandTra extends CommandTemplate {

	protected Hotel ha;
	protected Person pe;
	protected final HotelT hotel;
	protected final String person;
	private final boolean checkhotel;
	private final boolean checkperson;

	@Override
	protected void prevRun() {
		if (person != null) {
			pe = AutUtil.getPerson(iC.getJpa(), person);
			if (checkperson && (pe == null)) {
				closeJpa(false);
				HLog.failureE(GetLMess.CANNOTFINDPERSON, person);
				return;
			}
		}
		if (hotel != null) {
			ha = iC.getJpa().getOneWhereRecord(Hotel.class, "name",
					hotel.getName());
			if (checkhotel && (ha == null)) {
				closeJpa(false);
				HLog.failureE(GetLMess.CANNOTFINDHOTEL, hotel.getName());
				return;
			}
		}
		startTra(); // GAE: after

	}

	CommandTra(final SessionT sessionId, final String person,
			final HotelT hotel, boolean admin, boolean starttra,
			boolean checkperson, boolean checkhotel) {

		super(sessionId, admin, starttra, true);
		this.checkhotel = checkhotel;
		this.checkperson = checkperson;
		this.hotel = hotel;
		this.person = person;
		JpaEntity jpa = CommonHelper.getAutJPA();
		iC.setJpa(jpa);
	}
}
