package http;

import http.io.HttpInputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HttpClient {
    public static final int HTTP_PORT = 80;
    private static HttpClient INSTANCE;


    public static HttpClient getInstance() {
        if (INSTANCE == null) INSTANCE = new HttpClient();
        return INSTANCE;
    }


    private HttpClient() {

    }

    public Response send(Request request) {
        Socket socket;
        Response response;
        try {
            socket = new Socket(request.getHost() , HTTP_PORT);
            OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
            output.write(request.get());
            output.flush();

            ResponseBody body = new ResponseBody();
            response = new Response(socket);

            HttpInputStream stream = new HttpInputStream(socket.getInputStream());
            response.headers = stream.getHeaders();
            response.message = stream.message();
            response.statusCode = stream.getStatusCode();

            body.data = stream.getBody();
            body.contentLength = stream.getContentLength();
            body.contentType = response.getHeader("Content-Type");
            response.body = body;

        } catch (IOException e) {
            System.out.println("Connection Refused!");
            System.out.println(e.getMessage());
            return null;
        }
        return response;
    }

}
