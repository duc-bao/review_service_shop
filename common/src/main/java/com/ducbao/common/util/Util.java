package com.ducbao.common.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.UUID;

@UtilityClass
public class Util {
    public boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

//    public String generateRedisKey(String... arg) {
//        return String.join(RedisConstant.SEPARATOR, arg);
//    }
//
//    public String generateUUID() { return UUID.randomUUID().toString(); }
//
//    public String generateFileDirectory(String... arg) {
//        return String.join(FileConstant.DIRECTORY_DIVIDE, arg);
//    }
//
//    public String generateAuthorizationCode() {
//        int length = 60;
//        byte[] randomBytes = new byte[length];
//        SecureRandom secureRandom = new SecureRandom();
//        secureRandom.nextBytes(randomBytes);
//
//        String authorizationCode = Base64.encodeBase64URLSafeString(randomBytes);
//
//        return authorizationCode;
//    }
}