package ch.hackathon.quizit.app.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

import ch.hackathon.quizit.app.R;
import ch.hackathon.quizit.app.Utils.JSONBuilder;
import ch.hackathon.quizit.app.Utils.JSONParser;
import ch.hackathon.quizit.app.question.QuestionsActivity;

public class CreateGroupActivity extends ActionBarActivity {
    private final static String REQUEST = "http://128.179.161.172:9000/group";
    private EditText mNewGroupTextView;
    private Group mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        mNewGroupTextView = (EditText) findViewById(R.id.group_name_edit_text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_group, menu);
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

    public void displayDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateGroupActivity.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setMessage(R.string.group_already_exists);
        alertDialogBuilder.setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent newIntent = new Intent(CreateGroupActivity.this, QuestionsActivity.class);
                startActivity(newIntent);
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    public void createGroup(View view) {
        new CreateGroupAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class CreateGroupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String groupName = mNewGroupTextView.getText().toString();
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                HttpPost request = new HttpPost(REQUEST);
                JSONBuilder builder =  new JSONBuilder();

                StringEntity entity = new StringEntity(builder.putToken("token").putName(groupName).build());
                request.setEntity(entity);
                response = httpclient.execute(request);
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    String stringResponse = EntityUtils.toString(response.getEntity());
                    mGroup = new Group(JSONParser.getGroupID(stringResponse), groupName);
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
            Intent newIntent = new Intent(CreateGroupActivity.this, QuestionsActivity.class);
            newIntent.putExtra("Group name", mGroup.getName());
            startActivity(newIntent);
            //displayDialog();
        }
    }
}
