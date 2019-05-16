package com.malt.commission.web.controller;

import com.malt.commission.service.CommissionService;
import com.malt.commission.web.controller.dto.Commission;
import com.malt.commission.web.dto.CommissionRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/commissions")
@RequiredArgsConstructor
public class CommissionController {

    private final CommissionService commissionService;

    @PostMapping("/calculateRate")
    @ApiOperation("Calcul d'un taux de commission en fonction des règles métiers de pricing")
    public Commission calculateCommissionRate(
            @ApiParam(value = "Informations permettant un calcul du taux de commissionnement", required = true) @Valid @RequestBody CommissionRequest commissionRequest) {
        return commissionService.calculateComissionRate(commissionRequest);
    }
}
