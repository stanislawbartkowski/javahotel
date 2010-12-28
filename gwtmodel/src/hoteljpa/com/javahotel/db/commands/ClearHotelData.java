/*
 * Copyright 2010 stanislawbartkowski@gmail.com
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

import com.javahotel.common.command.DictType;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

public class ClearHotelData extends CommandAbstract {

	    public ClearHotelData(final SessionT sessionId, final HotelT ho) {
	        super(sessionId, true, true, ho);
	    }

	    @Override
	    protected void command() {
	    	String l = iC.logEvent(IMessId.CLEARHOTELDATA);
	    	iC.getLog().getL().warning(l);
	    	HotelHelper.removeAllDic(iC, DictType.RegistryParam, new HotelT(iC.getHotel()));
	    	HotelHelper.removeAllDic(iC, DictType.BookingList, new HotelT(iC.getHotel()));
	    	HotelHelper.removeAllDic(iC, DictType.CustomerList, new HotelT(iC.getHotel()));
	    	HotelHelper.removeAllDic(iC, DictType.PriceListDict, new HotelT(iC.getHotel()));
	    	HotelHelper.removeAllDic(iC, DictType.OffSeasonDict, new HotelT(iC.getHotel()));
	    	HotelHelper.removeAllDic(iC, DictType.RoomObjects, new HotelT(iC.getHotel()));
	    	HotelHelper.removeAllDic(iC, DictType.RoomStandard, new HotelT(iC.getHotel()));
	    	HotelHelper.removeAllDic(iC, DictType.RoomFacility, new HotelT(iC.getHotel()));
	    	HotelHelper.removeAllDic(iC, DictType.ServiceDict, new HotelT(iC.getHotel()));
	    	HotelHelper.removeAllDic(iC, DictType.VatDict, new HotelT(iC.getHotel()));
	    	HotelHelper.removeHo(iC, new HotelT(iC.getHotel()));
	    }
}
