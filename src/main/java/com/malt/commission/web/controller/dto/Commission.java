package com.malt.commission.web.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class Commission {

    @NotNull
    @ApiModelProperty(value = "Taux de commissionnement calculé", required = true)
    private Float fees;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty("Nom de la règle appliquée. Non retourné si aucune règle n'a pu être appliquée")
    private String reason;
}
