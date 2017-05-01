package webservers;

import app.http.HTTPRequest;
import app.http.HTTPResponse;
import app.http.RequestHandler;
import app.http.ResponseHeaders;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

// https://www.youtube.com/watch?v=bOUqVC4XkOY&index=2&list=PL4071737C12790477
public class HeartbeatHandler extends RequestHandler {
    static float videoStartTime = -1;
    static Map<Float, Float> joyPeriod = new HashMap<>();
    static float lastJoyStartTime = -1;
    boolean isFrown = false;
    boolean isJoy = false;
    static boolean hasPrintedFrownMessage = false;
    static boolean hasPrintedSecondFrownMessage = false;

    public HTTPResponse handleRequest(HTTPRequest request) {
        HTTPResponse response = new HTTPResponse();
        JSONObject responseBody = new JSONObject();

        videoStartTime = System.currentTimeMillis();

        if (request.getParam().get("frown") == null) {
            response.setResponseHeader(ResponseHeaders.NOT_FOUND_HEADER);
//            System.out.println("not found");
            return response;
        }
        isFrown = Boolean.parseBoolean(request.getParam().get("frown"));

        if (request.getParam().get("joy") != null) {
            isJoy = Boolean.parseBoolean(request.getParam().get("joy"));
        }

        if (isFrown) {
            if (!hasPrintedFrownMessage) {
                hasPrintedFrownMessage = true;
                System.out.println("> You are frowning...\nSeems that you are confused.\nCheck http://www.japanese-online.com for more information.\n\n");
            } else {
                if (!hasPrintedSecondFrownMessage) {
                    hasPrintedSecondFrownMessage = true;
                    System.out.println("> You are frowning...\nWould you like to try\nhttp://www.fluentu.com/japanese/blog/easy-japanese-words-phrases/ \n for more guidance?");
                }
            }
        }
//        if (!isJoy) {
//            float currJoyTime = System.currentTimeMillis();
//            joyPeriod.put(lastJoyStartTime, currJoyTime);
//            lastJoyStartTime = currJoyTime;
//        } else {
//            if (videoStartTime == -1) {
//
//            }
//        }
        responseBody.put("success", new Boolean(true));
        response.setResponseHeader(ResponseHeaders.OK_HEADER);
        response.setResponseBody(responseBody);
        return response;
    }
}
