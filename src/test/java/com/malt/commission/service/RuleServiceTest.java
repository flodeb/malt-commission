package com.malt.commission.service;

import com.malt.commission.model.Constraint;
import com.malt.commission.model.Restriction;
import com.malt.commission.model.enums.ConstraineName;
import com.malt.commission.model.enums.ConstraintKey;
import com.malt.commission.model.enums.Operator;
import com.malt.commission.model.enums.RestrictionCondition;
import com.malt.commission.repository.RuleRepository;
import com.malt.commission.web.dto.CommercialRelation;
import com.malt.commission.web.dto.CommissionRequest;
import com.malt.commission.web.dto.Mission;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static com.malt.commission.util.DataUtil.CLIENT_IP;
import static com.malt.commission.util.DataUtil.COUNTRY_ES;
import static com.malt.commission.util.DataUtil.COUNTRY_FR;
import static com.malt.commission.util.DataUtil.FREELANCER_IP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class RuleServiceTest {

    // Data providers
    private static final String OPERATORS_PROVIDER = "peakHoursProvider";

    private static final List<Restriction> FR_LOCATIONS_RESTRICTIONS = Arrays.asList(
            Restriction.builder()
                    .constraint(Constraint.builder()
                            .name(ConstraineName.FREELANCER_LOCATION)
                            .key(ConstraintKey.COUNTRY.name())
                            .value(COUNTRY_FR).build()).build(),
            Restriction.builder()
                    .constraint(Constraint.builder()
                            .name(ConstraineName.CLIENT_LOCATION)
                            .key(ConstraintKey.COUNTRY.name())
                            .value(COUNTRY_FR).build()).build());

    private RuleService ruleService;

    @Mock
    private RuleRepository repository;

    @Mock
    private IpStackService ipStackService;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ruleService = new RuleService(repository, ipStackService);
    }

    @DataProvider(name = OPERATORS_PROVIDER)
    public static Object[][] operatorsProvider() {
        return new Object[][]{
                {Operator.GT, 3, 1, false}, {Operator.GT, 3, 3, false}, {Operator.GT, 3, 5, true},
                {Operator.GE, 3, 1, false}, {Operator.GE, 3, 3, true}, {Operator.GE, 3, 5, true},
                {Operator.LT, 3, 1, true}, {Operator.LT, 3, 3, false}, {Operator.LT, 3, 5, false},
                {Operator.LE, 3, 1, true}, {Operator.LE, 3, 3, true}, {Operator.LE, 3, 5, false},
                {Operator.EQ, 3, 1, false}, {Operator.EQ, 3, 3, true}, {Operator.EQ, 3, 5, false},
                {Operator.NE, 3, 1, true}, {Operator.NE, 3, 3, false}, {Operator.NE, 3, 5, true}
        };
    }

    @Test
    public void shouldFindValidRuleForClientLocation() {
        // Given
        Restriction restriction = Restriction.builder()
                .constraint(Constraint.builder()
                        .name(ConstraineName.CLIENT_LOCATION)
                        .key(ConstraintKey.COUNTRY.name())
                        .value(COUNTRY_FR).build()).build();

        CommissionRequest commissionRequest = CommissionRequest.builder().clientIp(CLIENT_IP).build();

        when(ipStackService.getCountryForIp(CLIENT_IP)).thenReturn(COUNTRY_FR);

        // When
        boolean valid = ruleService.isValidRestriction(restriction, commissionRequest);

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldFindValidRuleForFreelancerLocation() {
        // Given
        Restriction restriction = Restriction.builder()
                .constraint(Constraint.builder()
                        .name(ConstraineName.FREELANCER_LOCATION)
                        .key(ConstraintKey.COUNTRY.name())
                        .value(COUNTRY_FR).build()).build();

        CommissionRequest commissionRequest = CommissionRequest.builder().freelancerIp(FREELANCER_IP).build();

        when(ipStackService.getCountryForIp(FREELANCER_IP)).thenReturn(COUNTRY_FR);

        // When
        boolean valid = ruleService.isValidRestriction(restriction, commissionRequest);

        // Then
        assertThat(valid).isTrue();
    }

    @Test(dataProvider = OPERATORS_PROVIDER)
    public void shouldFindValidRuleForMissionDuration(Operator operator, long restrictionDuration, long missionDuration, boolean isValid) {
        // Given
        Restriction restriction = Restriction.builder()
                .constraint(Constraint.builder()
                        .name(ConstraineName.MISSION_DURATION)
                        .key(operator.name())
                        .value(String.valueOf(restrictionDuration)).build()).build();

        CommissionRequest commissionRequest = CommissionRequest.builder().mission(Mission.builder().duration(missionDuration).build()).build();

        // When
        boolean valid = ruleService.isValidRestriction(restriction, commissionRequest);

        // Then
        assertThat(valid).isEqualTo(isValid);
    }

    @Test(dataProvider = OPERATORS_PROVIDER)
    public void shouldFindValidRuleForCommercialRelationDuration(Operator operator, long restrictionDuration, long missionDuration, boolean isValid) {
        // Given
        Restriction restriction = Restriction.builder()
                .constraint(Constraint.builder()
                        .name(ConstraineName.COMMERCIAL_RELATION_DURATION)
                        .key(operator.name())
                        .value(String.valueOf(restrictionDuration)).build()).build();

        ZonedDateTime date = ZonedDateTime.parse("2000-01-01T09:00:00.00Z");
        CommissionRequest commissionRequest = CommissionRequest.builder().commercialRelation(CommercialRelation.builder()
                .firstMission(date)
                .lastMission(date.plusDays(missionDuration)).build()).build();

        // When
        boolean valid = ruleService.isValidRestriction(restriction, commissionRequest);

        // Then
        assertThat(valid).isEqualTo(isValid);
    }

    @Test
    public void shouldNotFindValidRuleForOrRestrictions() {
        // Given
        Restriction restriction = Restriction.builder()
                .condition(RestrictionCondition.OR)
                .restrictions(FR_LOCATIONS_RESTRICTIONS).build();

        CommissionRequest commissionRequest = CommissionRequest.builder().clientIp(CLIENT_IP).freelancerIp(CLIENT_IP).build();

        when(ipStackService.getCountryForIp(CLIENT_IP)).thenReturn(COUNTRY_ES);
        when(ipStackService.getCountryForIp(FREELANCER_IP)).thenReturn(COUNTRY_ES);

        // When
        boolean valid = ruleService.isValidRestriction(restriction, commissionRequest);

        // Then
        assertThat(valid).isFalse();
    }

    @Test
    public void shouldFindValidRuleForOrRestrictions() {
        // Given
        Restriction restriction = Restriction.builder()
                .condition(RestrictionCondition.OR)
                .restrictions(FR_LOCATIONS_RESTRICTIONS).build();

        CommissionRequest commissionRequest = CommissionRequest.builder().clientIp(CLIENT_IP).freelancerIp(CLIENT_IP).build();

        when(ipStackService.getCountryForIp(CLIENT_IP)).thenReturn(COUNTRY_FR);
        when(ipStackService.getCountryForIp(FREELANCER_IP)).thenReturn(COUNTRY_ES);

        // When
        boolean valid = ruleService.isValidRestriction(restriction, commissionRequest);

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldNotFindValidRuleForAndRestrictions() {
        // Given
        Restriction restriction = Restriction.builder()
                .condition(RestrictionCondition.AND)
                .restrictions(FR_LOCATIONS_RESTRICTIONS).build();

        CommissionRequest commissionRequest = CommissionRequest.builder().clientIp(CLIENT_IP).freelancerIp(CLIENT_IP).build();

        when(ipStackService.getCountryForIp(CLIENT_IP)).thenReturn(COUNTRY_ES);
        when(ipStackService.getCountryForIp(FREELANCER_IP)).thenReturn(COUNTRY_ES);

        // When
        boolean valid = ruleService.isValidRestriction(restriction, commissionRequest);

        // Then
        assertThat(valid).isFalse();
    }

    @Test
    public void shouldFindValidRuleForAndRestrictions() {
        // Given
        Restriction restriction = Restriction.builder()
                .condition(RestrictionCondition.AND)
                .restrictions(FR_LOCATIONS_RESTRICTIONS).build();

        CommissionRequest commissionRequest = CommissionRequest.builder().clientIp(CLIENT_IP).freelancerIp(CLIENT_IP).build();

        when(ipStackService.getCountryForIp(CLIENT_IP)).thenReturn(COUNTRY_FR);
        when(ipStackService.getCountryForIp(FREELANCER_IP)).thenReturn(COUNTRY_FR);

        // When
        boolean valid = ruleService.isValidRestriction(restriction, commissionRequest);

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldFindValidRuleForAndNestedRestrictions() {
        // Given
        /*
         * OR :
         *   - Mission.duration > 20
         *   - AND :
         *     - Mission.duration > 10
         *     - CommercialRelation.duration < 3
         */
        Restriction restriction = Restriction.builder()
                .condition(RestrictionCondition.OR)
                .restrictions(Arrays.asList(
                        Restriction.builder()
                                .constraint(Constraint.builder()
                                        .name(ConstraineName.MISSION_DURATION)
                                        .key(Operator.GT.name())
                                        .value("20").build())
                                .build(),
                        Restriction.builder()
                                .condition(RestrictionCondition.AND)
                                .restrictions(Arrays.asList(
                                        Restriction.builder()
                                                .constraint(Constraint.builder()
                                                        .name(ConstraineName.MISSION_DURATION)
                                                        .key(Operator.GT.name())
                                                        .value("10").build())
                                                .build(),
                                        Restriction.builder()
                                                .constraint(Constraint.builder()
                                                        .name(ConstraineName.COMMERCIAL_RELATION_DURATION)
                                                        .key(Operator.LT.name())
                                                        .value("3").build())
                                                .build()))
                                .build()))
                .build();

        CommissionRequest commissionRequest = CommissionRequest.builder()
                .clientIp(CLIENT_IP)
                .freelancerIp(CLIENT_IP)
                .mission(Mission.builder()
                        .duration(15L).build())
                .commercialRelation(CommercialRelation.builder()
                        .firstMission(ZonedDateTime.parse("2000-01-01T09:00:00.00Z"))
                        .lastMission(ZonedDateTime.parse("2000-01-02T09:00:00.00Z"))
                        .build())
                .build();

        when(ipStackService.getCountryForIp(CLIENT_IP)).thenReturn(COUNTRY_FR);
        when(ipStackService.getCountryForIp(FREELANCER_IP)).thenReturn(COUNTRY_FR);

        // When
        boolean valid = ruleService.isValidRestriction(restriction, commissionRequest);

        // Then
        assertThat(valid).isTrue();
    }
}