package com.malt.commission.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "application")
@Validated
@Data
public class AppProps {

    // Taux de commissionnement par défaut, si aucune n'a pu être appliquée
    @NotNull
    private Float defaultFee;

    // Configuration pour l'accès IpStack
    @Valid
    @NotNull
    private IpStackProps ipStack;
}
