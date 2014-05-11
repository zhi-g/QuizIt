package ch.hackathon.quizit.app.group;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.hackathon.quizit.app.R;
import ch.hackathon.quizit.app.utils.JSONBuilder;
import ch.hackathon.quizit.app.utils.JSONParser;
import ch.hackathon.quizit.app.question.Question;
import ch.hackathon.quizit.app.question.QuestionsActivity;
import ch.hackathon.quizit.app.question.ShowQuestion;

/**
 * Created by mathieu on 10/05/14.
 */
public class CustomArrayAdapter extends ArrayAdapter<Group> {
    private static final String TAG = CustomArrayAdapter.class.getCanonicalName();
    private List<Group> mGroupsList;
    private List<CustomListAdapterObserver> mObservers = new ArrayList<CustomListAdapterObserver>();
    private Context mContext;

    public interface CustomListAdapterObserver {
        public void update();
    }

    public CustomArrayAdapter(Context context, List<Group> groups, CustomListAdapterObserver observer) {
        super(context, R.layout.custom_list_adapter_row_layout, groups);
        mContext = context;
        mGroupsList = groups;
        mObservers.add(observer);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.custom_list_adapter_row_layout, parent, false);

        TextView groupName = (TextView) rowView.findViewById(R.id.button_join_group);
        groupName.setText(mGroupsList.get(position).getName());
        groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(mContext, QuestionsActivity.class);
                newIntent.putExtra("Group name", mGroupsList.get(position).getName());
                mContext.startActivity(newIntent);
            }
        });

        Button leaveGroup = (Button) rowView.findViewById(R.id.leave_group_button);
        leaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                Resources res = mContext.getResources();
                String text = String.format(res.getString(R.string.leave_group_dialog), mGroupsList.get(position).getName());
                builder.setMessage(text);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mGroupsList.remove(position);
                        notifyDataSetChanged();
                        notifyObserver();
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });

                builder.create().show();
            }
        });

        return rowView;
    }

    public void addObserver(CustomListAdapterObserver observer) {
        mObservers.add(observer);
    }

    public void removeObserver(CustomListAdapterObserver observer) {
        mObservers.remove(observer);
    }

    public void notifyObserver() {
        for(CustomListAdapterObserver observer: mObservers) {
            observer.update();
        }
    }
}
