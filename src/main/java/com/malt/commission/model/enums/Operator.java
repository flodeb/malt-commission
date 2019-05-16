package com.malt.commission.model.enums;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

@Getter
@ApiModel("Opérateur pour une contrainte")
public enum Operator {
    GT,
    GE,
    LT,
    LE,
    EQ,
    NE
}
