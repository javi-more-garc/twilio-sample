package com.jmgits.sample.twilio.client.impl;

import com.jmgits.sample.twilio.client.IrisInvestorClient;
import com.jmgits.sample.twilio.view.PortfolioDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpMethod.GET;

/**
 * Created by javi.more.garc on 25/09/16.
 */
public class IrisInvestorClientImpl extends AbstractClientImpl implements IrisInvestorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrisInvestorClientImpl.class);

    private RestTemplate restTemplate;

    public IrisInvestorClientImpl(String baseUrl, RestTemplate restTemplate) {
        super(baseUrl, "/api/investor", restTemplate);

        this.restTemplate = restTemplate;
    }

    @Override
    public PortfolioDetail getPortfolioDetail(Long investorId) {

        LOGGER.debug("Requesting investor portfolio details...");

        String path = UriComponentsBuilder.fromUriString("/{investorId}/portfolio") //
                .build() //
                .expand(investorId) //
                .toUriString();

        String finalUrl = String.format("%s%s", baseUrl, path);

        ResponseEntity<PortfolioDetail> responseEntity = restTemplate.exchange(finalUrl, GET, null, PortfolioDetail.class);

        LOGGER.debug("Investor portfolio details requested successfully");

        return responseEntity.getBody();
    }
}
