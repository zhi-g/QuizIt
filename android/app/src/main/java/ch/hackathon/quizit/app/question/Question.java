package ch.hackathon.quizit.app.question;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.TreeSet;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by studio on 10.05.2014.
 */
public class Question implements Parcelable {
    private String questionText;
    private List<String> answers;
    private Set<String> tags;
    private int upvote;
    private int downvote;
    private String owner;
    private String groupId;

    public Question(String questionText, List<String> answers, Set<String> tags, int upvote, int downvote, String owner, String groupId){
        this.questionText = questionText;
        this.answers = new ArrayList<String>(answers);
        this.tags = new TreeSet<String>(tags);
        this.upvote = upvote;
        this.downvote = downvote;
        this.owner = owner;
        this.groupId = groupId;
    }

    public String getQuestionText(){
        return questionText;
    }
    public List<String> getAnswers(){
        return new ArrayList<String>(answers);
    }

    public Set getTags() {
        return new TreeSet<String>(tags);
    }

    public int getDownvote() {
        return downvote;
    }

    public int getUpvote() {
        return upvote;
    }

    public String getOwner() {
        return owner;
    }

    public String getGroupId() {
        return groupId;
    }

    public void upvote(){
        upvote--;
    }

    public void downvote(){
        downvote--;
    }


	/* Parcelable implementation */

    public Question(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionText);
        dest.writeStringList(answers);
        dest.writeStringList(new ArrayList<String>(tags));
        dest.writeInt(upvote);
        dest.writeInt(downvote);
        dest.writeString(owner);
        dest.writeString(groupId);
    }

    /**
     * Initializes the values of the members of the class
     * with the value written in the Parcel
     *
     * @param in the Parcel from which we are reading
     */
    private void readFromParcel(Parcel in) {
        questionText = in.readString();
        answers = new ArrayList<String>();
        in.readStringList(answers);
        ArrayList<String> tags_temp = new ArrayList<String>();
        in.readStringList(tags_temp);
        tags = new TreeSet<String>(tags_temp);
        in.readInt();
        upvote = in.readInt();
        downvote = in.readInt();
        owner = in.readString();
        groupId = in.readString();
    }


    /**
     * Static field used to regenerate the object, individually or as an array
     */
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
