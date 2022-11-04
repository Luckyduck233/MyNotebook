package com.example.mynotebook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends BaseAdapter {
    private Context mContent;
    private List<Note> noteList;

    public NoteAdapter(Context context, List noteList) {
        this.mContent = context;
        this.noteList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContent, R.layout.note_item_layout, null);
        TextView tv_content = view.findViewById(R.id.lv_item_name);
        TextView tv_time = view.findViewById(R.id.lv_time);

//        设置TV的文字内容

        tv_content.setText(noteList.get(position).getContent());
        tv_time.setText(noteList.get(position).getTime());
        return view;
    }
}
