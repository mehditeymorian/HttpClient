package app;

import http.*;
import utils.DateUtils;

public class RequestTransmitter {
    private static final String studentIdHeaderLabel = "x-student-id";


    public static void send(String httpAddress , Method method , String studentIdHeaderValue) {
        HttpClient client = HttpClient.getInstance();
        RequestBuilder request = RequestBuilder.from(httpAddress)
                .method(method);
        // add student id extension header
        if (studentIdHeaderValue != null) request.addHeader(studentIdHeaderLabel , studentIdHeaderValue);

        Response response = client.send(request.build());
        if (response == null) return;
        if (response.isSuccessful()) onSuccess(response);
        else handleStatusCode(response);


    }

    private static void handleStatusCode(Response response) {
        // handle status code
        String statusMessage = StatusGuy.getStatusMessage(response.getStatus());
        System.out.printf("%d | %s\nReceived Message: %s\n" , response.getStatus() , statusMessage, response.message());
        if (response.body().hasValue()) {
            System.out.printf("Response Body:\n%s\n",response.body().getAsText());
        }
    }

    public static void onSuccess(Response response) {
        ResponseBody body = response.body();
        if (body.hasValue()) {
            switch (body.getContentType()) {
                case "text/plain":
                    System.out.println(body.getAsText());
                    break;

                case "text/html":
                    String htmlFileAddress = body.storeAsFile("./html-file.html");
                    System.out.printf("Html file stored in %s successfully\n",htmlFileAddress);
                    break;

                case "application/json":
                    String jsonFileAddress = body.storeAsFile("./json-file.json");
                    System.out.printf("Json file stored in %s successfully\n",jsonFileAddress);
                    break;
                default:
                    String extension = body.getContentType().split("/")[1];
                    String fileName = DateUtils.getNowDateTime() + "." + extension;
                    String fileAddress = body.storeAsFile(fileName);
                    System.out.printf("File stored in %s successfully\n",fileAddress);
            }
        }else System.out.println("Http Request Successfully Sent and Received. Status Code 200 OK");
    }



}
