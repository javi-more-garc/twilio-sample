package com.jmgits.sample.twilio.service.impl;

import com.jmgits.sample.twilio.client.IrisInvestorClient;
import com.jmgits.sample.twilio.service.InvestorService;
import com.jmgits.sample.twilio.view.PortfolioDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Created by javi.more.garc on 24/09/16.
 */
@Service
public class InvestorServiceImpl implements InvestorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvestorServiceImpl.class);

    @Value("${application.iris.username}")
    private String username;

    @Inject
    private IrisInvestorClient irisInvestorClient;

    @Override
    public Long getInvestorId(String mobileNumber) {

        LOGGER.warn("Using fixed investor id '{}' for mobile number '{}'", username, mobileNumber);

        // TODO Use a real service (probably in the client)
        return Long.valueOf(username);
    }

    @Override
    public PortfolioDetail getPortfolioDetail(String mobileNumber) {

        Long investorId = getInvestorId(mobileNumber);

        return irisInvestorClient.getPortfolioDetail(investorId);
    }
}
