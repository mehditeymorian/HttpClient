package http;

import java.util.List;


public class Request {
    public static final String LINE_SEPARATOR = "\r\n";
    private static final String HTTP_VERSION = "HTTP/1.0";

    private final Method method;
    private final String host;
    private final String path;
    private final List<String> headers;
    private final List<String> bodyParams;

    Request(Method method , String host , String path ,
            List<String> headers , List<String> bodyParams) {
        this.method = method;
        this.host = host;
        this.path = path;
        this.headers = headers;
        this.bodyParams = bodyParams;
    }

    /**
     * @return structure of Http message
     */
    String get() {
        String body = String.join(LINE_SEPARATOR , bodyParams);
        if (!body.isEmpty()) body += LINE_SEPARATOR;

        return getRequestLine() +
                String.join("" , headers) + // headers
                LINE_SEPARATOR +
                body;
    }

    private String getRequestLine() {
        return String.format("%s %s %s%s" , method.name() , path , HTTP_VERSION , LINE_SEPARATOR);
    }

    String getHost() {
        return host;
    }

    public Method method() {
        return method;
    }



}
