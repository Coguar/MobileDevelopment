package com.example.notes;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Степан on 23.03.2017.
 */

class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "noteDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table notes (" +
                "id integer primary key autoincrement, title text, description text, priority text, date text, done integer);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}