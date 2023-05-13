package com.firstapp.braguia.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CookieValidation {
    public static boolean validateCookies(String csrftoken, String sessionid){
        String [] csrfTokenParts = csrftoken.split(";");
        String [] sessionIdParts = sessionid.split(";");
        String csrfExpires = null;
        String sessionExpires = null;

        boolean isCrfValid = false;
        boolean isSessionValid = false;

        for (String part : csrfTokenParts) {
            if (part.trim().startsWith("expires=")) {
                csrfExpires = part.trim().substring("expires=".length());
                break;
            }
        }

        for (String part : sessionIdParts) {
            if (part.trim().startsWith("expires=")) {
                sessionExpires = part.trim().substring("expires=".length());
                break;
            }
        }
        if (csrfExpires != null) {
            isCrfValid = checkExpireDate(csrfExpires);
        }
        if (sessionExpires != null) {
            isSessionValid = checkExpireDate(sessionExpires);
        }

        return isCrfValid && isSessionValid;

    }

    private static boolean checkExpireDate(String cookieDate) {
        if (cookieDate != null) {
            try {
                // parse the expiration time into a Date object
                Date expirationDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH).parse(cookieDate);

                // compare the expiration time with the current time
                if (expirationDate.getTime() > System.currentTimeMillis()) {
                    return true;
                } else {
                    return false;
                }
            } catch (ParseException e) {
                // error parsing the expiration time
                e.printStackTrace();
            }
        }
        return false;
    }



}
