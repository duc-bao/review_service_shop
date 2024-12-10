package com.ducbao.common.exception;

public class UserDoesNotHavePermission extends RuntimeException{
    public UserDoesNotHavePermission(String message) {
        super(message);
    }
}
