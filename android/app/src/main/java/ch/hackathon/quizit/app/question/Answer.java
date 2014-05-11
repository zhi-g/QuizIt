package ch.hackathon.quizit.app.question;

/**
 * Created by mathieu on 11/05/14.
 */
public class Answer {
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

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }
}
