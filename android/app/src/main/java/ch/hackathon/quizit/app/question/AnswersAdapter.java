package ch.hackathon.quizit.app.question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.hackathon.quizit.app.R;

/**
 * Created by studio on 11.05.2014.
 */
public class AnswersAdapter extends ArrayAdapter<Answer> {

    List<Answer> answers;
    Context context;



    public AnswersAdapter(Context context, List<Answer> answers) {
        super(context, R.layout.row_layout_questions, answers);
        this.answers = answers;
        this.context = context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(
                R.layout.row_layout_questions, parent, false);
        Button upvote = (Button) rowView.findViewById(R.id.button_upvote);
        Button downvote = (Button) rowView
                .findViewById(R.id.button_downvote);
        TextView answerText = (TextView) rowView
                .findViewById(R.id.text_question);

        //TODO add upvote and downvote icons

        upvote.setText(answers.get(position).getUpvote());
        upvote.setOnClickListener(new UpvoteButtonListener(position));
        downvote.setText(answers.get(position).getDownvote());
        downvote.setOnClickListener(new DownvoteButtonListener(position));

        answerText.setText(answers.get(position).getAnswer());

        return rowView;
    }
    private static List<String> extractAnswersText(List<Answer> answers) {
        List<String> answersText = new ArrayList<String>(answers.size());

        for(int i= 0; i<answers.size(); ++i) {
            answersText.add(answers.get(i).getAnswer());
        }
        return answersText;
    }
    private class UpvoteButtonListener implements  View.OnClickListener {
        private int position;

        public UpvoteButtonListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            answers.get(position).upvote();

        }
    }
    private class DownvoteButtonListener implements  View.OnClickListener {
        private int position;

        public DownvoteButtonListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            answers.get(position).downvote();
        }
    }
}
