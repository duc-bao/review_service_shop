package com.ducbao.common.model.enums;

public enum StatusCodeEnum {
    //EXCEPTION
    EXCEPTION("EXCEPTION"), // Exception
    EXCEPTION0400("EXCEPTION0400"), // Bad request
    EXCEPTION0404("EXCEPTION0404"), // Not found
    EXCEPTION0409("EXCEPTION0409"), // Data integrity violation
    EXCEPTION0501("EXCEPTION0501"), // Workflow action not executed
    EXCEPTION0502("EXCEPTION0502"), // Wrong workflow answers
    EXCEPTION0503("EXCEPTION0503"), // Http message not readable
    EXCEPTION0504("EXCEPTION0504"), // Missing servlet request parameter
    EXCEPTION0505("EXCEPTION0505"), // Invalid x-api-key
    EXCEPTION0506("EXCEPTION0506"), // Max size file exception
    EXCEPTION0507("EXCEPTION0507"); // Invalid value input

    public final String value;

    StatusCodeEnum(String value) {
        this.value = value;
    }
}
