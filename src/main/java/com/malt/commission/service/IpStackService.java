package com.malt.commission.service;

import com.malt.commission.client.IpStackClient;
import com.malt.commission.client.dto.IpStackResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
@AllArgsConstructor
@Slf4j
public class IpStackService {

    private final IpStackClient ipStackClient;

    public String getCountryForIp(String ip) {
        String country = null;

        try {
            IpStackResponse ipStackResponse = ipStackClient.findForIp(ip);
            country = ipStackResponse.getCountryCode();
        } catch (RestClientException ex) {
            log.error("Impossible to get country for IP {} from IPStack", ip, ex);
        }

        return country;
    }
}
