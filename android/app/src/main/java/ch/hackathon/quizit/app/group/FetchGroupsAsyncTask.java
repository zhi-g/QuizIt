package ch.hackathon.quizit.app.group;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.List;

import ch.hackathon.quizit.app.utils.JSONBuilder;
import ch.hackathon.quizit.app.utils.JSONParser;

/**
 * Created by mathieu on 11/05/14.
 */
public class FetchGroupsAsyncTask extends AsyncTask<Void, Void, Void> {
    private final static String TAG = FetchGroupsAsyncTask.class.getCanonicalName();
    private final static String URL = "http://128.179.161.172";
    private final static int PORT = 9000;
    private final static String REQUEST = "json";
    private AsyncTaskListener mListener;

    private List<Group> mGroupsList;

    public interface AsyncTaskListener {
        public void update(List<Group> newGroups);
    }

    public FetchGroupsAsyncTask(AsyncTaskListener listener) {
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            HttpPost request = new HttpPost(URL + ":" + PORT + "/" + REQUEST);
            JSONBuilder builder =  new JSONBuilder();
            request.addHeader("Content-type", "application/json");

            String json = builder.putToken("token").build();
            StringEntity entity = new StringEntity(json);
            request.setEntity(entity);
            response = httpclient.execute(request);
            Log.d(TAG, "Request: " + json);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
                String stringResponse = EntityUtils.toString(response.getEntity());
                Log.d(TAG, stringResponse);
                mGroupsList = JSONParser.getGroups(stringResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mListener.update(mGroupsList);
    }
}
