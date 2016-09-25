package com.jmgits.sample.twilio.support;

import com.jmgits.sample.twilio.view.PortfolioDetail;
import com.jmgits.sample.twilio.view.PortfolioValue;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.List;

/**
 * Created by javi.more.garc on 25/09/16.
 */
@Component
public class MessageBodySupport {

    private static String RESP_GENERAL_ERROR = "A general error occurred. Please,try later...";
    private static String RESP_ERROR_UNKNOWN_COMMAND = "Unknown command. Commands available: %s";

    private static String RESP_PORTFOLIO_DETAIL_MARKET_VALUE = "The total market value of your portfolio is $%s.\nThank You. UNIVERIS";
    private static String RESP_PORTFOLIO_DETAIL_BOOK_VALUE = "You have invested $%s to date.\nThank You. UNIVERIS";

    public String generateGeneralErrorBody() {
        return RESP_GENERAL_ERROR;
    }

    public String generateErrorUnknownCommandBody(List<String> commands){
        return String.format(RESP_ERROR_UNKNOWN_COMMAND, commands);
    }

    public String generateMarketValueMessageBody(PortfolioDetail portfolioDetail) {

        PortfolioValue value = portfolioDetail.getMarketValue();

        return transform(RESP_PORTFOLIO_DETAIL_MARKET_VALUE, value);
    }

    public String generateBookValueMessageBody(PortfolioDetail portfolioDetail) {

        PortfolioValue value = portfolioDetail.getBookValue();

        return transform(RESP_PORTFOLIO_DETAIL_BOOK_VALUE, value);
    }

    //
    // private methods

    private String transform(String template, PortfolioValue value) {
        String amountStr = value.getValue().setScale(2, RoundingMode.HALF_UP).toString();

        return String.format(template, amountStr);
    }

}
