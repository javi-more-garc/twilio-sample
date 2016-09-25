package com.jmgits.sample.twilio.client;

import com.jmgits.sample.twilio.view.PortfolioDetail;

/**
 * Created by javi.more.garc on 25/09/16.
 */
public interface IrisInvestorClient {

    PortfolioDetail getPortfolioDetail(Long investorId);

}
