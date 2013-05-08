package com.gwthotel.admin.gae.entities;

import com.googlecode.objectify.annotation.EntitySubclass;

@EntitySubclass(index = true)
public class EPerson extends EDictionary {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
