package com.jmgits.sample.twilio.view;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by javi.more.garc on 24/09/16.
 */
public class PortfolioValue implements Serializable {

    private String currency;
    private BigDecimal value;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
