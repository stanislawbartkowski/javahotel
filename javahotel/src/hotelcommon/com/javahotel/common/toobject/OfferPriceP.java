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
package com.javahotel.common.toobject;

import com.javahotel.common.command.CommandUtil;
import java.util.List;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class OfferPriceP extends DictionaryP {

	private String season;
	private List<OfferServicePriceP> serviceprice;

	public List<OfferServicePriceP> getServiceprice() {
		return serviceprice;
	}

	public void setServiceprice(
			final List<OfferServicePriceP> serviceprice) {
		this.serviceprice = serviceprice;
	}

	public enum F implements IField {

		season
	}

	@Override
	public Class<?> getT(final IField f) {
		Class<?> cla = super.getT(f);
		if (cla != null) {
			return cla;
		}
		F fie = (F) f;
		switch (fie) {
		case season:
			return String.class;
		}
		return null;
	}

	@Override
	public IField[] getT() {
		return CommandUtil.addTD(F.values());
	}

	@Override
	public Object getF(IField f) {
		DictionaryP.F pfi = CommandUtil.dF(f);
		if (pfi != null) {
			return super.getF(pfi);
		}

		F fi = (F) f;
		switch (fi) {
		case season:
			return getSeason();
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
		case season:
			setSeason((String) o);
			break;
		}
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(final String season) {
		this.season = season;
	}
}
