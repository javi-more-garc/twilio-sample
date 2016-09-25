/**
 * 
 */
package com.jmgits.sample.twilio.client.impl;

import org.springframework.web.client.RestTemplate;

/**
 * @author javi.more.garc
 *
 */
public abstract class AbstractClientImpl {

    protected String baseUrl;
    protected RestTemplate restTemplate;
    
    public AbstractClientImpl(String baseUrl, String clientUrl, RestTemplate restTemplate) {
        this.baseUrl = String.format("%s%s", baseUrl, clientUrl);
        this.restTemplate = restTemplate;
    }

}
