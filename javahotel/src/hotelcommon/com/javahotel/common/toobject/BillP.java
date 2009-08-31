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
package com.javahotel.common.toobject;

import java.util.List;

import com.javahotel.common.command.BillEnumTypes;
import com.javahotel.common.command.CommandUtil;
import com.javahotel.types.ILd;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class BillP extends DictionaryP implements ILd {

	public BillEnumTypes getBillType() {
		return billType;
	}

	public void setBillType(BillEnumTypes billType) {
		this.billType = billType;
	}

	public List<PaymentP> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentP> payments) {
		this.payments = payments;
	}

	public List<AdvancePaymentP> getAdvancePay() {
		return advancePay;
	}

	public void setAdvancePay(List<AdvancePaymentP> advancePay) {
		this.advancePay = advancePay;
	}

	public LId getCustomer() {
		return customer;
	}

	public void setCustomer(LId customer) {
		this.customer = customer;
	}

	/**
	 * @return the addpayments
	 */
	public List<AddPaymentP> getAddpayments() {
		return addpayments;
	}

	/**
	 * @param addpayments
	 *            the addpayments to set
	 */
	public void setAddpayments(List<AddPaymentP> addpayments) {
		this.addpayments = addpayments;
	}

	/**
	 * @return the oPrice
	 */
	public String getOPrice() {
		return oPrice;
	}

	/**
	 * @param oPrice
	 *            the oPrice to set
	 */
	public void setOPrice(String oPrice) {
		this.oPrice = oPrice;
	}

	public enum F implements IField {
		billType
	};

	private BillEnumTypes billType;
	private List<PaymentP> payments;
	private List<AdvancePaymentP> advancePay;
	private LId customer;
	private List<AddPaymentP> addpayments;
	private String oPrice;

	@Override
	public Object getF(IField f) {
		DictionaryP.F pfi = CommandUtil.dF(f);
		if (pfi != null) {
			return super.getF(pfi);
		}

		F fi = (F) f;
		switch (fi) {
		case billType:
			return getBillType();
		}
		return null;
	}

	@Override
	public void setF(IField f, Object o) {
		DictionaryP.F pfi = CommandUtil.dF(f);
		if (pfi != null) {
			super.setF(pfi, o);
			return;
		}

		F fi = (F) f;
		switch (fi) {
		case billType:
			setBillType((BillEnumTypes) o);
			break;
		}
	}
}
