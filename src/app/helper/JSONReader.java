package app.helper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader {
    public static JSONObject getJsonObject(String path) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            Object obj = parser.parse(new FileReader(path));
            jsonObject =  (JSONObject) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getJSONObjectFromStr(String str) {
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) new JSONParser().parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
