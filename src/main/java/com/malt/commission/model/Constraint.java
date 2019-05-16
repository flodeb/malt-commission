package com.malt.commission.model;

import com.malt.commission.model.enums.ConstraineName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
public class Constraint {

    @Column(name = "constraint_name")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("Nom de la contrainte")
    private ConstraineName name;

    @Column(name = "constraint_key")
    @ApiModelProperty("Clé de la contrainte. Doit correspondre à un ConstraintKey ou un Operator")
    private String key; // ConstraintKey ou Operator

    @Column(name = "constraint_value")
    @ApiModelProperty("Valeur de la contrainte, dont la mission doit correspondre pour que la contrainte soit valide")
    private String value;
}
