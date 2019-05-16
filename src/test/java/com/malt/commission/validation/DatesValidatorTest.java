package com.malt.commission.validation;

import com.malt.commission.web.dto.CommercialRelation;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DatesValidatorTest {

    private DatesValidator datesValidator = new DatesValidator();

    @Test
    public void shouldReturnFalseForInvalidDates() {
        // Given
        CommercialRelation commercialRelation = CommercialRelation.builder()
                .firstMission(ZonedDateTime.parse("2000-01-01T09:00:00.00Z"))
                .lastMission(ZonedDateTime.parse("1990-01-01T09:00:00.00Z")).build();

        // When
        boolean valid = datesValidator.isValid(commercialRelation, null);

        // Then
        assertThat(valid).isFalse();
    }

    @Test
    public void shouldReturnTrueForValidDates() {
        // Given
        CommercialRelation commercialRelation = CommercialRelation.builder()
                .firstMission(ZonedDateTime.parse("2000-01-01T09:00:00.00Z"))
                .lastMission(ZonedDateTime.parse("2010-01-02T09:00:00.00Z")).build();

        // When
        boolean valid = datesValidator.isValid(commercialRelation, null);

        // Then
        assertThat(valid).isTrue();
    }
}