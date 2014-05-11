package ch.hackathon.quizit.app.question;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.hackathon.quizit.app.MainActivity;
import ch.hackathon.quizit.app.utils.JSONBuilder;
import ch.hackathon.quizit.app.utils.JSONParser;

/**
 * Created by mathieu on 11/05/14.
 */
public class CreateQuestionAsyncTask extends AsyncTask<Void, Void, Void> {
    private final static String REQUEST = "group";
    private Question resultingQuestion = null;
    private Context mContext;
    private Observer mObserver;
    private long mGroupID;
    private String mQuestion;
    private String mTags;

    public interface Observer {
        public void update();
    }

    public CreateQuestionAsyncTask(Context context, Observer observer, long groupID, String question, String tags) {
        mContext = context;
        mObserver = observer;
        mGroupID = groupID;
        mQuestion = question;
        mTags = tags;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        long questionID;
        try {
            HttpPost request = new HttpPost(MainActivity.SERVER_URL + REQUEST);
            JSONBuilder builder =  new JSONBuilder();
            request.addHeader("Content-type", "application/json");

            String[] tags = mTags.split("[^a-zA-z']");
            Set<String> tagSet = new HashSet<String>(Arrays.asList(tags));


            StringEntity entity = new StringEntity(builder.putToken("token").putGroupID(mGroupID).putQuestionText(mQuestion).putTagSet(tagSet).build());
            request.setEntity(entity);
            response = httpclient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
                String stringResponse = EntityUtils.toString(response.getEntity());
                questionID = JSONParser.getQuestionID(stringResponse);
                resultingQuestion = new Question(questionID, mQuestion, mGroupID, new ArrayList<Answer>(), -1, 0, 0, tagSet);
            }

            resultingQuestion = new Question(42, mQuestion, mGroupID, new ArrayList<Answer>(), -1, 0, 0, tagSet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Intent newIntent = new Intent(mContext, ShowQuestion.class);
        newIntent.putExtra("question", resultingQuestion);
        mContext.startActivity(newIntent);
    }
}