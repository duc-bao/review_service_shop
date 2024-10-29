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
    EXCEPTION0507("EXCEPTION0507"), // Invalid value input
    EXCEPTION1001("EXCEPTION1001"), // No login

    //LOGIN
    LOGIN("LOGIN"), // Login
    LOGIN1000("LOGIN1000"),
    LOGIN1001("LOGIN1001"),// Login not success
    LOGIN1002("LOGIN1002"), // Email already exists
    LOGIN1003("LOGIN1003"), // Username already exists
    LOGIN1004("LOGIN1004"), // Phone already exists



    // User
    USER("USER"), // USER
    USER1000("USER1000"), // Save user successfully
    USER1001("USER1001"), // Save user not successfully
    USER1002("USER1002"), // Not found account
    USER1003("USER1003"), // Active account successfully
    USER1004("USER1004"), // Upload image success
    USER1005("USER1005"), // Update user success
    USER1006("USER1006"), // Find by id successfully
    USER1007("USER1007"), // exist email
    // Shop
    SHOP("SHOP"), // SHOP
    SHOP1000("SHOP1000"), // Save shop successfully
    SHOP1001("SHOP1001"), // Save shop not successfully
    SHOP1002("SHOP1002"), // Upload image successfully
    SHOP1003("SHOP1003"), // Not found shop
    SHOP1004("SHOP1004"), // Get by id success
    SHOP1005("SHOP1005"), // update open time successfully
    SHOP1006("SHOP1006"), // get list open time successfully


    // SERVICE
    SERVICE("SERVICE"), // SERVICE
    SERVICE1000("SERVICE1000"), // save service successfully
    SERVICE1001("SERVICE1001"), // save service successfully
    SERVICE1002("SERVICE1002"), // Get all service successfully
    SERVICE1003("SERVICE1003"), // Not found service


    // Category
    CATEGORY("CATEGORY"), // Category
    CATEGORY1000("CATEGORY1000"), // Save category successfully,
    CATEGORY1001("CATEGORY1001"), // Save category not successfully
    CATEGORY1002("CATEGORY1002"), // Not found category
    CATEGORY1003("CATEGORY1003"), // Get List category sucessfully
    CATEGORY1004("CATEGORY1004"), // Get list category with keyword successfully
    CATEGORY1005("CATEGORY1005"), // Get list category with filter successfully
    CATEGORY1006("CATEGORY1006"), // Get list category with filter and keyword successfully

    //Review
    REVIEW("REVIEW"), // REVIEW
    REVIEW1000("REVIEW1000"), // Save review successfully
    REVIEW1001("REVIEW1001"), // Save review not successfully
    REVIEW1002("REVIEW1002"), // Exists shop
    REVIEW1003("REVIEW1003"), // Not found review
    REVIEW1004("REVIEW1004"), // Delete review successfully
    ;
    public final String value;

    StatusCodeEnum(String value) {
        this.value = value;
    }
}
