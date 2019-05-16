package com.malt.commission.service;

import com.malt.commission.client.IpStackClient;
import com.malt.commission.client.dto.IpStackResponse;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClientException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.malt.commission.util.DataUtil.CLIENT_IP;
import static com.malt.commission.util.DataUtil.COUNTRY_FR;
import static liquibase.util.SystemUtils.USER_COUNTRY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class IpStackServiceTest {

    private IpStackService ipStackService;

    @Mock
    private IpStackClient ipStackClient;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ipStackService = new IpStackService(ipStackClient);
    }

    @Test
    public void shouldGetCountryForIp() {
        // Given
        IpStackResponse ipStackResponse = IpStackResponse.builder().countryCode(COUNTRY_FR).build();

        when(ipStackClient.findForIp(CLIENT_IP)).thenReturn(ipStackResponse);

        // When
        String country = ipStackService.getCountryForIp(CLIENT_IP);

        // Then
        assertThat(country).isEqualTo(USER_COUNTRY);
    }

    @Test
    public void shouldGetNullCountryWhenRestException() {
        // Given
        when(ipStackClient.findForIp(CLIENT_IP)).thenThrow(new RestClientException("Rest Error"));

        // When
        String country = ipStackService.getCountryForIp(CLIENT_IP);

        // Then
        assertThat(country).isNull();
    }
}