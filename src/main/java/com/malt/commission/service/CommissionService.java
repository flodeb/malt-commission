package com.malt.commission.service;

import com.malt.commission.config.property.AppProps;
import com.malt.commission.model.Rule;
import com.malt.commission.web.controller.dto.Commission;
import com.malt.commission.web.dto.CommissionRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@AllArgsConstructor
@Slf4j
public class CommissionService {

    private final AppProps appProps;

    private final RuleService ruleService;

    /**
     * Permet de calculer un taux de commissionnement en fonction des règles métiers existantes
     * Une règle s'applique uniquement si toutes ses contraintes sont remplies pra rapport à la mission donnée
     * Si plusieurs règles s'appliquent, le meilleur taux est renvoyé (plus le bas)
     * Si aucune règle ne peut s'appliquer, un taux par défaut est renvoyé
     *
     * @param commissionRequest Informations sur la mission
     * @return Taux de commissionnement et règle éventuellement appliquée
     */
    public Commission calculateComissionRate(CommissionRequest commissionRequest) {
        Rule appliedRule = ruleService.findAll().stream()
                .filter(rule -> rule.getRestrictions().stream().allMatch(restriction -> ruleService.isValidRestriction(restriction, commissionRequest)))
                .min(Comparator.comparing(Rule::getFees))
                .orElse(null);

        Commission commission = Commission.builder().fees(appProps.getDefaultFee()).build();

        if (appliedRule != null) {
            log.debug("Valid rule found : '{}', with final fees {}", appliedRule.getName(), appliedRule.getFees());

            commission.setFees(appliedRule.getFees());
            commission.setReason(appliedRule.getName());
        }

        return commission;
    }
}
