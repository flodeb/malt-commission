package com.malt.commission.validation;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IpAddressValidatorTest {

    private IpAddressValidator ipAddressValidator = new IpAddressValidator();

    @Test
    public void shouldReturnFalseForInvalidIpAddress() {
        // Given
        String ip = "Invalid";

        // When
        boolean valid = ipAddressValidator.isValid(ip, null);

        // Then
        assertThat(valid).isFalse();
    }

    @Test
    public void shouldReturnTrueForValidIpAddress() {
        // Given
        String ip = "217.127.206.227";

        // When
        boolean valid = ipAddressValidator.isValid(ip, null);

        // Then
        assertThat(valid).isTrue();
    }
}