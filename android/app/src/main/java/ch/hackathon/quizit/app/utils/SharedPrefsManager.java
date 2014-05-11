package ch.hackathon.quizit.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mathieu on 11/05/14.
 */
public class SharedPrefsManager {
    private static final String APP_SHARED_PREFS = "APP_SHARED_PREFS";
    private static final String GROUP_ACCESS = "GROUP_ACCESS";
    private final Context mContext;

    public SharedPrefsManager(Context context) {
        mContext = context;
    }

    public void setNewFirst(String name) {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(APP_SHARED_PREFS, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        String oldFirst = sharedPrefs.getString(GROUP_ACCESS + 0, "Group" + 0);
        String oldSecond = sharedPrefs.getString(GROUP_ACCESS + 1, "Group" + 1);

        editor.putString(GROUP_ACCESS + 0, name);

        if(oldSecond.equals(name)) {
            editor.putString(GROUP_ACCESS + 1, oldFirst);
        } else {
            editor.putString(GROUP_ACCESS + 1, oldFirst);
            editor.putString(GROUP_ACCESS + 2, oldSecond);
        }
        editor.commit();
    }
}
