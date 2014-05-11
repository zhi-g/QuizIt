package ch.hackathon.quizit.app.question;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import ch.hackathon.quizit.app.R;
import ch.hackathon.quizit.app.utils.JSONBuilder;
import ch.hackathon.quizit.app.utils.JSONParser;


public class ShowQuestion extends ListActivity {
    public static String SERVER_URL = "";
   private TextView text;
    private LinearLayout tags;
    private List<Answer> answers;
    private Question question;
    private AnswersAdapter answersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_question);

        text = (TextView) findViewById(R.id.text_question);
        tags = (LinearLayout) findViewById(R.id.list_tags);
        question = getIntent().getParcelableExtra("question");
        text.setText(question.getQuestionText());
        text.setMovementMethod(new ScrollingMovementMethod());

        Button upvote = (Button) findViewById(R.id.button_upvote);
        Button downvote = (Button) findViewById(R.id.button_downvote);

        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question.upvote();
            }
        });
        downvote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                question.downvote();
            }
        });

        new GetAnswersTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void showAnswers(List<Answer> answer) {
        answersAdapter = new AnswersAdapter(this, answers);
        setListAdapter(answersAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_question, menu);
     return true;
    }

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
    private  class GetAnswersTask extends AsyncTask<Void, Void,  List<Answer>> {

        @Override
        protected List<Answer> doInBackground(Void ...unused){
            HttpClient httpClient = new DefaultHttpClient();
            ResponseHandler<String> handler = new BasicResponseHandler();
            HttpResponse httpResponse = null;
            String httpBody;
            List<Answer> answers = null;

            HttpPost httpRequest = new HttpPost(SERVER_URL);
            try {
                httpResponse = httpClient.execute(httpRequest);
                String requestBody = new JSONBuilder().putToken("token").putGroupID(question.getId()).build();
                httpRequest.setEntity(new StringEntity(requestBody));
                StatusLine statusLine = httpResponse.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    httpBody = handler.handleResponse(httpResponse);
                    answers= JSONParser.getAnswers(httpBody);
                }
            }catch (IOException e) {
                //TODO some error and staff
            }  catch(JSONException e) {

            }

            return answers;
        }


        @Override
        protected void onPostExecute( List<Answer> answers) {
            if(answers != null) {
                showAnswers(answers);
            }else {
                //TODO error
            }
        }
    }

}
