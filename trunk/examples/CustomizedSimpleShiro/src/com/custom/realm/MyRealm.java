package com.custom.realm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


public class MyRealm extends AuthorizingRealm {

    public MyRealm() {
        super();
        setName("Custom realm");
    }

    private void throwNotExist(String person) {
        throw new AuthenticationException("User with username: " + person
                + " does not exist.");
    }

    private class User {
        private List<String> roles;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) at;
        String person = token.getUsername();
        if (!person.equals("guest")) throwNotExist(person);
        User user = new User();
        user.roles = new ArrayList<String>();
        user.roles.add("guest");
        return new SimpleAuthenticationInfo(user, "guest", getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        User user = (User) getAvailablePrincipal(pc);
        Set roleNames = new HashSet();
        Set permissions = new HashSet();

        for (String role : user.roles) {
            roleNames.add(role);
        }

        SimpleAuthorizationInfo simpleAuthInfo = new SimpleAuthorizationInfo(
                roleNames);
        simpleAuthInfo.setStringPermissions(permissions);
        return simpleAuthInfo;
    }
}