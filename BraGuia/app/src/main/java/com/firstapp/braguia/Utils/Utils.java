package com.firstapp.braguia.Utils;

import android.content.Context;

import androidx.annotation.RawRes;

import com.firstapp.braguia.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Utils {

    public String readRawResource(@RawRes int res, Context context) {
        return readStream(context.getResources().openRawResource(res));
    }

    private static String readStream(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray loadSONFile(Context ctx) {
        String filecontent = null;
        JSONArray jo = new JSONArray();
        try {
            filecontent = readStream(ctx.getResources().openRawResource(R.raw.trails));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return new JSONArray(filecontent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONArray(jsonText);
        }
    }
}