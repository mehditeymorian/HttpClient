package app;

public class StatusGuy {

    public static String getStatusMessage(int statusCode) {
        switch (statusCode) {
            case 201:
                return "Created";
            case 204:
                return "No Content";
            case 400:
                return "Bad Request";
            case 401:
                return "Unauthorized";
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            case 405:
                return "Method Not Allowed";
            case 500:
                return "Internal Server Error";
            case 501:
                return "Not Implemented";
            case 503:
                return "Service Unavailable";
            case 301:
                return "Moved Permanently";
            case 307:
                return "Moved Temporarily";
            case 304:
                return "Not Modified";
            default:
                return "Undefined Code To Client";
        }
    }
}
