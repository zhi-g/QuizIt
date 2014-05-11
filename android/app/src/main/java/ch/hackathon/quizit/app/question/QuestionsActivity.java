package ch.hackathon.quizit.app.question;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


import  ch.hackathon.quizit.app.utils.*;
import ch.hackathon.quizit.app.R;


public class QuestionsActivity extends ListActivity {
    public static final String SERVER_URL = "http://128.179.161.172:9000/";

    private String groupName = "Unknown";
    private List<Question> questions;
    private long groupId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        groupName = getIntent().getStringExtra("Group name");
        ((TextView)findViewById(R.id.group_name)).setText(groupName);
        groupId = getIntent().getLongExtra("id", 0);

        new GetQuestionsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

      questions = new ArrayList<Question>();
        questions.add(new Question(845,"Couihgjbv",2, new ArrayList<Answer>(),765,0, 0,new TreeSet<String>()));
        questions.add(new Question(845,"Couihgjbv",2, new ArrayList<Answer>(),765,0, 0,new TreeSet<String>()));
        questions.add(new Question(845,"Couihgjbv",2, new ArrayList<Answer>(),765,0, 0,new TreeSet<String>()));
        questions.add(new Question(845,"Couihgjbv",2, new ArrayList<Answer>(),765,0, 0,new TreeSet<String>()));

      ArrayAdapter<String> mAdapter = new QuestionsAdapter(this, questions);
      setListAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.questions, menu);
        return true;
    }
  //  @Override
   /* public void onListItemClick(ListView list, View view, int position, long id) {
        //TODO open a question activity
    //    Intent myIntent = new Intent(this, ShowQuestion.class);
      //  myIntent.putExtra("question", questions[position]);
        //startActivity(myIntent);
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addButtonClicked(View view){
           Intent mIntent = new Intent(this, AddQuestionActivity.class);
           mIntent.putExtra("groupId", groupId);
           startActivity(mIntent);
     }

    private void showQuestions(List<Question> questions) {
        ArrayAdapter<String> mAdapter = new QuestionsAdapter(this, questions);
        setListAdapter(mAdapter);
    }


    private  class GetQuestionsTask extends AsyncTask<Void, Void,  List<Question>> {

        @Override
        protected List<Question> doInBackground(Void ...unused){
            HttpClient httpClient = new DefaultHttpClient();
            ResponseHandler<String> handler = new BasicResponseHandler();
            HttpResponse httpResponse = null;
            String httpBody;
            List<Question> questions = null;

            HttpPost httpRequest = new HttpPost(SERVER_URL);
            try {
                httpResponse = httpClient.execute(httpRequest);
                String requestBody = new JSONBuilder().putToken("token").putGroupID(groupId).build();
                httpRequest.setEntity(new StringEntity(requestBody));
                StatusLine statusLine = httpResponse.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    httpBody = handler.handleResponse(httpResponse);
                   questions = JSONParser.getQuestions(httpBody);
                }
            }catch (IOException e) {
                    //TODO some error and staff
            }  catch(JSONException e) {

            }

            return questions;
        }


        @Override
        protected void onPostExecute( List<Question> questions) {
            if(questions != null) {
                showQuestions(questions);
            }else {
                //TODO error
            }
        }
    }

}

