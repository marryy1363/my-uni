package com.fara.demo.domain.enums;

public enum UserFields {
    FIRSTNAME("firstName"),
    LASTNAME("lastName"),
    USERNAME("username"),
    EMAIL("email");

    private final String attr;

    UserFields(String attr) {
        this.attr = attr;
    }

    public String getAttr() {
        return attr;
    }
}
