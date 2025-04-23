package com.aladdin.youbank001.model.enums;

import lombok.Getter;

@Getter
public enum BankCode {
    YOUBANK("2605"),
    BIRBANK("4169"),
    UNIBANK("2363"),
    ABBANK ("9940");

    private final String code;

    BankCode(String code) {
        this.code = code;
    }

}
