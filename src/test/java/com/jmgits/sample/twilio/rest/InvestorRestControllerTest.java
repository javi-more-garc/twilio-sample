package com.jmgits.sample.twilio.rest;

import com.jmgits.sample.twilio.service.InvestorService;
import com.jmgits.sample.twilio.support.MessageBodySupport;
import com.jmgits.sample.twilio.view.PortfolioDetail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_XHTML_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by javi.more.garc on 25/09/16.
 *
 * Testing for {@link InvestorRestController}
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvestorRestControllerTest {

    @Inject
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @MockBean
    private InvestorService mockInvestorService;

    @MockBean
    private MessageBodySupport mockMessageBodySupport;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testSmsOk() throws Exception{

        PortfolioDetail portfolioDetail = new PortfolioDetail();

        //
        // given

        given(mockInvestorService.getPortfolioDetail("myNumber")).willReturn(portfolioDetail);

        given(mockMessageBodySupport.generateMarketValueMessageBody(portfolioDetail)).willReturn("Test body");

        //
        // perform

        mockMvc.perform(post("/api/v1/investor/sms?From=myNumber&Body=What is the value of my portfolio?")
                .contentType(APPLICATION_XHTML_XML)) //
                .andDo(print()) //
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().xml("<Response><Message><Body>Test body</Body></Message></Response>"))
        ;

    }

    @Test
    public void testSmsOkCommandNotFound() throws Exception{

        //
        // given

        given(mockMessageBodySupport.generateErrorUnknownCommandBody(Mockito.anyList())).willReturn("Test unknown command body");

        //
        // perform

        mockMvc.perform(post("/api/v1/investor/sms?From=myNumber&Body=Unknown command")
                .contentType(APPLICATION_XHTML_XML)) //
                .andDo(print()) //
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().xml("<Response><Message><Body>Test unknown command body</Body></Message></Response>"))
        ;

    }

    @Test
    public void testSmsKoPortfolioProblem() throws Exception{

        //
        // given

        given(mockInvestorService.getPortfolioDetail("myNumber")).willThrow(new RuntimeException("Test exception"));

        given(mockMessageBodySupport.generateGeneralErrorBody()).willReturn("Test error body");

        //
        // perform

        mockMvc.perform(post("/api/v1/investor/sms?From=myNumber&Body=What is the value of my portfolio?")
                .contentType(APPLICATION_XHTML_XML)) //
                .andDo(print()) //
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().xml("<Response><Message><Body>Test error body</Body></Message></Response>"))
        ;

    }

}
