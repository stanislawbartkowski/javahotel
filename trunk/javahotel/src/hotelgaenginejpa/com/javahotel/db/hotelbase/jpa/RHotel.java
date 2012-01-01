/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

package com.javahotel.db.hotelbase.jpa;
 
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.IId;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */

@Entity
public class RHotel implements IId {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;

	public HId getId() {
		return new HId(id);
	}

	public void setId(HId id) {
		this.id = id.getL();
	}
	
    @Basic(optional=false)
    private String name;


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }


}
