# twilio-sample

Spring Boot application to demonstrate simple SMS request/ response handling by means of Twilio.

## Requirements to run the application 

* Java 8
* Maven
* IRIS endpoint
* Twilio application (optional if we just want to execute directly our REST endpoint to see the TwiML responses)

## Core Concepts

The web application expects Twilio to forward incoming SMS to http(s)://host:port/api/v1/investor/sms

We're initially checking that the SMS body contains a certain message body (aka command) that we support.

Initially we support these commands:

* Give me my account balance
* How much money have I invested
 
If we receive any of them, we connect with IRIS (through REST) and send to the enduser a TwiML response with the portfolio details:

```
 <Response>
   <Message>
     <Body>Total market value: 150428.24 (CANADIAN_DOLLAR), Gain/ loss: 60146.67 (CANADIAN_DOLLAR)</Body>
   </Message>
 </Response>
``` 

Otherwise, we just send a TwiML response saying we don't know the command requested:

```
<Response>
  <Message>
    <Body>Unknown command. Commands available: [Give me my account balance, How much money have I invested]</Body>
  </Message>
</Response>
```
 
## Improvements

We should remove the dummy implementation of InvestorServiceImpl.getInvestorId(mobileNumber) so that we return the investorId associated to the mobile number. This may affect as well the login functionality

The method InvestorRestController.sms should probably delegate the incoming SMS parameters received to a delegator class. This class would have helpers to dispatch commands to certain processors.

Introducing some voice functionalities.

Better token request/ refresh handling

## Running the application

We have different environments so we'd have to configure the application-xxx.properties we want to use.

Say we want to run the dev environment. We'd use this:

> mvn spring-boot:run -DSPRING_PROFILES_ACTIVE=dev -Djasypt.encryptor.password=xxx 

The jasypt password would dencrypt the ENC(xxx) settings (that if we actually have any encrypted properties)
 
The application should be up and kicking under 8080 port:
 
> http://localhost:8080/swagger-ui.html

After testing the investor-rest-controller works as expected, we need to configure the Twilio integration.
 
The first step would be to make our application publicly available by deploying it in a PaaS or much easier by using <a href="https://ngrok.com/">ngrok</a>:

> ngrok http 8080

That will expose our application under a certain public URL (forwarding) 

The final step is to buy a number in Twilio and then send to the ngrok forwarding URL the incoming SMSs received in the Twilio number.

The best was to achieve this last is to create a new App (Dashboard > Tools > TwilML Apps) and then assign it to the number's messaging option

Finally, we just send a SMS to that number with a command from the ones above.
