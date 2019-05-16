package com.malt.commission.service;

import com.malt.commission.model.Constraint;
import com.malt.commission.model.Restriction;
import com.malt.commission.model.Rule;
import com.malt.commission.model.enums.ConstraineName;
import com.malt.commission.model.enums.Operator;
import com.malt.commission.model.enums.RestrictionCondition;
import com.malt.commission.repository.RuleRepository;
import com.malt.commission.web.dto.CommissionRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RuleService {

    private final RuleRepository repository;

    private final IpStackService ipStackService;

    public Rule save(Rule rule) {
        return repository.save(rule);
    }

    public List<Rule> findAll() {
        return repository.findAll();
    }

    public Rule findByName(String name) {
        return repository.findByName(name);
    }

    /**
     * Permet de vérifier si un restriction est valide en fonction d'une requête de calcul
     *
     * @param restriction       Restriction sur une règle
     * @param commissionRequest Informations sur la demande de calcul
     * @return vrai si la restriction est valide, faux sinon
     */
    public boolean isValidRestriction(Restriction restriction, CommissionRequest commissionRequest) {
        boolean isValid = false;

        if (restriction.getCondition() == null) {
            Constraint constraint = restriction.getConstraint();
            if (constraint != null) {
                ConstraineName constraintName = constraint.getName();

                if (constraintName == ConstraineName.CLIENT_LOCATION) {
                    isValid = checkCountryRestriction(constraint.getValue(), commissionRequest.getClientIp());
                } else if (constraintName == ConstraineName.FREELANCER_LOCATION) {
                    isValid = checkCountryRestriction(constraint.getValue(), commissionRequest.getFreelancerIp());
                } else if (constraintName == ConstraineName.MISSION_DURATION) {
                    if (commissionRequest.getMission() != null) {
                        isValid = checkDurationRestriction(
                                Operator.valueOf(constraint.getKey()),
                                Long.valueOf(constraint.getValue()),
                                commissionRequest.getMission().getDuration());
                    }
                } else if (constraintName == ConstraineName.COMMERCIAL_RELATION_DURATION) {
                    if (commissionRequest.getCommercialRelation() != null) {
                        isValid = checkDurationRestriction(
                                Operator.valueOf(constraint.getKey()),
                                Long.valueOf(constraint.getValue()),
                                commissionRequest.getCommercialRelation().getDuration());
                    }
                }
            } else {
                log.warn("Final restriction {} has no constraint", restriction.getId());
            }
        } else if (restriction.getCondition() == RestrictionCondition.OR) {
            isValid = restriction.getRestrictions().stream().anyMatch(childRestriction -> isValidRestriction(childRestriction, commissionRequest));
        } else if (restriction.getCondition() == RestrictionCondition.AND) {
            isValid = restriction.getRestrictions().stream().allMatch(childRestriction -> isValidRestriction(childRestriction, commissionRequest));
        }

        return isValid;
    }

    private boolean checkCountryRestriction(String restrictionCountry, String userIp) {
        String foundCountry = ipStackService.getCountryForIp(userIp);

        log.info("Country {} found for IP {}", foundCountry, userIp);

        return restrictionCountry.equals(foundCountry);
    }

    private boolean checkDurationRestriction(Operator operator, Long restrictionDays, Long duration) {
        if (operator == Operator.EQ) {
            return duration.equals(restrictionDays);
        }
        if (operator == Operator.GT) {
            return duration > restrictionDays;
        }
        if (operator == Operator.GE) {
            return duration >= restrictionDays;
        }
        if (operator == Operator.LT) {
            return duration < restrictionDays;
        }
        if (operator == Operator.LE) {
            return duration <= restrictionDays;
        }
        if (operator == Operator.NE) {
            return !duration.equals(restrictionDays);
        }

        return false;
    }
}

