package com.malt.commission.validation;

import liquibase.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IpAddressValidator implements ConstraintValidator<IpAddress, String> {

    private static final Pattern IP_PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    @Override
    public boolean isValid(String ip, ConstraintValidatorContext context) {
        return StringUtils.isNotEmpty(ip) && IP_PATTERN.matcher(ip).matches();
    }
}
