package com.jmgits.sample.twilio.service;

import com.jmgits.sample.twilio.view.PortfolioDetail;

/**
 * Created by javi.more.garc on 24/09/16.
 */
public interface InvestorService {

    Long getInvestorId(String mobileNumber);

    PortfolioDetail getPortfolioDetail(String mobileNumber);

}
