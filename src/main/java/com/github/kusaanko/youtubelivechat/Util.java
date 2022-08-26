package com.github.kusaanko.youtubelivechat;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("unchecked")
public class Util {
    private static Gson gson;

    static {
        gson = new Gson();
    }

    public static String toJSON(Map<String, Object> json) {
        StringBuilder js = new StringBuilder();
        js.append("{");
        for (String key : json.keySet()) {
            js.append("'").append(key).append("': ");
            Object d = json.get(key);
            if (d instanceof Byte ||
                    d instanceof Character ||
                    d instanceof Short ||
                    d instanceof Integer ||
                    d instanceof Long ||
                    d instanceof Float ||
                    d instanceof Double ||
                    d instanceof Boolean) {
                js.append(d);
            } else if (d instanceof Map) {
                js.append(toJSON((Map<String, Object>) d));
            } else {
                js.append("\"").append(d.toString().replace("\"", "\\\"").replace("\\", "\\\\")).append("\"");
            }
            js.append(", ");
        }
        return js.substring(0, js.length() - 2) + "}";
    }

    public static Map<String, Object> toJSON(String json) {
        if (!json.startsWith("{")) {
            throw new IllegalArgumentException("This is not json(map)!");
        }
        Map<String, Object> result = gson.fromJson(json, Map.class);
        return result;
    }

    public static Map<String, Object> getJSONMap(Map<String, Object> json, String... keys) {
        Map<String, Object> map = json;
        for (String key : keys) {
            if (map.containsKey(key)) {
                map = (Map<String, Object>) map.get(key);
            } else {
                return null;
            }
        }
        return map;
    }

    public static Map<String, Object> getJSONMap(Map<String, Object> json, Object... keys) {
        Map<String, Object> map = json;
        List<Object> list = null;
        for (Object key : keys) {
            if (map != null) {
                if (map.containsKey(key.toString())) {
                    Object value = map.get(key.toString());
                    if (value instanceof List) {
                        list = (List<Object>) value;
                        map = null;
                    } else {
                        map = (Map<String, Object>) value;
                    }
                } else {
                    return null;
                }
            } else {
                map = (Map<String, Object>) list.get((int) key);
                list = null;
            }
        }
        return map;
    }

    public static List<Object> getJSONList(Map<String, Object> json, String listKey, String... keys) {
        Map<String, Object> map = getJSONMap(json, keys);
        if (map != null && map.containsKey(listKey)) {
            return (List<Object>) map.get(listKey);
        }
        return null;
    }

    public static Object getJSONValue(Map<String, Object> json, String key) {
        if (json != null && json.containsKey(key)) {
            return json.get(key);
        }
        return null;
    }

    public static String getJSONValueString(Map<String, Object> json, String key) {
        Object value = getJSONValue(json, key);
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    public static boolean getJSONValueBoolean(Map<String, Object> json, String key) {
        Object value = getJSONValue(json, key);
        if (value != null) {
            return (boolean) value;
        }
        return false;
    }

    public static long getJSONValueLong(Map<String, Object> json, String key) {
        Object value = getJSONValue(json, key);
        if (value != null) {
            return ((Double) value).longValue();
        }
        return 0;
    }

    public static int getJSONValueInt(Map<String, Object> json, String key) {
        return (int) getJSONValueLong(json, key);
    }

    public static String getPageContent(String url, Map<String, String> header) throws IOException {
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        putRequestHeader(header);
        for (String key : header.keySet()) {
            connection.setRequestProperty(key, header.get(key));
        }
        connection.connect();
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                InputStream inputStream = connection.getInputStream();
                byte[] buff = new byte[8192];
                int len;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = inputStream.read(buff)) != -1) {
                    baos.write(buff, 0, len);
                }
                inputStream.close();
                String content = baos.toString(StandardCharsets.UTF_8.toString());
                baos.close();
                return content;
            }
        } catch (IOException exception) {
            throw new IOException("Error during http request ", exception);
        }
        return null;
    }

    public static String getPageContentWithJson(String url, String data, Map<String, String> header) throws IOException {
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        putRequestHeader(header);
        header.put("Content-Type", "application/json");
        header.put("Content-Length", String.valueOf(data.length()));
        for (String key : header.keySet()) {
            connection.setRequestProperty(key, header.get(key));
        }
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
        writer.write(data);
        writer.close();
        connection.connect();
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                InputStream inputStream = connection.getInputStream();
                byte[] buff = new byte[8192];
                int len;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = inputStream.read(buff)) != -1) {
                    baos.write(buff, 0, len);
                }
                inputStream.close();
                String content = baos.toString(StandardCharsets.UTF_8.toString());
                baos.close();
                return content;
            }
        } catch (IOException exception) {
            throw new IOException("Error during http request ", exception);
        }
        return null;
    }

    public static void sendHttpRequestWithJson(String url, String data, Map<String, String> header) throws IOException {
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        putRequestHeader(header);
        header.put("Content-Type", "application/json");
        header.put("Content-Length", String.valueOf(data.length()));
        for (String key : header.keySet()) {
            connection.setRequestProperty(key, header.get(key));
        }
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
        writer.write(data);
        writer.close();
        connection.connect();
        try {
            connection.getInputStream();
            connection.disconnect();
        } catch (IOException e) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String s;
            StringBuilder str = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                str.append(s);
            }
            connection.disconnect();
            throw new IOException(str.toString(), e);
        }
    }

    private static void putRequestHeader(Map<String, String> header) {
        header.put("Accept-Charset", "utf-8");
        header.put("User-Agent", YouTubeLiveChat.userAgent);
    }

    public static String generateClientMessageId() {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 26; i++) {
            sb.append(base.charAt(random.nextInt(base.length())));
        }

        return sb.toString();
    }

    public static JsonElement searchJsonElementByKey(String key, JsonElement jsonElement) {

        JsonElement value = null;

        // If input is an array, iterate through each element
        if (jsonElement.isJsonArray()) {
            for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
                value = searchJsonElementByKey(key, jsonElement1);
                if (value != null) {
                    return value;
                }
            }
        } else {
            // If input is object, iterate through the keys
            if (jsonElement.isJsonObject()) {
                Set<Map.Entry<String, JsonElement>> entrySet = jsonElement
                        .getAsJsonObject().entrySet();
                for (Map.Entry<String, JsonElement> entry : entrySet) {

                    // If key corresponds to the
                    String key1 = entry.getKey();
                    if (key1.equals(key)) {
                        value = entry.getValue();
                        return value;
                    }

                    // Use the entry as input, recursively
                    value = searchJsonElementByKey(key, entry.getValue());
                    if (value != null) {
                        return value;
                    }
                }
            }

            // If input is element, check whether it corresponds to the key
            else {
                if (jsonElement.toString().equals(key)) {
                    value = jsonElement;
                    return value;
                }
            }
        }
        return value;
    }
}
