package ch.hackathon.quizit.app.question;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
        ImageButton upvote = (ImageButton) rowView.findViewById(R.id.button_upvote);
        ImageButton downvote = (ImageButton) rowView
                .findViewById(R.id.button_downvote);
        TextView questionText = (TextView) rowView
                .findViewById(R.id.question_text);

        //TODO add upvote and downvote icons

        upvote.setOnClickListener(new UpvoteButtonListener(position));
        downvote.setOnClickListener(new DownvoteButtonListener(position));

       questionText.setText(questions[position].getQuestionText());

      return rowView;
    }


    private class UpvoteButtonListener implements  View.OnClickListener {
     private int position;

         public UpvoteButtonListener(int position) {
             this.position = position;
         }

         @Override
         public void onClick(View view) {
            questions[position].upvote();

         }
      }
     private class DownvoteButtonListener implements  View.OnClickListener {
         private int position;

         public DownvoteButtonListener(int position) {
             this.position = position;
         }
         @Override
         public void onClick(View view) {
                questions[position].downvote();
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
             myIntent.putExtra("question", questions[position]);
            context.startActivity(myIntent);
        }
    }
 }

