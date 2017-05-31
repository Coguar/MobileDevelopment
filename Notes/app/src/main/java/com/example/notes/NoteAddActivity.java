package com.example.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by Степан on 22.03.2017.
 */

public class NoteAddActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    public String mPriority = "Low";
    public String mDate;
    private Button mAddBtn;
    private RadioGroup mGroup;
    private DBHelper helper;
    private CalendarView mCalendarView;

    private boolean mIsUpdate = false;
    private long mRecordId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecordId = getIntent().getLongExtra(getString(R.string.extra_record_id), 0);
        mIsUpdate = getIntent().getBooleanExtra(getString(R.string.extra_is_update_flag), false);

        setContentView(R.layout.note_add_activity);
        mAddBtn = (Button) findViewById(R.id.acceptBtn);
        mAddBtn.setOnClickListener(onClickListener);
        mGroup = (RadioGroup) findViewById(R.id.priority_group);
        mGroup.setOnCheckedChangeListener(this);
        helper = new DBHelper(this);

        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                mDate = new StringBuilder().append(mMonth + 1)
                        .append("/").append(mDay).append("/").append(mYear)
                        .append(" ").toString();
            }
        });

        if(mIsUpdate)
        {
            onUpdateActionInit();
        }

    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SQLiteDatabase db = helper.getWritableDatabase();

            EditText titleEditText = (EditText) findViewById(R.id.editTitle);
            EditText descrEditText = (EditText) findViewById(R.id.editDescr);

            ContentValues value = new ContentValues();
            value.put("title", titleEditText.getText().toString());
            value.put("description", descrEditText.getText().toString());

            value.put("priority", mPriority);
            value.put("done", 0);
            value.put("date", mDate);

            if (!titleEditText.getText().toString().isEmpty()) {
                if(mIsUpdate)
                {
                    db.update("notes", value, "id = ?", new String[]{String.valueOf(mRecordId)});
                }
                else {
                    db.insert("notes", null, value);
                }
            }
            helper.close();
            finish();
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.lowPriority:
                mPriority = getString(R.string.low);
                break;
            case R.id.middlePriority:
                mPriority = getString(R.string.middle);
                break;
            case R.id.highPriority:
                mPriority = getString(R.string.high);
                break;
        }
    }

    private void onUpdateActionInit(){
        SQLiteDatabase db = helper.getWritableDatabase();

        EditText titleEditText = (EditText) findViewById(R.id.editTitle);
        EditText descrEditText = (EditText) findViewById(R.id.editDescr);

        Cursor cursor = db.query("notes", null, "id = ?", new String[]{String.valueOf(mRecordId)}, null, null, null);

        if(cursor.moveToFirst())
        {
            titleEditText.setText(cursor.getString(cursor.getColumnIndex("title")));
            descrEditText.setText(cursor.getString(cursor.getColumnIndex("description")));
        }
        mAddBtn.setText("Update");
    }
}
