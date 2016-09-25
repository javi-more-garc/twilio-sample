package com.jmgits.sample.twilio.util;

import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiMLException;

/**
 * Created by javi.more.garc on 25/09/16.
 */
public final class TwiMLUtil {

    public static String messagingResponse(String messageBody) throws TwiMLException {

        return new MessagingResponse.Builder()
                .message(
                        new Message.Builder().body(new Body(messageBody)).build())
                .build()
                .toXml();
    }
}
