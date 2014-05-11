package ch.hackathon.quizit.app.question;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mathieu on 11/05/14.
 */
public class Answer implements Parcelable {
    private long id;

    private String answer;
    private int upvote;
    private int downvote;
    private long owner;

    public Answer(long id, String answer, int upvote, int downvote, long owner) {
        this.id = id;
        this.answer = answer;
        this.upvote = upvote;
        this.downvote = downvote;
        this.owner = owner;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }


    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getUpvote() {
        return upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public long getOwner() {
        return owner;
    }

    public void upvote() {
        upvote++;
    }
    public void downvote () {
        downvote++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {}

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }



    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }
}
