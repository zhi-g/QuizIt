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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import ch.hackathon.quizit.app.R;


public class QuestionsActivity extends ListActivity {
    public static final String SERVER_URL = "http://128.179.161.172:9000/";

    private String groupName = "Unknown";
    private Question[] questions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        groupName = getIntent().getStringExtra("Group name");
        ((TextView)findViewById(R.id.group_name)).setText(groupName);

        new GetQuestionsTask().execute();

        //    Question[] questions = new Question[] {new Question("Couihgjbv", new ArrayList<String>(),new TreeSet<String>(),0,0,"",""),new Question("dfh", new ArrayList<String>(),new TreeSet<String>(),0,0,"",""),new Question("Couidjfhvrghgjbv", new ArrayList<String>(),new TreeSet<String>(),0,0,"","")};
      //  ArrayAdapter<String> mAdapter = new QuestionsAdapter(this, questions);
        //setListAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.questions, menu);
        return true;
    }
    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        //TODO open a question activity
    //    Intent myIntent = new Intent(this, ShowQuestion.class);
      //  myIntent.putExtra("question", questions[position]);
        //startActivity(myIntent);
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

    public void addButtonClicked(View view){

     }

    private void showQuestions(Question[] questions) {
        ArrayAdapter<String> mAdapter = new QuestionsAdapter(this, questions);
        setListAdapter(mAdapter);
    }


    private  class GetQuestionsTask extends AsyncTask<Void, Void, Question[]> {

        @Override
        protected Question[] doInBackground(Void ...unused){
            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpResponse response = httpClient.execute(new HttpGet(SERVER_URL));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    
                }
            }catch (IOException e) {

            }

            return null;
        }


        @Override
        protected void onPostExecute(Question[] questions) {

        }
    }

}

