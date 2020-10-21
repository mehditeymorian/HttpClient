package http;

import java.util.Arrays;

public enum Method {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE;


    public static String presentAll() {
        return Arrays.toString(values());
    }
}
