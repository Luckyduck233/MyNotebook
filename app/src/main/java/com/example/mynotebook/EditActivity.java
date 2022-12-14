package com.example.mynotebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";
    private EditText mEditTxt;
    private Toolbar toolbar;
    private int tag = 1;
    private Spinner mSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
        initListener();
    }

    private void initView() {
        mEditTxt = findViewById(R.id.et_edit);
        toolbar = findViewById(R.id.toolbar_edit);

        toolbar.setNavigationIcon(R.drawable.ic_prev_24);

        mSpinner = findViewById(R.id.spinner);

        List<String> tagList = new ArrayList<String>();
        tagList.add("无标签");
        tagList.add("生活");
        tagList.add("学习");
        tagList.add("工作");
        tagList.add("娱乐");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,tagList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (mEditTxt.getText().toString().length() == 0) {
//                    没有内容
                    intent.putExtra("mode", -1);
                    Log.d(TAG, "Edit没有内容");
                } else {
                    intent.putExtra("mode",Constants.Content_NEW );
                    intent.putExtra("content", mEditTxt.getText().toString());
                    intent.putExtra("testid", Constants.TEST_ID);
                    intent.putExtra("tag", 1);
                    intent.putExtra("time", "2022-11-02");
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            if (mEditTxt.getText().toString().length() == 0) {
//                    没有内容
                intent.putExtra("mode", -1);
                Log.d(TAG, "Edit没有内容");
            } else {
                intent.putExtra("mode",Constants.Content_NEW );
                intent.putExtra("content", mEditTxt.getText().toString());
                intent.putExtra("testid", Constants.TEST_ID);
                intent.putExtra("tag", 1);
                intent.putExtra("time", "2022-11-02");
            }
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
//        if (keyCode == keyEvent.KEYCODE_HOME) {
//            return true;
//        } else if (keyCode == keyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent();
//            intent.putExtra("input", mEditTxt.getText().toString());
//            setResult(RESULT_OK,intent);
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, keyEvent);
//    }
}
