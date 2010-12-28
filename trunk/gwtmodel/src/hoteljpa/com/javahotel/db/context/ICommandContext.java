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
package com.javahotel.db.context;

import com.javahotel.common.command.DictType;
import com.javahotel.db.hotelbase.jpa.RHotel;
import com.javahotel.db.hotelbase.types.IPureDictionary;
import com.javahotel.db.jtypes.HId;
import com.javahotel.dbjpa.ejb3.JpaEntity;
import com.javahotel.dbutil.log.GetLogger;
import com.javahotel.remoteinterfaces.SessionT;
import com.javahotel.security.login.HotelLoginP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public interface ICommandContext {

    JpaEntity getJpa();

    void setJpa(JpaEntity jpa);

    HotelLoginP getHP();

    SessionT getSession();

    void setHotel(String h);

    String getHotel();

    void logFatal(String messid, Object ... params);

    void logFatalE(Exception e);
    
    GetLogger getLog();
    
    String logEvent(String eId,final Object ... params);
    
    public RHotel getRHotel();
    
    IPersistCache getC();
    
	boolean isNull(HId id);
	
	public String getRecordDescr(DictType d, IPureDictionary o);



}
