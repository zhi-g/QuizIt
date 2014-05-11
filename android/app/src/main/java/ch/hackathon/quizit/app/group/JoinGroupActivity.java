package ch.hackathon.quizit.app.group;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

import ch.hackathon.quizit.app.R;


public class JoinGroupActivity extends ListActivity implements CustomArrayAdapter.CustomListAdapterObserver, FetchGroupsAsyncTask.AsyncTaskListener {
    private static final String TAG = JoinGroupActivity.class.getCanonicalName();
    private CustomArrayAdapter mArrayAdapter;
    private List<Group> mGroupsList;

    public interface ListActivityObserver {
        public void update();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        mGroupsList = new ArrayList<Group>();
        mGroupsList.add(new Group(1, "Group1"));
        mGroupsList.add(new Group(2, "Group2"));

        mArrayAdapter = new CustomArrayAdapter(this, mGroupsList, this);

        setListAdapter(mArrayAdapter);
        new FetchGroupsAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.join_group, menu);
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

    }

    public void update() {
        mArrayAdapter.notifyDataSetChanged();
    }

    public void update(List<Group> newGroups) {
        mGroupsList = newGroups;
    }
}
