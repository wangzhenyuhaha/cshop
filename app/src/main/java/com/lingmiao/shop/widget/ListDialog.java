package com.lingmiao.shop.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.lingmiao.shop.R;

import java.util.ArrayList;
import java.util.List;

public class ListDialog extends Dialog implements AdapterView.OnItemClickListener {
    private DialogItemClickListener mLisetner;
    private Context mContext;
    private List<String> mDataList = new ArrayList<>();
    private DialogListAdapter mAdapter;
    private int chooseIndex = -1;
    private Boolean mDialogCancle = true;       //item选择完后dialog消息

    public ListDialog(Context context, DialogItemClickListener lisetner, List<String> mDataList, int index) {
        super(context);
        this.mLisetner = lisetner;
        this.mContext = context;
        this.mDataList = mDataList;
        this.chooseIndex = index;
    }
    public ListDialog(Context context, DialogItemClickListener lisetner, List<String> mDataList, int index, Boolean cancleValue) {
        super(context);
        this.mLisetner = lisetner;
        this.mContext = context;
        this.mDataList = mDataList;
        this.chooseIndex = index;
        this.mDialogCancle = cancleValue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list_center);
        initView();
    }

    private void initView() {
        ListView listview = (ListView) findViewById(R.id.dlc_list);
        mAdapter = new DialogListAdapter();
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mLisetner.onItemClik(position, (String) mDataList.get(position).split("@")[0], (String) mDataList.get(position).split("@")[1], chooseIndex);
        if(mDialogCancle) dismiss();
    }

    public interface DialogItemClickListener {
        public void onItemClik(int position, String id, String text, int chooseIndex);
    }

    public class DialogListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_text_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(mDataList.get(position).split("@")[1]);
            return convertView;
        }

        public class ViewHolder {
            private TextView text;

            public ViewHolder(View itemView) {
                text = itemView.findViewById(R.id.lt_text);
            }
        }
    }
}