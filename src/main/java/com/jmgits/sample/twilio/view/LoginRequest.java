package com.jmgits.sample.twilio.view;

import java.io.Serializable;

/**
 * Created by javi.more.garc on 25/09/16.
 */
public class LoginRequest implements Serializable{

    private String username;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
