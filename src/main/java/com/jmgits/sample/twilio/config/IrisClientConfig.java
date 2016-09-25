package com.jmgits.sample.twilio.config;

import com.jmgits.sample.twilio.client.IrisInvestorClient;
import com.jmgits.sample.twilio.client.impl.IrisInvestorClientImpl;
import com.jmgits.sample.twilio.support.IrisInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import java.util.Collections;

/**
 * Created by javi.more.garc on 25/09/16.
 */
@Configuration
public class IrisClientConfig {

    public static final int DEFAULT_READ_TIMEOUT_IN_SECS = 5;

    public static final int DEFAULT_CONNECTION_TIMEOUT_IN_SECS = 5;

    public static final int INTERCEPTOR_READ_TIMEOUT_IN_SECS = 15;

    public static final int INTERCEPTOR_TIMEOUT_IN_SECS = 15;

    @Value("${application.iris.url}")
    private String baseUrl;

    @Value("${application.iris.username}")
    private String username;

    @Value("${application.iris.password}")
    private String password;

    @Value("${application.environment}")
    private String environment;

    @Bean
    public IrisInvestorClient irisClient() {

        RestTemplate irisRestTemplate =
                new RestTemplate(clientHttpRequestFactory(DEFAULT_CONNECTION_TIMEOUT_IN_SECS, DEFAULT_READ_TIMEOUT_IN_SECS));

        irisRestTemplate.setInterceptors(Collections.singletonList(irisInterceptor()));

        return new IrisInvestorClientImpl(baseUrl, irisRestTemplate);

    }

    //
    // private methods

    private ClientHttpRequestFactory clientHttpRequestFactory(int connectTimeoutInSecs, int readTimeoutInSecs) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000 * connectTimeoutInSecs)
                .setConnectionRequestTimeout(1000 * readTimeoutInSecs)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor());

        if (!"pro".equals(environment)) {
            httpClientBuilder = httpClientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier());
        }

        HttpClient client = httpClientBuilder.build();

        return new HttpComponentsClientHttpRequestFactory(client);

    }

    private IrisInterceptor irisInterceptor() {

        RestTemplate irisInterceptorRestTemplate =
                new RestTemplate(clientHttpRequestFactory(INTERCEPTOR_TIMEOUT_IN_SECS, INTERCEPTOR_READ_TIMEOUT_IN_SECS));

        return new IrisInterceptor(baseUrl, irisInterceptorRestTemplate, username, password);
    }

}
