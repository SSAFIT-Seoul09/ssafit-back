package com.ssafy.ssafit.video.domain.model;

import com.ssafy.ssafit.video.exception.InvalidVideoParameterException;

public enum VideoPart {
    ARM("ARM"),
    LEG("LEG"),
    BACK("BACK"),
    CHEST("CHEST"),
    FULL_BODY("FULL_BODY");

    private final String value;

    VideoPart(String value) {
        this.value = value;
    }

    public static VideoPart from(String value) {
        for (VideoPart part : VideoPart.values()) {
            if (part.getValue().equalsIgnoreCase(value)) {
                return part;
            }
        }
        throw InvalidVideoParameterException.ofPart(value);
    }


    public String getValue() {
        return value;
    }
}
