package com.malt.commission.repository;

import com.malt.commission.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {

    Rule findByName(String name);
}
