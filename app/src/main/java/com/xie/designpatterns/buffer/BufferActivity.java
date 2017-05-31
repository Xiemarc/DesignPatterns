package com.xie.designpatterns.buffer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/5/4.
 */

public class BufferActivity extends AppCompatActivity {
    EditText edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        edit = (EditText) findViewById(R.id.edit);
        edit.setCursorVisible(false);
        edit.setOnClickListener(click ->
                edit.setCursorVisible(true)
        );
    }
}
