package http;


import java.net.Socket;
import java.util.Map;

public class Response implements AutoCloseable {
    private final Socket socket;
    Map<String, String> headers;
    String message;
    int statusCode;
    ResponseBody body;


    public Response(Socket socket) {
        this.socket = socket;
    }

    public ResponseBody body() {
        return body;
    }

    public boolean isSuccessful() {
        return getStatus() == 200;
    }

    public String message() {
        return message;
    }

    public int getStatus() {
        return statusCode;
    }

    public String getHeader(String headerLabel) {
        return headers.get(headerLabel);
    }


    @Override
    public void close() throws Exception {
        socket.close();
    }
}
