package com.example.notes;

import android.app.Fragment;
import android.app.ListActivity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {
    final String[] catNames = new String[]{"Рыжик", "Барсик", "Мурзик",
            "Мурка", "Васька", "Томасина", "Кристина", "Пушок", "Дымка",
            "Кузя", "Китти", "Масяня", "Симба"};
    private ListView lvSimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvSimple = (ListView) findViewById(R.id.list);
        MyListAdapter adapter = new MyListAdapter(this, R.layout.frame_row, catNames);
        lvSimple.setAdapter(adapter);
        registerForContextMenu(lvSimple);
        Button but = (Button) findViewById(R.id.addBtn);
        registerForContextMenu(but);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, 1, Menu.NONE, "Добавить");
        menu.add(Menu.NONE, 2, Menu.NONE, "Удалить");
    }



}
