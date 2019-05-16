package com.malt.commission.web.controller;

import com.malt.commission.CommissionApplication;
import com.malt.commission.config.property.AppProps;
import com.malt.commission.service.CommissionService;
import com.malt.commission.util.TestUtil;
import com.malt.commission.web.dto.CommissionRequest;
import com.malt.commission.web.error.RestExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.malt.commission.util.DataUtil.CLIENT_IP;
import static com.malt.commission.util.DataUtil.FREELANCER_IP;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CommissionApplication.class)
public class CommissionControllerIntTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @Autowired
    private AppProps appProps;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        CommissionController commissionController = new CommissionController(commissionService);
        mockMvc = MockMvcBuilders.standaloneSetup(commissionController)
                .setControllerAdvice(restExceptionHandler)
                .build();
    }

    @Test
    public void shouldCalculateCommission() throws Exception {
        // Given
        CommissionRequest commission = CommissionRequest.builder().clientIp(CLIENT_IP).freelancerIp(FREELANCER_IP).build();

        // When
        ResultActions result = mockMvc.perform(post("/api/commissions/calculateRate")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commission)));

        // When
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.fees").value(appProps.getDefaultFee()));
    }
}
