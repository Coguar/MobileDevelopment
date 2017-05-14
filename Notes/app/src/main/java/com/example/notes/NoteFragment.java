package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Степан on 27.02.2017.
 */

public class NoteFragment extends ListFragment {

    DBHelper helper;
    ArrayList<ListElement> elements = new ArrayList<>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        helper = new DBHelper(this.getActivity());
        WriteDBInArr();
        ListAdapter adapter = new MyListAdapter(getActivity(),
                android.R.layout.simple_list_item_1, elements);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        WriteDBInArr();
        ListAdapter adapter = new MyListAdapter(getActivity(),
                android.R.layout.simple_list_item_1, elements);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.listfragment, null);
    }

    public void DoOnClick(View v, int position) {
        String status = " : " + getString(R.string.done);
        if (elements.get(position).isChecked()) {
            status = " : " + getString(R.string.not_done);
            elements.get(position).SetChecked(false);
        } else {
            elements.get(position).SetChecked(true);
        }
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        checkBox.setChecked(elements.get(position).isChecked());

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues value = new ContentValues();

        int done = 0;
        if (elements.get(position).isChecked()) {
            done = 1;
        }
        value.put("done", done);
        db.update("notes", value, "id = ?", new String[]{String.valueOf(elements.get(position).GetId())});

        Toast.makeText(getActivity(), elements.get(position).GetTitle() + status, Toast.LENGTH_SHORT).show();
    }

    private void WriteDBInArr() {
        elements.clear();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("notes", null, null, null, null, null, "done");
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String priority = cursor.getString(cursor.getColumnIndex("priority"));
                boolean isDone = cursor.getInt(cursor.getColumnIndex("done")) > 0;
                ListElement element = new ListElement(title, description, date, priority, isDone, cursor.getInt(cursor.getColumnIndex("id")));
                elements.add(element);
            } while (cursor.moveToNext());
        }

    }

    private void RemoveById(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("notes", "id = " + id, null);
        WriteDBInArr();
        ListAdapter adapter = new MyListAdapter(getActivity(),
                android.R.layout.simple_list_item_1, elements);
        setListAdapter(adapter);

    }

    public class MyListAdapter extends ArrayAdapter<ListElement> {

        private Context mContext;

        public MyListAdapter(Context context, int textViewResourceId, ArrayList<ListElement> elements) {
            super(context, textViewResourceId, elements);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.frame_row, parent,
                    false);

            TextView titleView = (TextView) row.findViewById(R.id.title);
            titleView.setText(elements.get(position).GetTitle());

            TextView descriptionView = (TextView) row.findViewById(R.id.description);
            descriptionView.setText(elements.get(position).GetDescription());

            TextView dateView = (TextView) row.findViewById(R.id.date);
            dateView.setText(elements.get(position).GetDate());

            TextView priorityView = (TextView) row.findViewById(R.id.priority);
            priorityView.setText(elements.get(position).GetPriority());

            CheckBox doneBox = (CheckBox) row.findViewById(R.id.checkBox);
            doneBox.setChecked(elements.get(position).isChecked());
            registerForContextMenu(row);

            row.setOnClickListener(new MyOnClickListener(row, position));

            MyLongClickListener listener = new MyLongClickListener(position);
            row.setOnLongClickListener(listener);
            return row;
        }

        public class MyOnClickListener implements View.OnClickListener {
            private int mPosition;
            private View mView;

            public MyOnClickListener(View view, int position) {
                mPosition = position;
                mView = view;
            }

            @Override
            public void onClick(View v) {
                DoOnClick(mView, mPosition);
            }
        }

        public class MyLongClickListener implements View.OnLongClickListener {

            private int mPosition;

            public MyLongClickListener(int position) {
                mPosition = position;
            }

            @Override
            public boolean onLongClick(View v) {
                ShowPopupMenu(v, elements.get(mPosition).GetId());
                return true;
            }
        }
    }

    private void ShowPopupMenu(View v, int recorfId){
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.inflate(R.menu.note_menu);

        popupMenu.setOnMenuItemClickListener(new MyPopupMenuListener(recorfId));
        popupMenu.show();
    }

    public class MyPopupMenuListener implements PopupMenu.OnMenuItemClickListener {

        private long mId;

        public MyPopupMenuListener(int recordPosition){mId = recordPosition;}

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // Toast.makeText(PopupMenuDemoActivity.this,
            // item.toString(), Toast.LENGTH_LONG).show();
            // return true;
            switch (item.getItemId()) {

                case R.id.Delete:
                    Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 400 milliseconds
                    vibrator.vibrate(400);
                    RemoveById(mId);
                    return true;
                case R.id.Update:
                    Intent intent = new Intent(getActivity(), NoteAddActivity.class);
                    intent.putExtra(getString(R.string.extra_record_id), mId);
                    intent.putExtra(getString(R.string.extra_is_update_flag), true);
                    startActivity(intent);
                    return true;
                default:
                    return false;
            }
        }
    }
}