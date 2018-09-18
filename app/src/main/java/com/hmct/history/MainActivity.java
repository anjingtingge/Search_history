package com.hmct.history;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FlowLayout mFlowLayout;
    private LayoutInflater mInflater;
    private List<String> list;

    /************
     * 以上为流式标签相关
     ************/
    public static final String EXTRA_KEY_TYPE = "extra_key_type";
    public static final String EXTRA_KEY_KEYWORD = "extra_key_keyword";
    public static final String KEY_SEARCH_HISTORY_KEYWORD = "key_search_history_keyword";
    private SharedPreferences mPref;//使用SharedPreferences记录搜索历史
    private EditText input;
    private Button btn_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFlowView();
        initSearchHistory();
    }


    private void initFlowView() {
        mInflater = LayoutInflater.from(this);
        mFlowLayout = (FlowLayout) findViewById(R.id.flowlayout);
        input = (EditText) findViewById(R.id.et_input);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

        mPref = getSharedPreferences("input", Activity.MODE_PRIVATE);

        String keyword = getIntent().getStringExtra(EXTRA_KEY_KEYWORD);
        if (!TextUtils.isEmpty(keyword)) {
            input.setText(keyword);
        }

        input = (EditText) findViewById(R.id.et_input);
    }


    /**
     * 储存搜索历史
     */
    public void save() {
        String text = input.getText().toString();
        String oldText = mPref.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
        Log.e("tag", "" + oldText);
        Log.e("Tag", "" + text);
        Log.e("Tag", "" + oldText.contains(text));
        if (!TextUtils.isEmpty(text) && !(oldText.contains(text))) {
            if (list.size()> 11) {
                //最多保存条数
                return;
            }
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(KEY_SEARCH_HISTORY_KEYWORD, text + "," + oldText);
            editor.commit();

        }

    }


    /**
     * 初始化搜索历史记录
     */
    public void initSearchHistory() {

        String history = mPref.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
        if (!TextUtils.isEmpty(history)) {
            list = new ArrayList<String>();
            for (Object o : history.split(",")) {
                list.add((String) o);
            }
        }


        if (list.size()> 0) {
            mFlowLayout.setVisibility(View.VISIBLE);
        } else {
            mFlowLayout.setVisibility(View.GONE);
        }

    }


    /**
     * 清除历史纪录
     */
    public void cleanHistory() {
        mPref = getSharedPreferences("input", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(KEY_SEARCH_HISTORY_KEYWORD).commit();
        mFlowLayout.setVisibility(View.GONE);
        Toast.makeText(MainActivity.this, "清楚搜索历史成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                String keywords = input.getText().toString();
                if (!TextUtils.isEmpty(keywords)) {
                    Toast.makeText(MainActivity.this, keywords + "save成功", Toast.LENGTH_LONG).show();
                    save();
                    if (list.size()<12){
                        list.add(keywords);
                    }
                    else {
                        System.out.println("list: " + list);
                        list.remove(0);
                        list.add(keywords);
                    }


                    mFlowLayout.removeAllViews();


                    for (int i = 0; i < list.size(); i++) {
                        TextView tv = (TextView) mInflater.inflate(
                                R.layout.search_label_tv, mFlowLayout, false);
                        tv.setText(list.get(list.size()-1-i));
                        final String str = tv.getText().toString();
                        //点击事件
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //加入搜索历史纪录记录
                                Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
                            }
                        });
                        mFlowLayout.addView(tv);
                    }


                } else {
                    Toast.makeText(MainActivity.this, "请输入搜索内容", Toast.LENGTH_LONG).show();
                }
                break;
//            case R.id.clear_history_btn:
//                cleanHistory();
//                break;
            default:
                break;
        }

    }
}
