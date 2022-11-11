package com.example.mynotebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private List<Note> noteList = new ArrayList();
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
            Log.d(TAG1, noteList.size() + "");
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


        //lv长按点击事件
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Note deletedNote = noteList.get(position);
                new AlertDialog.Builder(context).setMessage("确认删除该笔记吗?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CRUD crud = new CRUD(context);
                        crud.open();
                        crud.removeNote(deletedNote);
                        crud.close();
                        refreshListView();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return true;
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

//        toolbar右侧按钮点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        Toast.makeText(MainActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                        String content = noteList.get(0).getContent();
                        Log.d(TAG1, "notelist.getContent"+content);
                        break;
                    case R.id.action_refresh:
                        Toast.makeText(MainActivity.this, "刷新", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_delete:
                        Toast.makeText(MainActivity.this, "删除", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(context).setMessage("是否删除所有日记?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NoteDatabase deHelper = new NoteDatabase(context);
                                SQLiteDatabase db = deHelper.getWritableDatabase();
//                                根据表名删除表数据
//                                删除 notes 表的所有数据
                                db.delete("notes", null, null);
//                                重置id 为 1
                                db.execSQL("update sqlite_sequence set seq=0 where name='note'");
                                refreshListView();
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int returnMode;
        returnMode = data.getExtras().getInt("mode");
//        新建笔记
        if (returnMode == 0) {
            String content = data.getExtras().getString("content");
            long testid = data.getLongExtra("testId",000);
            String time = data.getStringExtra("time");
            int tag = data.getIntExtra("tag",9999);
//            Log.d("999", testid+"");
//            Log.d("999", content);
//            Log.d("999", time+"");
//            Log.d("999", tag+"");
            Note note = new Note(content, time, tag);
//            String testC = note.toString();
//            Log.d(TAG1, "note toString的值为" + testC);
            CRUD crud = new CRUD(context);
            crud.open();
            crud.addNote(note);
            crud.close();
        }

        refreshListView();
        super.onActivityResult(requestCode, resultCode, data);
    }

    //    加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    列表长按点击事件


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.d(TAG1, "onItemLongClick: 执行了");
//        Log.d(TAG1, parent.getId()+"");
//        switch (parent.getId()) {
//            case R.id.lv:
//                Log.d(TAG1, "onItemLongClick: 执行了");
//                final Note deletedNote = noteList.get(position);
//                new AlertDialog.Builder(context).setMessage("确认删除该日记吗?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        CRUD op = new CRUD(context);
//                        op.open();
//                        op.removeNote(deletedNote);
//                        op.close();
//                        refreshListView();
//                    }
//                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).create().show();
//                break;
//        }
//
//
//    }

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
