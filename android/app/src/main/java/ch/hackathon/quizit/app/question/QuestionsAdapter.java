package ch.hackathon.quizit.app.question;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import ch.hackathon.quizit.app.R;

/**
 * Created by studio on 10.05.2014.
 */
public class QuestionsAdapter extends ArrayAdapter<String> {
    Question[] questions;



    public QuestionsAdapter(Context context, Question[] questions) {
        super(context, R.layout.row_layout_questions, extractQuestionText(questions));
        this.questions = questions;

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
