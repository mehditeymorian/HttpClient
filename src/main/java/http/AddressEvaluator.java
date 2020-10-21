package http;

import http.exception.InvalidAddressException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressEvaluator {
    private static final String HTTP_VALIDATION_REGEX = "(?i)(http://)?([-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b)([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";


    private String protocol = "http";
    private final String host;
    private String path;

    public AddressEvaluator(String address) throws InvalidAddressException {
        Matcher matcher = matcherAgainst(address);
        boolean valid = matcher.find();

        if (!valid)
            throw new InvalidAddressException();

        // TODO: 10/16/2020 read http too
        host = matcher.group(2);
        path = matcher.group(3);
        if (path.isEmpty()) path = "/";
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public static boolean evaluate(String address) {
        return matcherAgainst(address).find();
    }

    private static Matcher matcherAgainst(String address) {
        Pattern pattern = Pattern.compile(HTTP_VALIDATION_REGEX, Pattern.MULTILINE);
        return pattern.matcher(address);
    }
}
