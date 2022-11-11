package com.example.mynotebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CRUD {
    private static final String TAG1 = "MainActivity66666";
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;

    private static final String[] columns = {
            NoteDatabase.ID,
            NoteDatabase.CONTENT,
            NoteDatabase.TIME,
            NoteDatabase.MODE
    };

    public CRUD(Context context) {
        dbHandler = new NoteDatabase(context);
    }

    public void open() {
        Log.d(TAG1, "open: ");
        db = dbHandler.getWritableDatabase();
    }

    public void close() {
        Log.d(TAG1, "close: ");
        dbHandler.close();
    }

//    把笔记加入到 database
    public Note addNote(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatabase.CONTENT, note.getContent());
        contentValues.put(NoteDatabase.TIME, note.getTime());
        contentValues.put(NoteDatabase.MODE,note.getTag());

        long insertId = db.insert(NoteDatabase.TABLE_NAME, "null", contentValues);

        note.setId(insertId);
        Log.d(TAG1, "2 note toString的值为"+note.toString());
        return note;
    }



    //    从database中获取单个笔记
//    public Note getNote(long id) {
//        Cursor cursor = db.query(NoteDatabase.TABLE_NAME, columns, NoteDatabase.ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//        Note note = new Note(cursor.getString(1), cursor.getString(2));
//        return note;
//    }

    //    从database中获取所右笔记的列表
    public List<Note> getAllNotes() {
        Log.d(TAG1, "getAllnotes");

//        Cursor cursor = db.query(NoteDatabase.TABLE_NAME, columns, null, null, null, null, null);
        String s = db.toString();
        Cursor cursor = db.query(NoteDatabase.TABLE_NAME,columns,null,null,null, null, null);
        Log.d(TAG1, "cursor的值为："+cursor.getCount());
        Log.d(TAG1, "db的值为："+s);
        List<Note> notes = new ArrayList<>();

        Log.d(TAG1, "cursor的值是："+cursor.getCount());
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                Note note = new Note();
                int index = cursor.getColumnIndex(NoteDatabase.ID);
                note.setId(cursor.getLong(index));
                index = cursor.getColumnIndex(NoteDatabase.CONTENT);
                note.setContent(cursor.getString(index));
                index = cursor.getColumnIndex(NoteDatabase.TIME);
                note.setTime(cursor.getString(index));
                index = cursor.getColumnIndex(NoteDatabase.MODE);
                note.setTag(cursor.getInt(index));
                notes.add(note);
                Log.d(TAG1, "是否有内容"+note.toString());
            }
        }
        Log.d(TAG1, notes.size()+"");
        return notes;
    }

//    根据id删除目标日记
    public void removeNote(Note deletedNote) {
        db.delete(NoteDatabase.TABLE_NAME, NoteDatabase.ID + "=" + deletedNote.getId(), null);
    }
}
