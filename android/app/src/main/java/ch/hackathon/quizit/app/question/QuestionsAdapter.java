package ch.hackathon.quizit.app.question;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by studio on 10.05.2014.
 */
public class QuestionsAdapter extends ArrayAdapter<String> {
    public QuestionsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
}
