package com.malt.commission.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.malt.commission.model.enums.RestrictionCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
public class Restriction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty("Condition éventuelle de la restriction, pour appliquer un sous-ensemble de restrictions")
    private RestrictionCondition condition;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @ApiModelProperty("Sous-ensemble de restrictions éventuel. Uniquement si une condition a été remplie sur la règle")
    private List<Restriction> restrictions = new ArrayList<>();

    @Embedded
    @ApiModelProperty("La contrainte finale de règle. Uniquement si aucune condition n'a été remplie sur la règle")
    private Constraint constraint;
}
