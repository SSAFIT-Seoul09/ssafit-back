package com.ssafy.ssafit.video.domain.model;

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

    public String getValue() {
        return value;
    }
}
