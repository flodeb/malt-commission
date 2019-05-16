package com.malt.commission.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.malt.commission.validation.ValidDates;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@ValidDates
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
public class CommercialRelation {

    @NotNull
    @ApiModelProperty(value = "Date de la première mission au format ISO 8601", required = true)
    private ZonedDateTime firstMission;

    @NotNull
    @ApiModelProperty(value = "Date de la dernière mission au format ISO 8601", required = true)
    private ZonedDateTime lastMission;

    @JsonIgnore
    public long getDuration() {
        return ChronoUnit.DAYS.between(firstMission, lastMission);
    }
}
