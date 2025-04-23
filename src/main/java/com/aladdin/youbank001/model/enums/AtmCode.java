package com.aladdin.youbank001.model.enums;

import lombok.Getter;

@Getter
public enum AtmCode {
    YOUBANK("A2605"),
    BIRBANK("K4169"),
    UNIBANK("U2363"),
    ABBANK("A9940");

    private final String code;

    AtmCode(String code) {
        this.code = code;
    }


}
