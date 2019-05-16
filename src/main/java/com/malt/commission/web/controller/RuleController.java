package com.malt.commission.web.controller;

import com.malt.commission.model.Rule;
import com.malt.commission.service.RuleService;
import com.malt.commission.web.error.ApiErrorResponse;
import com.malt.commission.web.error.exception.ExistingEntityException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    @PostMapping
    @ApiOperation("Création d'une règle de pricing")
    @ApiResponses(@ApiResponse(code = 400, message = "Une règle avec le même nom existe déjà. CODE : 2", response = ApiErrorResponse.class))
    public Rule create(@ApiParam(value = "Règle de pricing", required = true) @Valid @RequestBody Rule rule) {
        Rule existingRule = ruleService.findByName(rule.getName());
        if (existingRule != null) {
            throw new ExistingEntityException(Rule.class, rule.getName());
        }

        return ruleService.save(rule);
    }
}
