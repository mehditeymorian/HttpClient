package http.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpInputStream implements AutoCloseable{
    public static final String STATUS_LINE_REGEX = "^(HTTP/\\d\\.\\d) ([1-5]\\d\\d) ([a-zA-Z_ ]+)";
    public static final String HEADER_REGEX = "([\\w-]+): ([\\w, :*./]+);? ?(.*)";

    private static final String CONTENT_LENGTH_HEADER = "Content-Length";
    private static final byte CLRF_CODE = -1;

    private final InputStream stream;

    private boolean reachedBody = false;
    private final Map<String, String> headers;
    private String message;
    private int statusCode;
    private int contentLength;
    private byte[] body;


    public HttpInputStream(InputStream stream) {
        this.stream = stream;
        headers = new HashMap<>();

        try {
            readResponseFromStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void readResponseFromStream() throws Exception {
        readStatusLine();


        while (!reachedBody) readHeader(readLine());
        available();
        body = readBody();

        stream.close();
    }

    private void readStatusLine() {
        String input = readLine();
        Matcher matcher = Pattern.compile(STATUS_LINE_REGEX).matcher(input);
        if (matcher.find()) {
            // http version group 1
            statusCode = Integer.parseInt(matcher.group(2));
            message = matcher.group(3);
        } else {
            message = null;
            statusCode = -1;
        }
    }

    private void readHeader(String input) {
        Matcher matcher = Pattern.compile(HEADER_REGEX).matcher(input);
        if (!matcher.find()) return;
        String headerLabel = matcher.group(1);
        String headerValue = matcher.group(2);
        headers.put(headerLabel , headerValue);
        checkForContentLength(headerLabel , headerValue);
    }

    private void checkForContentLength(String label , String value) {
        if (!label.equals(CONTENT_LENGTH_HEADER)) return;

        contentLength = Integer.parseInt(value);
    }






    //region Getters
    public String message() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getBody() {
        return body;
    }

    public int getContentLength() {
        return contentLength;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
    //endregion

    //region Reading From Stream
    /**
     * @return read a line of stream if there is data otherwise return null.
     */
    public String readLine() {
        StringBuilder builder = new StringBuilder();
        try {
            while (available()) {
                byte b = (byte) stream.read();
                byte detectRCLF = detectCRLF(b);
                if (detectRCLF == CLRF_CODE) break;
                else builder.append((char) detectRCLF);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String val = builder.toString();
        if (val.isEmpty()) reachedBody = true;
        return val;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private byte[] readBody() {
        byte[] bytes = null;
        try {
            bytes = new byte[contentLength];
            stream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public byte detectCRLF(byte b) throws IOException {
        if (b != '\r') return b;
        available();
        byte read = (byte) stream.read();
        return read == '\n' ? CLRF_CODE : read;
    }

    @SuppressWarnings("BusyWait")
    private boolean available()  {
        int limit = reachedBody ? contentLength : 1;

        try {
            while (stream.available() < limit) Thread.sleep(1);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void close() throws Exception {
        stream.close();
    }
    //endregion

}
