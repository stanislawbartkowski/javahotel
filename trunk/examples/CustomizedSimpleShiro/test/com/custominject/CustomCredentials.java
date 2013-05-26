package com.custominject;
import java.util.ArrayList;
import java.util.List;

import com.custom.nextrealm.InjectCredentials;

public class CustomCredentials implements InjectCredentials {

    @Override
    public String getPerson() {
        return "guest";
    }

    @Override
    public String getPassword() {
        return "secret";
    }

    @Override
    public List<String> getRoles() {
        List<String> roles = new ArrayList<String>();
        roles.add("welcome");
        return roles;
    }

}
