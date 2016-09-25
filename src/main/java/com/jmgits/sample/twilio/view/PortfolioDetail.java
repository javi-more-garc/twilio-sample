package com.jmgits.sample.twilio.view;

import java.io.Serializable;

/**
 * Created by javi.more.garc on 24/09/16.
 */
public class PortfolioDetail implements Serializable {

    private PortfolioValue marketValue;
    private PortfolioValue bookValue;
    private PortfolioValue gainLoss;

    public PortfolioValue getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(PortfolioValue marketValue) {
        this.marketValue = marketValue;
    }

    public PortfolioValue getBookValue() {
        return bookValue;
    }

    public void setBookValue(PortfolioValue bookValue) {
        this.bookValue = bookValue;
    }

    public PortfolioValue getGainLoss() {
        return gainLoss;
    }

    public void setGainLoss(PortfolioValue gainLoss) {
        this.gainLoss = gainLoss;
    }
}
