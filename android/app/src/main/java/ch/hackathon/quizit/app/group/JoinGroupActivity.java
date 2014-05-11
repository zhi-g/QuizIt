package ch.hackathon.quizit.app.group;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.hackathon.quizit.app.R;
import ch.hackathon.quizit.app.group.CustomArrayAdapter;


public class JoinGroupActivity extends ListActivity implements CustomArrayAdapter.CustomListAdapterObserver {
    private static final String TAG = JoinGroupActivity.class.getCanonicalName();
    private CustomArrayAdapter mArrayAdapter;
    private List<String> mGroupsList;
    private final String URL = "http://128.179.161.172";
    private final int PORT = 9000;
    private final String REQUEST = "groups";

    public interface ListActivityObserver {
        public void update();
    }

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

    public void joinGroup(View view) {

    }

    public void update() {

    }

    private class FetchGroupsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                HttpGet request = new HttpGet(URL + ":" + PORT + "/" + REQUEST);
                response = httpclient.execute(request);
                StatusLine statusLine = response.getStatusLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            update();
        }
    }
}
