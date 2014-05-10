package ch.hackathon.quizit.app.group;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ch.hackathon.quizit.app.R;
import ch.hackathon.quizit.app.group.CustomArrayAdapter;


public class JoinGroupActivity extends ListActivity implements CustomArrayAdapter.CustomListAdapterObserver {
    private CustomArrayAdapter mArrayAdapter;
    private List<String> mGroupsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        mGroupsList = new ArrayList<String>();
        mGroupsList.add("Group1");
        mArrayAdapter = new CustomArrayAdapter(this, mGroupsList, this);

        setListAdapter(mArrayAdapter);
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

    public void update() {

    }
}
