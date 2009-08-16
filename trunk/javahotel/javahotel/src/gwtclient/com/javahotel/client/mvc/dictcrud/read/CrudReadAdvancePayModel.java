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

package com.javahotel.client.mvc.dictcrud.read;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.auxabstract.AdvancePaymentCustomer;
import com.javahotel.client.mvc.crud.controler.ICrudReadModel;
import com.javahotel.client.mvc.crud.controler.ITableModelSignalRead;
import com.javahotel.client.mvc.dictdata.model.IAdvancePaymentModel;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DownPaymentP;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CrudReadAdvancePayModel implements ICrudReadModel {

	private final IResLocator rI;
	private final IAdvancePaymentModel iPa;

	CrudReadAdvancePayModel(IResLocator rI, IAdvancePaymentModel iPa) {
		this.rI = rI;
		this.iPa = iPa;
	}

	public void readModel(ITableModel model, ITableModelSignalRead signal) {
		Date d1 = iPa.getDFrom().getDate();
		Date d2 = iPa.getDTo().getDate();
		CommandParam pa = rI.getR().getHotelCommandParam();
		pa.setDateFrom(d1);
		pa.setDateTo(d2);
		rI.getR().getList(RType.DownPayments, pa, new ReadP(model, signal));
	}

	private class ReadP implements IVectorList {

		private final ITableModel model;
		private final ITableModelSignalRead signal;

		ReadP(ITableModel mo, ITableModelSignalRead signal) {
			this.model = mo;
			this.signal = signal;
		}

		public void doVList(final ArrayList<? extends AbstractTo> val) {
			Collection<AdvancePaymentCustomer> v = new ArrayList<AdvancePaymentCustomer>();
			for (AbstractTo a : val) {
				DownPaymentP d = (DownPaymentP) a;
				AdvancePaymentCustomer dV = new AdvancePaymentCustomer();
				CommandUtil.copyA(d, dV, dV.getT());
				LId id = d.getCustomerId();
				dV.setCustomerId(id);
				v.add(dV);
			}
			model.setList((ArrayList<? extends AbstractTo>) v);
			signal.successRead();
		}
	}

}
