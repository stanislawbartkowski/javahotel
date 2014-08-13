/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jython.ui.server.jpastoragekey.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jython.serversecurity.jpa.entities.EObjectDict;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hotel", "name" }))
@NamedQueries({

        @NamedQuery(name = "findAllMails", query = "SELECT x FROM EMailNote x WHERE x.hotel = ?1"),
        @NamedQuery(name = "findOneMail", query = "SELECT x FROM EMailNote x WHERE x.hotel = ?1 AND x.name = ?2") })
public class EMailNote extends EObjectDict {

    private String content;
    private String mailFrom;
    private String sendRes;
    private boolean text;
    
    @ElementCollection
    private List<EMailAttach> aList = new ArrayList<EMailAttach>();
    
    @ElementCollection
    private List<String> recipientsList = new ArrayList<String>();

    public String getHeader() {
        return getDescription();
    }

    public void setHeader(String header) {
        setDescription(header);
    }


    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getSendRes() {
        return sendRes;
    }

    public void setSendRes(String sendRes) {
        this.sendRes = sendRes;
    }

    public List<EMailAttach> getaList() {
        return aList;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public List<String> getRecipientsList() {
        return recipientsList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
    
    

}
