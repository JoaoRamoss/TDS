package com.firstapp.braguia.Utils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

// Used to format raw cookies in strings we can use to make requests.
public class CookieOven {

    public static List<String> formatCookiesForStorage(Response<ResponseBody> rawCookies) {
        List<String> cookies = new ArrayList<>();
        for (int i = 0; i < rawCookies.headers().size(); i++) {
            String headerName = rawCookies.headers().name(i);
            String headerValue = rawCookies.headers().value(i);
            if (headerName.equalsIgnoreCase("Set-Cookie")) {
                cookies.add(headerValue);
            }
        }
        return cookies;
    }


    public static String extractCsrfTokenValue (String cookie) {
        String[] parts = cookie.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("csrftoken" + "=")) {
                return part.substring(("csrftoken" + "=").length());
            }
        }
        return null; // Cookie not found
    }

    public static String extractSessionIdValue (String cookie) {
        String[] parts = cookie.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("sessionid" + "=")) {
                return part.substring(("sessionid" + "=").length());
            }
        }
        return null; // Cookie not found
    }

    public static String getFormatedCookies(String csrf, String session) {
        StringBuilder sb = new StringBuilder("");
        sb.append("csrftoken=").append(csrf)
                .append(";").append("sessionid=").append(session);
        return sb.toString();
    }


}
