package com.example.mynotebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity666";
    private static final String TAG1 = "MainActivity66666";
    private FloatingActionButton mFloatingActionBtn;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private TextView tv_main;
    private ListView lv;
    private NoteAdapter adapter;
    private List noteList = new ArrayList();
    private Achievement achievement;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        tv_main = findViewById(R.id.tv_main);
        mFloatingActionBtn = findViewById(R.id.floatingActionBtn);
        drawerLayout = findViewById(R.id.drawer_main);
        lv = findViewById(R.id.lv);
        adapter = new NoteAdapter(getApplicationContext(), noteList);

        refreshListView();
        lv.setAdapter(adapter);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("我的笔记本");

//        设置导航图标要在setSupportActionBar方法之后
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        Toast.makeText(MainActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_refresh:
                        Toast.makeText(MainActivity.this, "刷新", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_delete:
                        Toast.makeText(MainActivity.this, "删除", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });


    }

    private void refreshListView() {
//        初始化CRUD
        Log.d(TAG1, "refreshLV 4");
        CRUD crud = new CRUD(context);
        crud.open();

//        设置 Adapter 适配器
//        if (noteList.size() > 0) {
//            Log.d(TAG1, "noteList clear");
//            noteList.clear();
//            noteList.addAll(crud.getAllNotes());
//        }
        if (true) {
            Log.d(TAG1, "noteList clear");
            noteList.clear();
            Log.d(TAG1, "noteList clear2");
            noteList.addAll(crud.getAllNotes());
            Log.d(TAG1, noteList.size()+"");
        }
        crud.close();
        adapter.notifyDataSetChanged();
    }


    private void initEvent() {
        mFloatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "floatingactionbutton onClick: ");
                startActivityForResult(new Intent(MainActivity.this, EditActivity.class), 1);
            }
        });

//        点击打开和关闭侧边栏
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "点击了侧边菜单按钮", Toast.LENGTH_SHORT).show();
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    Log.d(TAG, "no");
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    Log.d(TAG, "yes");
                    drawerLayout.openDrawer(GravityCompat.START);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int returnMode;
        returnMode = data.getExtras().getInt("mode");
        Log.d(TAG1, returnMode+"");
//        新建笔记
        if (returnMode == 0) {
            String content = data.getExtras().getString("content");
            long testid = data.getExtras().getLong("testid");
            String time = data.getExtras().getString("time");

            Log.d(TAG1, "2");
            Note note = new Note(content, time);
            CRUD crud = new CRUD(context);
            crud.open();
            crud.addNote(note);
            crud.close();
        }

        Log.d(TAG1, "start refreshLV");
        refreshListView();

    }

    //    加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //    功能实现系统
    public class Achievement {
        private SharedPreferences sharedPreferences;
        int noteNumber;
        int wordNumber;

        public Achievement(Context context) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        //        添加笔记
        public void addNote(String content) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            noteNumber++;

            edit.putInt("noteNumber", noteNumber);
            wordNumber += content.length();
            edit.putInt("wordNumber", wordNumber);
            edit.commit();
        }


    }

}
