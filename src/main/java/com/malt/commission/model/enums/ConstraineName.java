package com.malt.commission.model.enums;

import lombok.Getter;

@Getter
public enum ConstraineName {

    CLIENT_LOCATION("client.location"),
    FREELANCER_LOCATION("freelancer.location"),
    MISSION_DURATION("mission.duration"),
    COMMERCIAL_RELATION_DURATION("commercialrelation.duration");

    private final String name;

    ConstraineName(String name) {
        this.name = name;
    }

    public static ConstraineName findByName(String name) {
        for (ConstraineName value : values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }

        return null;
    }
}
