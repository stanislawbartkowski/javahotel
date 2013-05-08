package com.gwthotel.admin.gae.entities;

import com.googlecode.objectify.annotation.Embed;

@Embed
public class ElemPerm {

    private String perm;
    private Long personId;
    private Long hotelId;

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

}
