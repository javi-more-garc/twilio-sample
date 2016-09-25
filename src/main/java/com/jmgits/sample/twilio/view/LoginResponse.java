package com.jmgits.sample.twilio.view;

import java.io.Serializable;

/**
 * Created by javi.more.garc on 25/09/16.
 */
public class LoginResponse implements Serializable {

    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
