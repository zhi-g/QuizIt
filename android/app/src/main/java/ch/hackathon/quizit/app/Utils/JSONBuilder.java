package ch.hackathon.quizit.app.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mathieu on 11/05/14.
 */
public class JSONBuilder {
    private JSONObject json;

    public JSONBuilder() {
        json = new JSONObject();
    }

    public JSONBuilder putToken(String token) throws JSONException {
        json.put("token", token);
        return this;
    }

    public JSONBuilder putQuestionID(long id) throws JSONException {
        json.put("qid", id);
        return this;
    }

    public JSONBuilder putGroupID(long id) throws JSONException {
        json.put("gid", id);
        return this;
    }

    public JSONBuilder putQuestionText(String text) throws JSONException {
        json.put("text", text);
        return this;
    }

    public JSONBuilder putQuestionAnswer(String text) throws JSONException {
        return putQuestionText(text);
    }

    public JSONBuilder putName(String name) throws JSONException {
        json.put("name", name);
        return this;
    }

    public String build() {
        return json.toString();
    }
}
