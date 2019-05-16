package com.malt.commission.config.property;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

@Data
public class IpStackProps {

    // URL de l'API IpStack
    @URL
    @NotEmpty
    private String url;

    // Clé d'accès à IpStack
    @NotEmpty
    private String accessKey;
}
