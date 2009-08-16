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
