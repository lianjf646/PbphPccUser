package com.pbph.pcc.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.adapter.DataAdapter;
import com.android.ui.OnSingleClickListener;
import com.android.utils.LogUtils;
import com.android.utils.PinyingUtil;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.MyUserInfo;
import com.pbph.pcc.bean.response.SchoolBean;
import com.pbph.pcc.bean.response.SchoolNotOpenBean;
import com.pbph.pcc.bean.response.SetSchoolInfoBean;
import com.pbph.pcc.broadcast.BroadcastManager;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.viewholder.SelectCity1ViewHolder;
import com.pbph.pcc.viewholder.SelectCity2ViewHolder;
import com.pbph.pcc.viewholder.SelectCity3ViewHolder;
import com.pbph.pcc.viewholder.SelectCityFastViewHolder;
import com.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * gdl:这个是 选择学校功能，查询 TODO  select 这个 就是最终选择的结果。如果对结果有什么处理请在那里处理
 **/

public class ChooseSchoolActivity extends Activity implements
        AdapterView.OnItemClickListener, TextWatcher {


    public Context context;
    private TextView textView;
    private EditText go_search;

    private ListView listView;
    private DataAdapter adapter;

    private ListView fastList;
    private DataAdapter fastAdapter;


    // 城市数据 key：首字母， value：vo
    Map<Character, List<SchoolBean.DataBean>> map = new HashMap<Character, List<SchoolBean.DataBean>>();

    // 城市数据 key：首字母， value：vo
    Map<Character, List<String>> mapNotOpen = new HashMap<Character, List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_chooseschool);

        textView = findViewById(R.id.titlebar_center);
        textView.setText("搜索学校");
        View left = findViewById(R.id.titlebar_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(onTitleClick);


        go_search = findViewById(R.id.mall_go_search);
        go_search.addTextChangedListener(this);

        listView = findViewById(R.id.ListViewmsggeducation);

        adapter = new DataAdapter(this, listView, 3);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        fastList = findViewById(R.id.fastbar);
        fastAdapter = new DataAdapter(this, fastList,
                SelectCityFastViewHolder.class);
        fastList.setAdapter(fastAdapter);
        fastList.setOnTouchListener(fastTouch);

        HttpAction.getInstance().querySchool(querySchoolCallback);

        HttpAction.getInstance().queryNotOpenSchool(queryNotOpenSchoolCallback);

    }

    StringCallback querySchoolCallback = new StringCallback() {
        @Override
        public void onSuccess(Response<String> response) {

            LogUtils.e("res=  " + response.body());
            try {
                SchoolBean vo = JsonMananger.jsonToBean(response.body(), SchoolBean.class);

                if (!StringUtils.isEqual(vo.code, "200")) {
                    Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                final List<SchoolBean.DataBean> datas = vo.getData();
                initAll(true, datas, null);
                if (datas == null || datas.size() <= 0) {
                    return;
                }


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    StringCallback queryNotOpenSchoolCallback = new StringCallback() {
        @Override
        public void onSuccess(Response<String> response) {

            LogUtils.e("res=  " + response.body());
            try {
                SchoolNotOpenBean vo = JsonMananger.jsonToBean(response.body(), SchoolNotOpenBean.class);

                if (!StringUtils.isEqual(vo.code, "200")) {
//                    Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                final List<String> datas = vo.getData();
                initAll(false, null, datas);
                if (datas == null || datas.size() <= 0) {
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onPause() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) imm.h(, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(go_search.getWindowToken(), 0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        HttpAction.getInstance().cancel(ConstantData.QUERYSCHOOL);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            PccApplication application = (PccApplication) getApplication();
            if (StringUtils.isEmpty(application.getMyInfoData().getSchoolId())) {
                Toast.makeText(context, "请选择学校", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    OnSingleClickListener onTitleClick = new OnSingleClickListener() {
        @Override
        public void onCanClick(View view) {
            switch (view.getId()) {
                case R.id.titlebar_left: {

                    PccApplication application = (PccApplication) getApplication();
                    if (StringUtils.isEmpty(application.getMyInfoData().getSchoolId())) {
                        Toast.makeText(context, "请选择学校", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    finish();
                }
                break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Class cls = adapter.getItemViewTypeClass(arg2);
        Object obj = adapter.getItem(arg2);

        if (cls == SelectCity1ViewHolder.class) {
        }
        if (cls == SelectCity2ViewHolder.class) {
            if (!(obj instanceof SchoolBean.DataBean)) {
                return;
            }
            select((SchoolBean.DataBean) obj);
        }
        if (cls == SelectCity3ViewHolder.class) {
        }
    }


    View.OnTouchListener fastTouch = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {
            try {

                int x = (int) event.getX();
                int y = (int) event.getY();

                if (x > 0 && y > 0 && x <= v.getWidth() && y <= v.getHeight()) {
                    int height = ((ListView) v).getChildAt(0).getHeight();
                    int pos = y / height;

                    pos = pos >= fastAdapter.getCount() ? fastAdapter
                            .getCount() - 1 : pos;
                    String py = (String) fastAdapter.getItem(pos);

                    for (int i = 0, count = adapter.getCount(); i < count; i++) {


                        Class cls = adapter.getItemViewTypeClass(i);
                        Object obj = adapter.getItem(i);

                        if (cls == SelectCity1ViewHolder.class) {
                            if (StringUtils.isEqual(py, "*") && StringUtils.isEqual("未开通", obj)) {
                                listView.setSelection(i);
                                break;
                            }
                            if (obj.equals(py)) {
                                listView.setSelection(i);
                                break;
                            }
                        }
                        if (cls == SelectCity2ViewHolder.class) {
                        }
                        if (cls == SelectCity3ViewHolder.class) {
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    };
    List<SchoolBean.DataBean> datasOpen;
    List<String> datasNotOpen;

    private synchronized void initAll(boolean b, List<SchoolBean.DataBean> tempDatasOpen, List<String> tempDatasNotOpen) {

        //这一大堆判断 其实仅仅做了一件事情，等待两次请求获取的数据都返回后，再执行接下来的事情 thread线程。
        if (b) {
            datasOpen = new ArrayList<>();
            if (tempDatasOpen != null) {
                datasOpen.addAll(tempDatasOpen);
            }
        } else {
            datasNotOpen = new ArrayList<>();
            if (tempDatasNotOpen != null) {
                datasNotOpen.addAll(tempDatasNotOpen);
            }
        }
        if (datasOpen == null) {
            return;
        }
        if (datasNotOpen == null) {
            return;
        }


        new Thread() {
            @Override
            public void run() {
                super.run();

                if (datasOpen != null && datasOpen.size() > 0) {
                    init(datasOpen);
                    datasOpen = null;
                }

                if (datasNotOpen != null && datasNotOpen.size() > 0) {
                    initNotOpen(datasNotOpen);
                    datasNotOpen = null;
                }

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void init(final List<SchoolBean.DataBean> datas) {
        for (SchoolBean.DataBean vo : datas) {

            String[] strs = PinyingUtil.getHeadByString(vo.getSchoolName()
                    .substring(0, 1));

            Character fst;
            if (strs == null || strs.length <= 0) {
                fst = '#';
            } else {
                try {
                    fst = strs[0].toUpperCase().charAt(0);
                } catch (Exception e) {
                    fst = '#';
                }
            }
            if (fst < 'A' || fst > 'Z') {
                fst = '#';
            }
            List<SchoolBean.DataBean> list = map.get(fst);
            if (list == null) {
                list = new ArrayList<SchoolBean.DataBean>();
                map.put(fst, list);
            }
            list.add(vo);
        }
    }


    private void initNotOpen(final List<String> datas) {
        for (String vo : datas) {

            String[] strs = PinyingUtil.getHeadByString(vo.substring(0, 1));

            Character fst;
            if (strs == null || strs.length <= 0) {
                fst = '#';
            } else {
                try {
                    fst = strs[0].toUpperCase().charAt(0);
                } catch (Exception e) {
                    fst = '#';
                }
            }
            if (fst < 'A' || fst > 'Z') {
                fst = '#';
            }
            List<String> list = mapNotOpen.get(fst);
            if (list == null) {
                list = new ArrayList<String>();
                mapNotOpen.put(fst, list);
            }
            list.add(vo);
        }
    }


    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            setDatas();
            setDatasNotOpen();

        }
    };


    private void setDatas() {

        char ch = '#';
        List<SchoolBean.DataBean> list = map.get(ch);
        if (list != null && list.size() > 0) {
            String first = String.valueOf(ch);
            adapter.addData(first, SelectCity1ViewHolder.class);
            fastAdapter.addDataJust(first);
            for (SchoolBean.DataBean vo : list) {
                adapter.addData(vo, SelectCity2ViewHolder.class);
            }
        }

        for (int i = 'A'; i <= 'Z'; i++) {
            ch = (char) i;
            list = map.get(ch);
            if (list != null && list.size() > 0) {
                String first = String.valueOf(ch);
                adapter.addData(first, SelectCity1ViewHolder.class);
                fastAdapter.addDataJust(first);
                for (SchoolBean.DataBean vo : list) {
                    adapter.addData(vo, SelectCity2ViewHolder.class);
                }
            }
        }

        fastAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }


    private void setDatasNotOpen() {

        adapter.addData("未开通", SelectCity1ViewHolder.class);
        fastAdapter.addDataJust("*");

        char ch = '#';
        List<String> list = mapNotOpen.get(ch);
        if (list != null && list.size() > 0) {
            for (String vo : list) {
                adapter.addData(vo, SelectCity3ViewHolder.class);
            }
        }

        for (int i = 'A'; i <= 'Z'; i++) {
            ch = (char) i;
            list = mapNotOpen.get(ch);
            if (list == null || list.size() <= 0) {
                continue;
            }
            for (String vo : list) {
                adapter.addData(vo, SelectCity3ViewHolder.class);
            }
        }

        fastAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    private void search(String str) {

        fastAdapter.clearDatas();
        adapter.clearDatas();

        if (StringUtils.isEmpty(str)) {
            setDatas();
            setDatasNotOpen();
            return;
        }

        char ch = '#';
        List<SchoolBean.DataBean> list = map.get(ch);
        if (list != null && list.size() > 0) {
            boolean b = false;
            int pos = adapter.getCount();

            String first = String.valueOf(ch);

            for (SchoolBean.DataBean vo : list) {
                if (StringUtils.isEmpty(str) || vo.getSchoolName().indexOf(str) != -1) {
                    adapter.addData(vo, SelectCity2ViewHolder.class);
                    b = true;
                }
            }
            if (b) {
                fastAdapter.addData(first);
                adapter.addData(pos, first, SelectCity1ViewHolder.class);
            }
        }

        for (int i = 'A'; i <= 'Z'; i++) {
            ch = (char) i;
            list = map.get(ch);
            if (list != null && list.size() > 0) {
                boolean b = false;
                int pos = adapter.getCount();

                String first = String.valueOf(ch);

                for (SchoolBean.DataBean vo : list) {
                    if (StringUtils.isEmpty(str) || vo.getSchoolName().indexOf(str) != -1) {
                        adapter.addData(vo, SelectCity2ViewHolder.class);
                        b = true;
                    }
                }
                if (b) {
                    fastAdapter.addData(first);
                    adapter.addData(pos, first, SelectCity1ViewHolder.class);
                }
            }
        }
        fastAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        search(s.toString());
    }

    // 这里是 你想要获取结果的最后一步 item 就是需要的那个数据。
    private void select(SchoolBean.DataBean item) {

        PccApplication application = (PccApplication) getApplication();
        if (StringUtils.isEqual(application.getMyInfoData().getSchoolId(), item.getSchoolId())) {
            finish();
            return;
        }
        setSchoolInfo(item);
    }


    private void setSchoolInfo(final SchoolBean.DataBean item) {

        HttpAction.getInstance().setSchoolInfo(PccApplication.getUserid(), item.getSchoolId(), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {

                    SetSchoolInfoBean vo = JsonMananger.jsonToBean(response.body(), SetSchoolInfoBean.class);


                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!StringUtils.isEqual(item.getSchoolId(), vo.getData().getSchoolId()))
                        return;

                    PccApplication application = (PccApplication) getApplication();
                    MyUserInfo userInfo = application.getMyInfoData();

                    userInfo.setSchoolId(item.getSchoolId());
                    userInfo.setSchoolName(item.getSchoolName());

                    userInfo.setRaddressName(vo.getData().getRaddressName());
                    userInfo.setReceiveId(vo.getData().getReceiveId());
                    userInfo.setReceiveName(vo.getData().getReceiveName());
                    userInfo.setReceivePhone(vo.getData().getReceivePhone());
                    userInfo.setReceiveSex(vo.getData().getReceiveSex());

                    BroadcastManager.getInstance(ChooseSchoolActivity.this).sendBroadcast(ConstantData.REFRESH_BANNER_ACITON);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.connect_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
