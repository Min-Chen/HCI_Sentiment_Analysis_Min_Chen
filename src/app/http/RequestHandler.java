package app.http;

/**
 * Abstract class, to handle HTTP requests and return JSONObject response.
 */
public abstract class RequestHandler {
    /**
     * Return a app.http.HTTPResponse type of response
     *
     * @param request - An instance of HTTP request, with request hearder (with/without request body).
     * @return
     */
    public HTTPResponse handleRequest(HTTPRequest request) {
        return null;
    }

}