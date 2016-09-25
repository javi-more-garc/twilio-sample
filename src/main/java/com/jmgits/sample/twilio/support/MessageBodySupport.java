package com.jmgits.sample.twilio.support;

import com.jmgits.sample.twilio.view.PortfolioDetail;
import com.jmgits.sample.twilio.view.PortfolioValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by javi.more.garc on 25/09/16.
 */
@Component
public class MessageBodySupport {

    private static String RESP_GENERAL_ERROR = "A general error occurred. Please,try later...";
    private static String RESP_ERROR_UNKNOWN_COMMAND = "Unknown command. Commands available: %s";

    private static String RESP_PORTFOLIO_DETAIL_MARKET_VALUE = "Total market value: %s (%s)";
    private static String RESP_PORTFOLIO_DETAIL_GAIN_LOSS = "Gain/ loss: %s (%s)";
    private static String RESP_PORTFOLIO_DETAIL_BOOK_VALUE = "Book value: %s (%s)";

    public String generateGeneralErrorBody() {
        return RESP_GENERAL_ERROR;
    }

    public String generateErrorUnknownCommandBody(List<String> commands){
        return String.format(RESP_ERROR_UNKNOWN_COMMAND, commands);
    }

    public String generateMessageBody(PortfolioDetail portfolioDetail) {

        PortfolioValue marketValue = portfolioDetail.getMarketValue();
        PortfolioValue gainLoss = portfolioDetail.getGainLoss();
        PortfolioValue bookValue = portfolioDetail.getBookValue();

        List<String> items = new ArrayList<>();

        Optional.ofNullable(marketValue).ifPresent(value -> items.add(transform(RESP_PORTFOLIO_DETAIL_MARKET_VALUE, value)));
        Optional.ofNullable(gainLoss).ifPresent(value -> items.add(transform(RESP_PORTFOLIO_DETAIL_GAIN_LOSS, value)));
        Optional.ofNullable(bookValue).ifPresent(value -> items.add(transform(RESP_PORTFOLIO_DETAIL_BOOK_VALUE, value)));

        return items.stream().collect(Collectors.joining(", "));

    }

    //
    // private methods

    private String transform(String template, PortfolioValue value) {
        return String.format(template, value.getValue().setScale(2, java.math.RoundingMode.HALF_UP).toString(), value.getCurrency());
    }

}
