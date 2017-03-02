package com.example.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<String> {

    final String[] catNames = new String[]{"Рыжик", "Барсик", "Мурзик",
            "Мурка", "Васька", "Томасина", "Кристина", "Пушок", "Дымка",
            "Кузя", "Китти", "Масяня", "Симба"};

    private Context mContext;

    public MyListAdapter(Context context, int textViewResourceId,
                         String[] objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.frame_row, parent,
                false);

        TextView catNameTextView = (TextView) row.findViewById(R.id.title);
        catNameTextView.setText(catNames[position]);
        CheckBox doneBox = (CheckBox) row.findViewById(R.id.checkBox);
        doneBox.setChecked(true);
        return row;
    }
}