package app.http;

import org.json.simple.JSONObject;

/**
 * HTTP Response class, to help developers easily access response.
 */
public class HTTPResponse {
    private String responseHeader;
    private JSONObject responseBody;

    public HTTPResponse() {
        responseBody = null;
    }

    public String getReponseHeader() {
        return this.responseHeader;
    }

    public JSONObject getResponseBody() {
        return this.responseBody;
    }

    public void setResponseHeader(String header) {
        this.responseHeader = header;
    }

    public void setResponseBody(JSONObject body) {
        this.responseBody = body;
    }
}