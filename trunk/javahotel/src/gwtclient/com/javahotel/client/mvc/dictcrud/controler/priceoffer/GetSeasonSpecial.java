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
package com.javahotel.client.mvc.dictcrud.controler.priceoffer;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.seasonprice.model.ISpecialMap;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.SeasonPeriodT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetSeasonSpecial {

	private final IResLocator rI;
	private String sName;
	private ArrayList<MapSpecialToI> toI;

	void initSeason() {
		sName = null;
	}

	@Inject
	public GetSeasonSpecial(IResLocator rI) {
		this.rI = rI;
		initSeason();
	}

	private class ReadSeason implements IOneList<OfferSeasonP> {

		ISpecialMap i;

		ReadSeason(ISpecialMap i) {
			this.i = i;
		}

		public void doOne(OfferSeasonP oP) {
			toI = new ArrayList<MapSpecialToI>();
			List<OfferSeasonPeriodP> co = oP.getPeriods();
			if (co != null) {
				for (OfferSeasonPeriodP p : co) {
					if (p.getPeriodT() !=SeasonPeriodT.SPECIAL) { continue; }
					MapSpecialToI m = new MapSpecialToI(p.getPId(), p
							.getDescription());
					toI.add(m);
				}
			}
			sName = oP.getName();
			i.set(toI);
		}
	}

	public void runSpecial(String s, ISpecialMap i) {
		if (s == null) {
			sName = null;
			toI = new ArrayList<MapSpecialToI>();
			i.set(toI);
			return;
		}
		if ((sName != null) && sName.equals(s)) {
			i.set(toI);
			return;
		}
		CommandParam p = rI.getR().getHotelCommandParam();
		p.setDict(DictType.OffSeasonDict);
		p.setRecName(s);
		rI.getR().getOne(RType.ListDict, p, new ReadSeason(i));
	}
}
