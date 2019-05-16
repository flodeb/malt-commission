package com.malt.commission.validation;

import com.malt.commission.web.dto.CommercialRelation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DatesValidator implements ConstraintValidator<ValidDates, Object> {
    @Override
    public void initialize(ValidDates constraint) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof CommercialRelation) {
            CommercialRelation commercialRelation = (CommercialRelation) obj;
            return commercialRelation.getFirstMission().isBefore(commercialRelation.getLastMission());
        }

        return true;
    }
}
