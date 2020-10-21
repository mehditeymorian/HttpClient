package http;

import java.io.*;

public class ResponseBody {
    String contentType;
    int contentLength;
    byte[] data;


    ResponseBody() {
    }

    public String getAsText() {
        return hasValue() ? new String(data) : null;
    }

    public byte[] getBytes() {
        return hasValue() ? data : null;
    }

    public InputStream getInputStream() {
        return hasValue() ? new ByteArrayInputStream(getBytes()) : null;
    }

    public boolean hasValue() {
        return contentLength != 0;
    }

    public String getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String storeAsFile(String fileAddress) {
        File file = new File(fileAddress);

        try {
            OutputStream os = new FileOutputStream(file);
            os.write(getBytes());
            os.close();
        } catch (IOException e) {
            System.out.println("An Error Related to OutputStream Happened on Saving Response Body as File!");
        }
        return file.getAbsolutePath();
    }
}
