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
package com.gwtmodel.table.attachlist;

import com.gwtmodel.table.AVModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import java.util.Date;
import java.util.List;

/**
 *
 * @author perseus
 */


public class AttachData extends AVModelData {

    private Date addDate;
    private Date modifDate;
    private String comment;
    private String fileName;
    private Long id;
    
    private String tempFileName;

    @Override
    public List<IVField> getF() {
        IVField f[] = {
            AttachDataField.vadddate,
            AttachDataField.vmodifdate,
            AttachDataField.vcomment,
            AttachDataField.vid,
            AttachDataField.vfilename
        };
        List<IVField> li = Utils.toList(f);
        return li;
    }

    @Override
    public Object getF(IVField fie) {
        AttachDataField d = (AttachDataField) fie;
        switch (d.getFie()) {
            case ADDDATE:
                return getAddDate();
            case MODIFDATE:
                return getModifDate();
            case COMMENT:
                return getComment();
            case FILENAME:
                return getFileName();
            case ID:
                return getId();
        }
        return null;
    }

    @Override
    public void setF(IVField fie, Object val) {
        AttachDataField d = (AttachDataField) fie;
        switch (d.getFie()) {
            case ADDDATE:
                setAddDate(Utils.DToD(val));
                break;
            case MODIFDATE:
                setModifDate(Utils.DToD(val));
                break;
            case COMMENT:
                setComment((String) val);
                break;
            case FILENAME:
                setFileName((String) val);
                break;
            case ID:
                setId((Long) val);
                break;
        }
    }

    /**
     * @return the addDate
     */
    public Date getAddDate() {
        return addDate;
    }

    /**
     * @param addDate the addDate to set
     */
    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    /**
     * @return the modifDate
     */
    public Date getModifDate() {
        return modifDate;
    }

    /**
     * @param modifDate the modifDate to set
     */
    public void setModifDate(Date modifDate) {
        this.modifDate = modifDate;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isValid(IVField fie) {
        return true;
    }

    /**
     * @return the tempFileName
     */
    public String getTempFileName() {
        return tempFileName;
    }

    /**
     * @param tempFileName the tempFileName to set
     */
    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }

    
}
