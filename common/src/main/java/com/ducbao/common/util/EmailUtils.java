package com.ducbao.common.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class EmailUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    public boolean isEmail(String email) {
        if(Util.isNullOrEmpty(email) || email.length() < 3 || email.length() > 50){
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
