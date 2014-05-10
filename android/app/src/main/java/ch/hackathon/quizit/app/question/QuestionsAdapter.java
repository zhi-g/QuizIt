package ch.hackathon.quizit.app.question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import ch.hackathon.quizit.app.R;

/**
 * Created by studio on 10.05.2014.
 */
public class QuestionsAdapter extends ArrayAdapter<String> {
    Question[] questions;
    Context  context;



    public QuestionsAdapter(Context context, Question[] questions) {
        super(context, R.layout.row_layout_questions,extractQuestionText(questions));
        this.questions = questions;
        this.context = context;

    }

    /**
     * Extracts the questions' text to display
     * @param questions
     * @return
     */
    protected static String[] extractQuestionText(Question[] questions) {
        String[] questionsText = new String [questions.length];

        for(int i= 0; i<questions.length; ++i) {
            questionsText[i] = questions[i].getQuestionText();
        }
      return questionsText;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(
                R.layout.row_layout_questions, parent, false);
        Button upvote = (Button) rowView
                .findViewById(R.id.button_upvote);
        Button downvote = (Button) rowView
                .findViewById(R.id.button_downvote);
        TextView questionText = (TextView) rowView
                .findViewById(R.id.question_text);

        upvote.setText("A");

       questionText.setText(questions[position].getQuestionText());

      return rowView;
    }


 private class UpvoteButtonListener implements  View.OnClickListener {
     private int position;
     private QuestionsAdapter adapter;

     public UpvoteButtonListener(int position,QuestionsAdapter adapter) {
         this.position = position;
         this.adapter = adapter;
     }
     @Override
     public void onClick(View view) {

     }

     private class DownvoteButtonListener implements  View.OnClickListener {
         private int position;
         private QuestionsAdapter adapter;

         public DownvoteButtonListener(int position,QuestionsAdapter adapter) {
             this.position = position;
             this.adapter = adapter;
         }
         @Override
         public void onClick(View view) {

         }
     }
 }
}
