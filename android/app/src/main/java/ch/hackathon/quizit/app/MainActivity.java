package ch.hackathon.quizit.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ch.hackathon.quizit.app.group.CreateGroupActivity;
import ch.hackathon.quizit.app.group.FetchGroupsAsyncTask;
import ch.hackathon.quizit.app.group.Group;
import ch.hackathon.quizit.app.group.JoinGroupActivity;
import ch.hackathon.quizit.app.question.QuestionsActivity;


public class MainActivity extends ActionBarActivity implements FetchGroupsAsyncTask.AsyncTaskListener {
    private static final String GROUP_NAME_EXTRA = "Group name";
    private List<Group> mGroupsList;
    private Button mGroupButton1;
    private Button mGroupButton2;
    private Button mGroupButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGroupsList = new ArrayList<Group>();

        mGroupButton1 = (Button) findViewById(R.id.most_recent_group_button_1);
        mGroupButton1.setText("Group1");
        mGroupButton2 = (Button) findViewById(R.id.most_recent_group_button_2);
        mGroupButton2.setText("Group2");
        mGroupButton3 = (Button) findViewById(R.id.most_recent_group_button_3);
        mGroupButton3.setText("Group3");

        new FetchGroupsAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void joinGroup(View view) {
        Intent newIntent = new Intent(this, JoinGroupActivity.class);
        startActivity(newIntent);
    }

    public void searchGroup(View view) {

    }

    public void createGroup(View view) {
        Intent newIntent = new Intent(this, CreateGroupActivity.class);
        startActivity(newIntent);
    }

    public void update(List<Group> newGroups) {
        if(newGroups != null && !newGroups.isEmpty()) {
            mGroupsList = newGroups;
            mGroupButton1.setText(mGroupsList.get(0).getName());
        }
    }

    public void mostRecentGroup1(View view) {
        String groupName = mGroupButton1.getText().toString();
        Intent newIntent = new Intent(this, QuestionsActivity.class);
        newIntent.putExtra(GROUP_NAME_EXTRA, groupName);
        startActivity(newIntent);
    }

    public void mostRecentGroup2(View view) {
        String groupName = mGroupButton2.getText().toString();
        Intent newIntent = new Intent(this, QuestionsActivity.class);
        newIntent.putExtra(GROUP_NAME_EXTRA, groupName);
        startActivity(newIntent);
    }

    public void mostRecentGroup3(View view) {
        String groupName = mGroupButton3.getText().toString();
        Intent newIntent = new Intent(this, QuestionsActivity.class);
        newIntent.putExtra(GROUP_NAME_EXTRA, groupName);
        startActivity(newIntent);
    }
}
