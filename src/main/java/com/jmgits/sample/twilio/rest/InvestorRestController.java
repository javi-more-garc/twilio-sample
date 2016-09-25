package com.jmgits.sample.twilio.rest;

import com.jmgits.sample.twilio.service.InvestorService;
import com.jmgits.sample.twilio.support.MessageBodySupport;
import com.jmgits.sample.twilio.util.TwiMLUtil;
import com.jmgits.sample.twilio.view.PortfolioDetail;
import com.twilio.twiml.TwiMLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/v1/investor")
public class InvestorRestController {

    private static List<String> REQ_PORTFOLIO_DETAIL_COMMANDS = asList("Give me my account balance", "How much money have I invested");

    private static final Logger LOGGER = LoggerFactory.getLogger(InvestorRestController.class);

    @Inject
    private InvestorService investorService;

    @Inject
    private MessageBodySupport messageBodySupport;

    @RequestMapping(value = "/sms", method = POST, produces = "application/xml")
    public String sms(@RequestParam("From") String from, @RequestParam("Body") String body) throws Exception {

        if (isUnknownCommand(body)) {
            return TwiMLUtil.messagingResponse(messageBodySupport.generateErrorUnknownCommandBody(REQ_PORTFOLIO_DETAIL_COMMANDS));
        }

        PortfolioDetail portfolioDetail = investorService.getPortfolioDetail(from);

        String messageBody = messageBodySupport.generateMessageBody(portfolioDetail);

        return TwiMLUtil.messagingResponse(messageBody);
    }

    @ExceptionHandler
    public String error(Exception e) throws TwiMLException {

        LOGGER.error("Error handling sms", e);

        return TwiMLUtil.messagingResponse(messageBodySupport.generateGeneralErrorBody());

    }

    //
    // private methods

    private boolean isUnknownCommand(String passedCommand) {
        return REQ_PORTFOLIO_DETAIL_COMMANDS.stream()
                .noneMatch(existingCommand -> existingCommand.equalsIgnoreCase(passedCommand));
    }
}
