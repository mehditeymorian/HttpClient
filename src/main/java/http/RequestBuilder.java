package http;

import java.util.ArrayList;
import java.util.List;

import static http.Request.LINE_SEPARATOR;

public class RequestBuilder {
    public static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/*,*/*;q=0.8";
    private Method method = Method.GET;
    private String host;
    private String path;
    private final List<String> headers;
    private final List<String> bodyParams;

    public static RequestBuilder from(String address) {
        return new RequestBuilder(address);
    }

    private RequestBuilder(String address) {
        headers = new ArrayList<>();
        bodyParams = new ArrayList<>();
        configureParams(address);
        addDefaultHeaders();
    }

    public Request build() {
        return new Request(method , host , path , headers , bodyParams);
    }

    private void configureParams(String address) {
        AddressEvaluator evaluator = new AddressEvaluator(address);
        host = evaluator.getHost();
        path = evaluator.getPath();
    }

    private void addDefaultHeaders() {
        addHeader("Host" , host);
        addHeader("Accept" , ACCEPT);
        addHeader("Accept-Encoding" , "gzip, deflate, br");
    }

    public RequestBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public RequestBuilder addHeader(String headerLabel , String headerValue) {
        headers.add(header(headerLabel , headerValue));
        return this;
    }

    public RequestBuilder addBodyParam(String key , String value) {
        bodyParams.add(String.format("%s=%s" , key , value));
        return this;
    }


    String header(String label , String value) {
        return String.format("%s: %s%s" , label , value , LINE_SEPARATOR);
    }


}
