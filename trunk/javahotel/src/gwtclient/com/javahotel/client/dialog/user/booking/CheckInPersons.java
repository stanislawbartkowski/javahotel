/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.dialog.user.booking;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.util.GetMaxUtil;
import java.util.ArrayList;
import com.javahotel.client.mvc.auxabstract.ResRoomGuest;
import com.javahotel.client.mvc.edittable.dialog.EditTableDialogFactory;
import com.javahotel.client.mvc.edittable.dialog.IEditTableDialog;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.client.roominfo.RoomInfoData;
import com.javahotel.common.command.SynchronizeList;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.ResObjectP;
import java.util.Collection;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.client.widgets.popup.PopupUtil;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.persistrecord.IPersistResult;
import com.javahotel.common.command.CustomerType;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CheckInPersons {

	private final IResLocator rI;
	private final BookingP p;
	private final RoomInfoData rInfo;
	private Sync sync;
	private final IPersistResult pe;

	CheckInPersons(IResLocator rI, BookingP p, IPersistResult pe) {
		this.rI = rI;
		this.p = p;
		this.rInfo = new RoomInfoData(rI);
		this.pe = pe;
	}

	private class Sync extends SynchronizeList {

		private final ArrayList<ResRoomGuest> gList = new ArrayList<ResRoomGuest>();
		private final Widget w;

		Sync(int no, Widget w) {
			super(no);
			this.w = w;
		}

		@Override
		protected void doTask() {
			IEditTableDialog iD = EditTableDialogFactory.getDialog(rI,
					new DictData(DictData.SpecE.ResGuestList), p.getName(),
					gList);
			iD.setPersist(pe);
			PopupUtil.setPos(iD.getDialog(), w);
			iD.show();
		}
	}

	private class R implements IOneList {

		private final BookElemP r;
		private SyncC s;

		R(BookElemP r) {
			this.r = r;
		}

		private class SyncC extends SynchronizeList {

			SyncC(int no) {
				super(no);
			}

			@Override
			protected void doTask() {

				sync.signalDone();
			}
		}

		private class BackC implements RData.IOneList {

			private final ResRoomGuest ge;

			BackC(ResRoomGuest ge) {
				this.ge = ge;
			}

			public void doOne(AbstractTo val) {
				CustomerP cu = (CustomerP) val;
				ge.setO2(cu);
				s.signalDone();
			}
		}

		private ResRoomGuest createR(ResObjectP re) {
			ResRoomGuest gu = new ResRoomGuest(re);
			gu.getO2().setCType(CustomerType.Person);
			return gu;
		}

		public void doOne(AbstractTo val) {
			ResObjectP re = (ResObjectP) val;
			int ma = re.getNoPerson();
			ArrayList<ResRoomGuest> gList = new ArrayList<ResRoomGuest>();
			Collection<GuestP> ge = r.getGuests();
			s = new SyncC(1);
			if (ge != null) {
				s = new SyncC(1 + ge.size());
				for (GuestP g : ge) {
					ResRoomGuest gu = createR(re);
					gu.setO1(g);
					gList.add(gu);
					CommandParam pa = rI.getR().getHotelDictId(
							DictType.CustomerList, g.getCustomer());
					rI.getR().getOne(RType.ListDict, pa, new BackC(gu));
				}
			}
			while (gList.size() < ma) {
				ResRoomGuest gu = createR(re);
				gList.add(gu);
			}
			sync.gList.addAll(gList);
			s.signalDone();
		}
	}

	void showDialog(Widget arg0) {
		BookRecordP br = GetMaxUtil.getLastBookRecord(p);
		sync = new Sync(br.getBooklist().size(), arg0);
		for (BookElemP r : br.getBooklist()) {
			R rr = new R(r);
			rInfo.getInfo(r.getResObject(), rr);
		}
	}
}
