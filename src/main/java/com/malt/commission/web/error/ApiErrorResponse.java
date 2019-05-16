package com.malt.commission.web.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public
class ApiErrorResponse {

    @NotNull
    @ApiModelProperty(value = "Code fonctionel de l'erreur", required = true)
    private Integer code;

    @NotEmpty
    @ApiModelProperty(value = "Message d'erreur global", required = true)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    @ApiModelProperty("Liste des erreurs spécifique éventuelles")
    private List<FieldError> fieldErrors;

    @Data
    @AllArgsConstructor
    static final class FieldError {

        @ApiModelProperty(value = "Nom du champ erreur", required = true)
        @NotEmpty
        private String field;

        @ApiModelProperty(value = "Message d'erreur sur le champ", required = true)
        @NotEmpty
        private String message;
    }
}
