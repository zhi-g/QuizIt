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

import ch.hackathon.quizit.app.R;


public class QuestionsActivity extends ListActivity {
    private String groupName = "Unknown";
    private Question[] questions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        groupName = getIntent().getStringExtra("Group name");
        ((TextView)findViewById(R.id.group_name)).setText(groupName);
        showQuestions();

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
        Intent myIntent = new Intent(this, ShowQuestion.class);
        myIntent.putExtra("question", questions[position]);
        startActivity(myIntent);
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

    private void showQuestions() {

        ArrayAdapter<String> mAdapter = new QuestionsAdapter(this, new String[]{"bla", "plop", "Coucou"});
    }
    private class GetQuestionsTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void ...unused){
            return null;
        }
    }

}

