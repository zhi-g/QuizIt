package ch.hackathon.quizit.app.question;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.TreeSet;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by studio on 10.05.2014.
 */
public class Question implements Parcelable {
    private long id;
    private String questionText;
    private List<Answer> answers;
    private long owner;
    private int upvote;
    private int downvote;
    private Set<String> tags;
    private long groupId;

    /**
     *
     * @param id
     * @param questionText
     * @param groupId
     * @param answers
     * @param owner
     * @param upvote
     * @param downvote
     * @param tags
     */
    public Question(long id, String questionText, long groupId, List<Answer> answers, long owner, int upvote, int downvote, Set<String> tags){
        this.id = id;
        this.questionText = questionText;
        this.answers = answers;
        this.owner = owner;
        this.upvote = upvote;
        this.downvote = downvote;
        this.tags = new TreeSet<String>(tags);
        this.groupId = groupId;
    }

    public Question(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        new Question(jsonObject.getLong("qid"), jsonObject.getString("text"),jsonObject.getLong("gid"), null, jsonObject.getInt("owner"), jsonObject.getInt("upvote"), jsonObject.getInt("downvote"), null);
    }


    public String getQuestionText(){
        return questionText;
    }

    public List<Answer> getAnswers(){
        return new ArrayList<Answer>(answers);
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

    public long getOwner() {
        return owner;
    }

    public long getGroupId() {
        return groupId;
    }

    public long getId()  {return id;}

    public void upvote(){
        upvote++;
    }

    public void downvote(){
        downvote++;
    }


	/* Parcelable implementation */

    public Question(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionText);
        //dest.write TODO write answers to parcel
        dest.writeStringList(new ArrayList<String>(tags));
        dest.writeInt(upvote);
        dest.writeInt(downvote);
        dest.writeLong(owner);
        dest.writeLong(groupId);
    }

    /**
     * Initializes the values of the members of the class
     * with the value written in the Parcel
     *
     * @param in the Parcel from which we are reading
     */
    private void readFromParcel(Parcel in) {
        questionText = in.readString();
       // answers = new ArrayList<String>();
       // in.readStringList(answers); TODO read answers
        ArrayList<String> tags_temp = new ArrayList<String>();
        in.readStringList(tags_temp);
        tags = new TreeSet<String>(tags_temp);
        in.readInt();
        upvote = in.readInt();
        downvote = in.readInt();
        owner = in.readLong();
        groupId = in.readLong();
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
