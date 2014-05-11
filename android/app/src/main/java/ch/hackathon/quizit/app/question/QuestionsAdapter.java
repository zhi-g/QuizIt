package ch.hackathon.quizit.app.question;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.hackathon.quizit.app.R;

/**
 * Created by studio on 10.05.2014.
 */
public class QuestionsAdapter extends ArrayAdapter<String> {
    private static final String TAG = QuestionsAdapter.class.getCanonicalName();
    List<Question> questions;
    Context  context;



    public QuestionsAdapter(Context context, List<Question> questions) {
        super(context, R.layout.row_layout_questions,extractQuestionText(questions));
        this.questions = questions;
        this.context = context;
    }

    /**
     * Extracts the questions' text to display
     * @param questions
     * @return
     */
    protected static List<String> extractQuestionText(List<Question> questions) {
        List<String> questionsText = new ArrayList<String>(questions.size());

        for(int i= 0; i<questions.size(); ++i) {
            questionsText.add(questions.get(i).getQuestionText());
        }
      return questionsText;
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
        TextView questionText = (TextView) rowView
                .findViewById(R.id.question_text);

        //TODO add upvote and downvote icons
        upvote.setText(questions.get(position).getUpvote() + "");
        upvote.setOnClickListener(new UpvoteButtonListener(position));
        downvote.setText(questions.get(position).getDownvote() + "");
        downvote.setOnClickListener(new DownvoteButtonListener(position));

        questionText.setText(questions.get(position).getQuestionText());
        questionText.setOnClickListener(new QuestionTextListener(position));

      return rowView;
    }


    private class UpvoteButtonListener implements  View.OnClickListener {
     private int position;

         public UpvoteButtonListener(int position) {
             this.position = position;
         }

         @Override
         public void onClick(View view) {
            questions.get(position).upvote();
             notifyDataSetChanged();
         }
      }
     private class DownvoteButtonListener implements  View.OnClickListener {
         private int position;

         public DownvoteButtonListener(int position) {
             this.position = position;
         }
         @Override
         public void onClick(View view) {
                questions.get(position).downvote();
                 notifyDataSetChanged();
         }
    }

    private class QuestionTextListener implements View.OnClickListener {
        private int position;

        public QuestionTextListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(context, ShowQuestion.class);
            myIntent.putExtra("question", questions.get(position));
            context.startActivity(myIntent);
        }
    }
 }

