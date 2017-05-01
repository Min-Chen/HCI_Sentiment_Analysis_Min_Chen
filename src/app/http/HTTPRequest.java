package app.http;

import org.json.simple.JSONObject;
import app.helper.ParseByRegex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * HTTP Request class, to help develpers easily access request content.
 */
public class HTTPRequest {
    private Socket socket;
    // requestHeader could be null
    private String requestHeader;
    private JSONObject requestBody;
    public static final String methodPattern = "[A-Z]+";
    public static final String handlerPattern = "/api/[a-zA-Z.]+";
    public static final String paramPattern = "?[A-Za-z0-9-]+";
    public static final String httpVersionPattern = " HTTP/[0-9.]+";

    public HTTPRequest(Socket socket) {
        this.socket = socket;
        setHeader(socket);
    }

    private void setHeader(Socket socket) {
        try {
            BufferedReader instream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.requestHeader = instream.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRequestHeader() {
        return this.requestHeader;
    }

    public String getMethod() {
        if (this.requestHeader != null) {
            return new ParseByRegex().parseByRegex(this.requestHeader, methodPattern, 0);
        }
        else return null;
    }

    public String getHandler() {
        if (this.requestHeader != null) {
            return new ParseByRegex().parseByRegex(this.requestHeader, handlerPattern, 0);
        }
        else return null;
    }

    private String getHTTPVersion() {
        if (this.requestHeader != null) {
            return new ParseByRegex().parseByRegex(this.requestHeader, httpVersionPattern, 0);
        }
        return null;
    }

    public HashMap<String, String> getParam() {
        if (this.requestHeader != null) {
            HashMap<String, String> params = new HashMap<>();
            String s = null;
            try {
                s = URLDecoder.decode(this.requestHeader, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            s = s.replace("GET ", "");
            s = s.replace(getHTTPVersion(), "");
            s = s.substring(s.indexOf("?") + 1);
            String[] keyAndValues = s.split("&");

            for (String keyAndValue : keyAndValues) {
                String key = keyAndValue.split("=")[0];
                String value = null;
                if (keyAndValue.split("=").length != 1) {
                    value = keyAndValue.split("=")[1];
                }
                params.put(key, value);
            }

            return params;
        }
        else return null;
    }
}