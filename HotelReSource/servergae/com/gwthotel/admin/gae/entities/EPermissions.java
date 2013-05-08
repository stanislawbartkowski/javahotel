package com.gwthotel.admin.gae.entities;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class EPermissions {

    @Id
    private Long id;

    private List<ElemPerm> eList = new ArrayList<ElemPerm>();

    public Long getId() {
        return id;
    }

    public List<ElemPerm> geteList() {
        return eList;
    }

}
