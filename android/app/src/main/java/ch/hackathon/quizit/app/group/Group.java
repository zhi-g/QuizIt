package ch.hackathon.quizit.app.group;

/**
 * Created by studio on 10.05.2014.
 */
public class Group {
    private long id;
    private String mName;

    public Group(long id, String name) {
        this.id = id;
        mName = name;
    }

    public void setID(long id) {
        this.id = id;
    }

    public long getID() {
        return id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
