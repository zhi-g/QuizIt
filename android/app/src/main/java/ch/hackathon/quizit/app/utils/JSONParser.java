package ch.hackathon.quizit.app.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.hackathon.quizit.app.group.Group;
import ch.hackathon.quizit.app.question.Answer;
import ch.hackathon.quizit.app.question.Question;

/**
 * Created by mathieu on 11/05/14.
 */
public class JSONParser {
    private JSONParser() {}

    public static List<Group> getGroups(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("groups");

        List<Group> groupList = new ArrayList<Group>();
        for(int i = 0; i < jsonArray.length(); ++i) {
            JSONObject json = jsonArray.getJSONObject(i);
            long groupID = json.getLong("gid");
            String groupName = json.getString("name");
            groupList.add(new Group(groupID, groupName));
        }

        return groupList;
    }

    public static List<Question> getQuestions(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("questions");

        List<Question> questionsList = new ArrayList<Question>();
        for(int i = 0; i < jsonArray.length(); ++i) {
            JSONObject json =  jsonArray.getJSONObject(i);
            long questionID = json.getLong("qid");
            String questionText = json.getString("text");
            long owner = json.getLong("owner");
            int upVote = json.getInt("upvote");
            int downVote = json.getInt("downvote");
            int groupId = json.getInt("gid");
            JSONArray tags = json.getJSONArray("tags");

            Set<String> tagsList = new HashSet<String>();
            for(int j = 0; j < tags.length(); ++j) {
                JSONObject tag = tags.getJSONObject(i);
                tagsList.add(tag.getString("tag"));
            }

            questionsList.add(new Question(questionID, questionText, groupId, null, owner, upVote, downVote, tagsList));
        }

        return questionsList;
    }

    public static List<Answer> getAnswers(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("answers");


        List<Answer> answersList = new ArrayList<Answer>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject json = jsonArray.getJSONObject(i);
            long answerID = json.getLong("aid");
            String answer = json.getString("answer");
            int upVote = json.getInt("upvote");
            int downVote = json.getInt("downvote");
            long owner = json.getLong("owner");

            answersList.add(new Answer(answerID, answer, upVote, downVote, owner));
        }
        return answersList;
    }

    public static long getQuestionID(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        return json.getLong("qid");
    }

    public static long getAnswerID(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        return json.getLong("aid");
    }

    public static long getGroupID(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        return json.getLong("gid");
    }
}
