package com.malt.commission.client;

import com.malt.commission.client.dto.IpStackResponse;
import com.malt.commission.config.property.AppProps;
import com.malt.commission.config.property.IpStackProps;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@AllArgsConstructor
public class IpStackClient {

    private final AppProps appProps;

    public IpStackResponse findForIp(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        IpStackProps ipStack = appProps.getIpStack();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ipStack.getUrl() + "/" + ip).queryParam("access_key", ipStack.getAccessKey());

        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, IpStackResponse.class).getBody();
    }
}
