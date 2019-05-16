package com.malt.commission.web.controller;

import com.malt.commission.CommissionApplication;
import com.malt.commission.model.Constraint;
import com.malt.commission.model.Restriction;
import com.malt.commission.model.Rule;
import com.malt.commission.model.enums.ConstraineName;
import com.malt.commission.model.enums.ConstraintKey;
import com.malt.commission.repository.RuleRepository;
import com.malt.commission.service.RuleService;
import com.malt.commission.util.TestUtil;
import com.malt.commission.web.error.ErrorCode;
import com.malt.commission.web.error.RestExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;

import static com.malt.commission.util.DataUtil.COUNTRY_FR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CommissionApplication.class)
public class RuleControllerIntTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        RuleController ruleController = new RuleController(ruleService);
        mockMvc = MockMvcBuilders.standaloneSetup(ruleController)
                .setControllerAdvice(restExceptionHandler)
                .build();
    }

    @Test
    public void shouldNotCreateRuleForExistingRuleName() throws Exception {
        // Given
        Rule rule = Rule.builder()
                .name("Existing rule name")
                .fees(42.42F)
                .restrictions(Collections.singletonList(Restriction.builder().constraint(Constraint.builder()
                        .name(ConstraineName.CLIENT_LOCATION)
                        .key(ConstraintKey.COUNTRY.name())
                        .value(COUNTRY_FR).build()).build())).build();

        ruleService.save(rule);

        // When
        ResultActions result = mockMvc.perform(post("/api/admin/rules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rule)));

        // When
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.EXISTING_ENTITY.getCode()))
                .andExpect(jsonPath("$.message").value(String.format(ErrorCode.EXISTING_ENTITY.getMessage(), Rule.class.getSimpleName(), rule.getName())));
    }

    @Test
    public void shouldCreateRule() throws Exception {
        // Given
        Rule rule = Rule.builder()
                .name("First rule")
                .fees(42.42F)
                .restrictions(Collections.singletonList(Restriction.builder().constraint(Constraint.builder()
                        .name(ConstraineName.CLIENT_LOCATION)
                        .key(ConstraintKey.COUNTRY.name())
                        .value(COUNTRY_FR).build()).build())).build();

        // When
        ResultActions result = mockMvc.perform(post("/api/admin/rules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rule)));

        // When
        result.andExpect(status().isOk());
    }
}
