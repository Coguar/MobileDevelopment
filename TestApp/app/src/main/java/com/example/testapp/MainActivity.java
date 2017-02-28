package com.example.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private TextView nameTxt;
    private boolean textState = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTxt = (TextView) findViewById(R.id.hello_txt);
        Button btn = (Button) findViewById(R.id.btn_change);

        btn.setOnClickListener(onClickListener);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(textState) {
                nameTxt.setText("PS-31");
                textState = false;
            }
            else {
                nameTxt.setText("Sorokin Stepan");
                textState = true;
            }
        }
    };
}
