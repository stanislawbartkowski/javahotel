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
package com.javahotel.remoteinterfaces;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Remote
public interface IHotelOp {

    ReturnPersist hotelOp(SessionT sessionID, HotelOpType op,
            CommandParam p);

    ReturnPersist hotelOp(SessionT sessionID, CommandParam p);

    List<ReturnPersist> hotelOp(SessionT sessionID, List<CommandParam> p);
}
