package com.github.kusaanko.youtubelivechat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("unchecked")
public class Util {

    public static Map<String, Object> toJSON(String json) {
        LinkedHashMap<String, Object> js = new LinkedHashMap<>();
        toJSON(json, js, 0);
        return js;
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

    public static int toJSON(String json, Map<String, Object> result, int pos) {
        if (!json.substring(pos).startsWith("{")) {
            throw new IllegalArgumentException("This is not json(map)!");
        }
        StringBuilder key = new StringBuilder();
        StringBuilder str = new StringBuilder();
        StringBuilder unicode = new StringBuilder();
        boolean keyStart = false;
        boolean valueStart = false;
        boolean strStart = false;
        boolean numStart = false;
        boolean strEscape = false;
        char[] jsonArray = json.toCharArray();
        int unicodePos = 0;
        pos++;
        for (; pos < json.length(); pos++) {
            char c = jsonArray[pos];
            if (unicodePos > 0) {
                if (unicodePos > 2) {
                    unicode.append(c);
                }
                unicodePos++;
                if (unicodePos == 5) {
                    str.append(new String(new BigInteger(unicode.toString(), 16).toByteArray()));
                    unicodePos = 0;
                }
            } else if (keyStart) {
                if (c == '\\') {
                    if (strEscape) {
                        key.append(c);
                        strEscape = false;
                    } else {
                        strEscape = true;
                    }
                } else if (c == '"') {
                    if (strEscape) {
                        key.append(c);
                        strEscape = false;
                    } else {
                        keyStart = false;
                        valueStart = true;
                    }
                } else if (strEscape) {
                    if (c == 'u') {
                        unicodePos = 1;
                    } else if (c == 'n') {
                        str.append("\n");
                    }
                    strEscape = false;
                } else {
                    key.append(c);
                }
                continue;
            } else if (valueStart) {
                if (strStart) {
                    if (c == '\\') {
                        if (strEscape) {
                            str.append(c);
                            strEscape = false;
                        } else {
                            strEscape = true;
                        }
                    } else if (c == '"') {
                        if (strEscape) {
                            str.append(c);
                            strEscape = false;
                        } else {
                            result.put(key.toString(), str.toString());
                            str = new StringBuilder();
                            key = new StringBuilder();
                            strStart = false;
                            valueStart = false;
                        }
                    } else if (strEscape) {
                        if (c == 'u') {
                            unicodePos = 1;
                        } else if (c == 'n') {
                            str.append("\n");
                        }
                        strEscape = false;
                    } else {
                        str.append(c);
                    }
                    continue;
                } else if (c == '"') {
                    strStart = true;
                } else if (c >= '0' && c <= '9' || c == '-') {
                    numStart = true;
                } else if (c == 't') {
                    result.put(key.toString(), true);
                    key = new StringBuilder();
                    valueStart = false;
                    pos += 3;
                } else if (c == 'f') {
                    result.put(key.toString(), false);
                    key = new StringBuilder();
                    valueStart = false;
                    pos += 4;
                } else if (c == '[') {
                    ArrayList<Object> list = new ArrayList<>();
                    pos = toJSON(json, list, pos);
                    result.put(key.toString(), list);
                    key = new StringBuilder();
                    valueStart = false;
                } else if (c == '{') {
                    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                    pos = toJSON(json, map, pos);
                    result.put(key.toString(), map);
                    key = new StringBuilder();
                    valueStart = false;
                    continue;
                }
                if (numStart) {
                    if ((c >= '0' && c <= '9' || c == '-') || c == '.') {
                        str.append(c);
                    } else {
                        numStart = false;
                        String n = str.toString();
                        if (n.contains(".")) {
                            result.put(key.toString(), Double.parseDouble(n));
                        } else {
                            result.put(key.toString(), Long.parseLong(n));
                        }
                        key = new StringBuilder();
                        str = new StringBuilder();
                        valueStart = false;
                    }
                }
            } else if (c == '"') {
                keyStart = true;
            }
            if (c == '}') {
                break;
            }
        }
        return pos;
    }

    public static int toJSON(String json, List<Object> result, int pos) {
        if (!json.substring(pos).startsWith("[")) {
            throw new IllegalArgumentException("This is not json(list)!");
        }
        StringBuilder str = new StringBuilder();
        StringBuilder unicode = new StringBuilder();
        boolean strStart = false;
        boolean strEscape = false;
        boolean numStart = false;
        int unicodePos = 0;
        char[] jsonArray = json.toCharArray();
        pos++;
        for (; pos < json.length(); pos++) {
            char c = jsonArray[pos];
            if (unicodePos > 0) {
                if (unicodePos > 2) {
                    unicode.append(c);
                }
                unicodePos++;
                if (unicodePos == 5) {
                    str.append(new String(new BigInteger(unicode.toString(), 16).toByteArray()));
                    unicodePos = 0;
                }
            } else if (strStart) {
                if (c == '\\') {
                    if (strEscape) {
                        str.append(c);
                        strEscape = false;
                    } else {
                        strEscape = true;
                    }
                } else if (c == '\"') {
                    if (!strEscape) {
                        strStart = false;
                        result.add(str.toString());
                        str = new StringBuilder();
                    } else {
                        str.append(c);
                    }
                    strEscape = false;
                } else if (strEscape) {
                    if (c == 'u') {
                        unicodePos = 1;
                    } else if (c == 'n') {
                        str.append("\n");
                    }
                    strEscape = false;
                } else {
                    str.append(c);
                }
                continue;
            } else if (c == '\"') {
                strStart = true;
            } else if (c >= '0' && c <= '9' || c == '-') {
                numStart = true;
            } else if (c == 't') {
                result.add(true);
                pos += 3;
            } else if (c == 'f') {
                result.add(false);
                pos += 4;
            } else if (c == '[') {
                ArrayList<Object> list = new ArrayList<>();
                pos = toJSON(json, list, pos);
                result.add(list);
            } else if (c == '{') {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                pos = toJSON(json, map, pos);
                result.add(map);
            }
            if (numStart) {
                if ((c >= '0' && c <= '9' || c == '-') || c == '.') {
                    str.append(c);
                } else {
                    numStart = false;
                    String n = str.toString();
                    if (n.contains(".")) {
                        result.add(Double.parseDouble(n));
                    } else {
                        result.add(Long.parseLong(n));
                    }
                    str = new StringBuilder();
                }
            }
            if (c == ']') {
                break;
            }
        }
        return pos;
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
            return (long) value;
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
        connection.getInputStream();
        connection.disconnect();
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
}
