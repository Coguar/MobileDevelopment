package com.example.notes;

import android.support.annotation.NonNull;

/**
 * Created by Степан on 28.02.2017.
 */

public class ListElement {
    @NonNull
    private String mElementTitle = "";
    @NonNull
    private String mDescription = "";
    @NonNull
    private String mDate = "";
    @NonNull
    private String mPriority = "";

    private boolean mIsDone;
    private int mDbId;

    public ListElement(String title, String description, String date, String priority, boolean isDone, int id) {
        mElementTitle = title;
        mDescription = description;
        mDate = date;
        mIsDone = isDone;
        mPriority = priority;
        mDbId = id;
    }

    public int GetId() {
        return mDbId;
    }

    public void SetChecked(boolean isDone) {
        mIsDone = isDone;
    }

    public String GetTitle() {
        return mElementTitle;
    }

    public String GetDescription() {
        return mDescription;
    }

    public String GetDate() {
        return mDate;
    }

    public boolean isChecked() {
        return mIsDone;
    }

    public String GetPriority() {
        return mPriority;
    }
}
