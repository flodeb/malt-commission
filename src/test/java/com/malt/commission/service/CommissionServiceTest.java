package com.malt.commission.service;

import com.malt.commission.config.property.AppProps;
import com.malt.commission.model.Constraint;
import com.malt.commission.model.Restriction;
import com.malt.commission.model.Rule;
import com.malt.commission.model.enums.ConstraineName;
import com.malt.commission.model.enums.ConstraintKey;
import com.malt.commission.model.enums.Operator;
import com.malt.commission.web.controller.dto.Commission;
import com.malt.commission.web.dto.CommissionRequest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class CommissionServiceTest {

    private static final Float DEFAULT_FEE = 8.5F;

    private static final CommissionRequest COMMISSION_REQUEST = CommissionRequest.builder().build();

    private static final Restriction MISSION_DURATION_RESTRICTION = Restriction.builder().constraint(Constraint.builder() // mission.duration > 10 days
            .name(ConstraineName.MISSION_DURATION)
            .key(Operator.GT.name())
            .value("10").build()).build();

    private static final Restriction CLIENT_LOCATION_DURATION_RESTRICTION = Restriction.builder().constraint(Constraint.builder() // client.country = ES
            .name(ConstraineName.CLIENT_LOCATION)
            .key(ConstraintKey.COUNTRY.name())
            .value("ES").build()).build();

    private static final Rule MISSION_DURATION_RULE = Rule.builder()
            .fees(4F)
            .name("Rule 1")
            .restrictions(Collections.singletonList(MISSION_DURATION_RESTRICTION)).build();

    private static final Rule CLIENT_LOCATION_DURATION_RULE = Rule.builder()
            .fees(2.5F)
            .name("Rule 2")
            .restrictions(Collections.singletonList(CLIENT_LOCATION_DURATION_RESTRICTION)).build();

    private CommissionService commissionService;

    @Mock
    private AppProps appProps;

    @Mock
    private RuleService ruleService;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(appProps.getDefaultFee()).thenReturn(DEFAULT_FEE);

        commissionService = new CommissionService(appProps, ruleService);
    }

    @Test
    public void shouldReturnDefaultFeeForNoSpecificRule() {
        // Given

        // When
        Commission commission = commissionService.calculateComissionRate(null);

        // Then
        assertThat(commission.getFees()).isEqualTo(DEFAULT_FEE);
        assertThat(commission.getReason()).isNull();
    }

    @Test
    public void shouldReturnDefaultFeeForNoAppliedRule() {
        // Given
        when(ruleService.findAll()).thenReturn(Collections.singletonList(MISSION_DURATION_RULE));
        when(ruleService.isValidRestriction(MISSION_DURATION_RESTRICTION, COMMISSION_REQUEST)).thenReturn(false);

        // When
        Commission commission = commissionService.calculateComissionRate(COMMISSION_REQUEST);

        // Then
        assertThat(commission.getFees()).isEqualTo(DEFAULT_FEE);
        assertThat(commission.getReason()).isNull();
    }

    @Test
    public void shouldReturnFeeForAppliedRule() {
        // Given
        when(ruleService.findAll()).thenReturn(Collections.singletonList(MISSION_DURATION_RULE));
        when(ruleService.isValidRestriction(MISSION_DURATION_RESTRICTION, COMMISSION_REQUEST)).thenReturn(true);

        // When
        Commission commission = commissionService.calculateComissionRate(COMMISSION_REQUEST);

        // Then
        assertThat(commission.getFees()).isEqualTo(MISSION_DURATION_RULE.getFees());
        assertThat(commission.getReason()).isEqualTo(MISSION_DURATION_RULE.getName());
    }

    @Test
    public void shouldReturnBestFeeForMultipleAppliedRules() {
        // Given
        when(ruleService.findAll()).thenReturn(Arrays.asList(MISSION_DURATION_RULE, CLIENT_LOCATION_DURATION_RULE));
        when(ruleService.isValidRestriction(any(), eq(COMMISSION_REQUEST))).thenReturn(true);

        // When
        Commission commission = commissionService.calculateComissionRate(COMMISSION_REQUEST);

        // Then
        assertThat(commission.getFees()).isEqualTo(CLIENT_LOCATION_DURATION_RULE.getFees());
        assertThat(commission.getReason()).isEqualTo(CLIENT_LOCATION_DURATION_RULE.getName());
    }
}