package ch.hackathon.quizit.app.group;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import ch.hackathon.quizit.app.R;

/**
 * Created by mathieu on 10/05/14.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {
    List<String> mGroupsList;
    List<CustomListAdapterObserver> mObservers = new ArrayList<CustomListAdapterObserver>();
    private Context mContext;

    public interface CustomListAdapterObserver {
        public void update();
    }

    public CustomArrayAdapter(Context context, List<String> groups, CustomListAdapterObserver observer) {
        super(context, R.layout.custom_list_adapter_row_ayout, groups);
        mContext = context;
        mGroupsList = groups;
        mObservers.add(observer);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.custom_list_adapter_row_ayout, parent, false);

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

    public void joinGroup(View view) {

    }
}
