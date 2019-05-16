package com.malt.commission.web.dto;

import com.malt.commission.validation.IpAddress;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
public class CommissionRequest {

    @NotEmpty
    @IpAddress
    @ApiModelProperty(value = "Adresse IP du client", required = true)
    private String clientIp;

    @NotEmpty
    @IpAddress
    @ApiModelProperty(value = "Adresse IP du freelancer", required = true)
    private String freelancerIp;

    @Valid
    @ApiModelProperty("Informations sur la mission")
    private Mission mission;

    @Valid
    @ApiModelProperty("Informations sur la relation commerciale existante entre le freelancer et l'entreprise")
    private CommercialRelation commercialRelation;
}
