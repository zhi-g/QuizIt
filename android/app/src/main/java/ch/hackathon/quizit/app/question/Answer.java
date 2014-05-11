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
}
