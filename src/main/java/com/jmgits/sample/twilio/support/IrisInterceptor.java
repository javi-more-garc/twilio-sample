package com.jmgits.sample.twilio.support;

import com.jmgits.sample.twilio.view.LoginRequest;
import com.jmgits.sample.twilio.view.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by javi.more.garc on 29-Jul-16.
 */
public class IrisInterceptor implements ClientHttpRequestInterceptor {

    public static final Long TOKEN_EXPIRY_IN_MINUTES = 15L;
    public static final String PATH_LOGIN = "api/authentication/login?lang=ENGLISH&tenant=DFS&gateway=g3&userType=INVESTOR";

    private static final Logger LOGGER = LoggerFactory.getLogger(IrisInterceptor.class);

    private String baseUrl;

    private RestTemplate restTemplate;

    private String apiUsername;

    private String apiPassword;

    //

    private String urlLogin;

    private LoginResponse loginResponse;
    private Date requestedAt = new Date(0);

    public IrisInterceptor(String baseUrl, RestTemplate restTemplate, String apiUsername, String apiPassword) {

        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
        this.apiUsername = apiUsername;
        this.apiPassword = apiPassword;

        this.urlLogin = String.format("%s/%s", baseUrl, PATH_LOGIN);

    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        HttpHeaders headers = request.getHeaders();
        headers.set("Content-type", APPLICATION_JSON_VALUE);
        headers.set("Authorization", String.format("Bearer %s", getOrRequestLogin().getAccessToken()));

        return execution.execute(request, body);
    }

    public LoginResponse getOrRequestLogin() {

        Date now = new Date();

        long nowInMinutes = MILLISECONDS.toMinutes(new Date().getTime());
        long requestedAtInMinutes = MILLISECONDS.toMinutes(this.requestedAt.getTime());

        long minutesSinceRequested = nowInMinutes - requestedAtInMinutes;

        LOGGER.debug("Iris token valid for '{}' minutes", TOKEN_EXPIRY_IN_MINUTES - (minutesSinceRequested > TOKEN_EXPIRY_IN_MINUTES ? 0 : minutesSinceRequested));

        return minutesSinceRequested >= TOKEN_EXPIRY_IN_MINUTES ? requestLogin(now) : loginResponse;

    }

    public LoginResponse requestLogin(Date date) {

        LOGGER.debug("Requesting Iris token...");

        LoginRequest loginRequest = new LoginRequest(apiUsername, apiPassword);

        ResponseEntity<LoginResponse> entity = restTemplate.postForEntity(urlLogin, loginRequest, LoginResponse.class);

        LOGGER.debug("Iris token requested successfully");

        this.loginResponse = entity.getBody();
        this.requestedAt = date;

        return this.loginResponse;
    }

}