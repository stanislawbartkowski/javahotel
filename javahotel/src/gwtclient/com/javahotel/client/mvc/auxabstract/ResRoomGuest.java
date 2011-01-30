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

package com.javahotel.client.mvc.auxabstract;

import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.ResObjectP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ResRoomGuest extends GuestList {
    private String resName;
    private String resDesc;

    /**
     * @return the resName
     */
    public String getResName() {
        return resName;
    }

    /**
     * @param resName the resName to set
     */
    public void setResName(String resName) {
        this.resName = resName;
    }

    /**
     * @return the resDesc
     */
    public String getResDesc() {
        return resDesc;
    }

    /**
     * @param resDesc the resDesc to set
     */
    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }

    public enum F implements IField {

        name,desc,choosebutt
    };

    @Override
    public Object getF(IField f) {
        if (f instanceof F) {
            F fie = (F) f;
            switch (fie) {
                case name: return getResName();
                case desc: return getResDesc();
                case choosebutt:
                    return null;
                default: return null;
            }
        }
        return super.getF(f);
    }

    @Override
    public Class getT(IField f) {
         if (f instanceof F) {
             return String.class;
         }
         return super.getT(f);
    }


    @Override
    public void setF(IField f, Object o) {
        if (f instanceof F) {
            F fie = (F) f;
            switch (fie) {
                case name: setResName((String) o); break;
                case desc: setResDesc((String) o); break;
                case choosebutt: break;
            }
            return;
        }
        super.setF(f, o);
    }

    @Override
    public IField[] getT() {
        return CommandUtil.addT(F.values(),super.getT());
    }

    public ResRoomGuest() {
        resName = null;
        resDesc = null;
    }


    public ResRoomGuest(ResObjectP re) {
        super();
        resName = re.getName();
        resDesc = re.getDescription();
    }

}
