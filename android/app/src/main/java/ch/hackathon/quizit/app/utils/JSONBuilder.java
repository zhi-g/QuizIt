package ch.hackathon.quizit.app.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    public JSONBuilder putTagSet(Set<String> tagSet) throws JSONException {
        JSONArray jsonArray = new JSONArray(tagSet);
        json.put("tags", jsonArray);
        Log.d(JSONBuilder.class.getCanonicalName(), "JSON: " + this.build().toString());
        return this;
    }

    public String build() {
        return json.toString();

    }
}