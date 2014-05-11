package ch.hackathon.quizit.app.question;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import ch.hackathon.quizit.app.R;

public class AddQuestionActivity extends ActionBarActivity implements CreateQuestionAsyncTask.Observer {
    private long mGroupID;
    private EditText mQuestionEditText;
    private EditText mTagsEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        mGroupID = getIntent().getLongExtra("groupID", -1);
        mQuestionEditText = (EditText) findViewById(R.id.edit_text_question);
        mTagsEditText = (EditText) findViewById(R.id.edit_text_tags);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_question, menu);
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

    public void update() {

    }

    public void submitButtonClicked(View view) {
        new CreateQuestionAsyncTask(this, this, mGroupID, mQuestionEditText.getText().toString(), mTagsEditText.getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
