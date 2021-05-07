package com.pbph.pcc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.adapter.DataSpeAdapter;
import com.android.ui.FlowLayout;
import com.android.ui.OnSingleClickListener;
import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.GetUserStoreGoodsSeclectBean;
import com.pbph.pcc.bean.response.GetUserStoreGoodsSeclectBean.DataBean.StoreListEntityBean;
import com.pbph.pcc.db.ShopSearchRecord;
import com.pbph.pcc.db.ShopSearchRecordDao;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.viewholder.ReleaseOrderSearchRestaurantGoodViewHolder;
import com.pbph.pcc.viewholder.ReleaseOrderSearchRestaurantShopViewHolder;
import com.utils.StringUtils;

import java.util.Date;
import java.util.List;


public class ReleaseOrderRestaurantSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    int schoolId;

    private PccApplication application = null;
    Context context = this;
    LayoutInflater inflater;


    /////////////////

    private EditText tb_center;


    /////////////////

    private ListView listView;
    private DataSpeAdapter adapter;

    private FlowLayout flowLayout;
    private View textViewToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        application = (PccApplication) getApplication();
        inflater = LayoutInflater.from(context);

        Intent intent = getIntent();

        schoolId = Integer.parseInt(application.getMyInfoData().getSchoolId());


        setContentView(R.layout.activity_releaseordersearch);

        View left = findViewById(R.id.titlebar_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(onTitleClick);
        View right = findViewById(R.id.titlebar_right);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(onTitleClick);

        tb_center = (EditText) findViewById(R.id.mall_go_search);
        tb_center.setHint("点击搜索");
        tb_center.addTextChangedListener(textWatcher);
        tb_center.setOnEditorActionListener(onEditorActionListener);


        listView = (ListView) findViewById(R.id.classify_mainlist);

        flowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        flowLayout.setMargins(20);

        textViewToast = findViewById(R.id.TextViewToast);
        textViewToast.setVisibility(View.GONE);

        View view = findViewById(R.id.emptyview);
        listView.setEmptyView(view);

        adapter = new DataSpeAdapter(this, listView, ReleaseOrderSearchRestaurantShopViewHolder.class, ReleaseOrderSearchRestaurantGoodViewHolder.class, new DataSpeAdapter.OnGetSon() {

            @Override
            public List getSonList(Object obj) {
                StoreListEntityBean vo = (StoreListEntityBean) obj;
                return vo.getGoodsList();
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        initRecord();


    }

    private void initRecord() {

        List<ShopSearchRecord> list = PccApplication.getDaoSession().getShopSearchRecordDao().queryBuilder().where(ShopSearchRecordDao.Properties.SearchType.eq(1)).orderDesc(ShopSearchRecordDao.Properties.CreateTime).limit(10).list();

        if (list == null || list.size() <= 0) return;

        for (int i = 0, count = list.size(); i < count; i++) {

            ShopSearchRecord vo = list.get(i);

            TextView cb;
            if (i < flowLayout.getChildCount()) {
                cb = (TextView) flowLayout.getChildAt(i);
                setRecordTextView(cb, i, vo.getSearchName());
            } else {
                cb = (TextView) inflater.inflate(R.layout.layout_textview, null);
                setRecordTextView(cb, i, vo.getSearchName());
                flowLayout.addViewByLayoutParams(cb);
            }
        }
    }

    private void setRecordTextView(TextView cb, int i, String str) {
        cb.setId(i);
        cb.setText(str);
        cb.setOnClickListener(onFlowClickListener);
        cb.setMaxLines(1);
    }

    OnSingleClickListener onFlowClickListener = new OnSingleClickListener() {
        @Override
        public void onCanClick(View view) {
            TextView tv = (TextView) view;
            String str = tv.getText().toString().trim();
            tb_center.setText(str);
            doSearch(str);
        }
    };


    OnSingleClickListener onTitleClick = new OnSingleClickListener() {
        @Override
        public void onCanClick(View view) {
            switch (view.getId()) {
                case R.id.titlebar_left: {
                    finish();
                }
                break;
                case R.id.titlebar_right: {
                    doSearch(tb_center.getText().toString().trim());
                }
                break;

            }
        }
    };
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
//            doSearch(s.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //当actionId == XX_SEND 或者 XX_DONE时都触发
            //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
            //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
            if (actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                //处理事件
                doSearch(tb_center.getText().toString().trim());
            }
            return false;
        }
    };

    String search_text;

    private void doSearch(String str) {

        if (StringUtils.isEmpty(str)) {
            adapter.clearDatas();
            return;
        }

        if (StringUtils.isEqual(str, search_text)) {
            return;
        }
        search_text = str;

        ShopSearchRecordDao dao = PccApplication.getDaoSession().getShopSearchRecordDao();
        List<ShopSearchRecord> list = dao.queryBuilder().where(ShopSearchRecordDao.Properties.SearchType.eq(1)).where(ShopSearchRecordDao.Properties.SearchName.eq(str)).list();
        if (list == null || list.size() <= 0) {
            ShopSearchRecord vo = new ShopSearchRecord();
            vo.setSearchName(str);
            vo.setCreateTime(new Date());
            vo.setSearchType(1);
            dao.insert(vo);
        } else {
            ShopSearchRecord vo = list.get(0);
            vo.setCreateTime(new Date());
            dao.update(vo);
        }

        initRecord();

        getUserStoreGoodsSeclect(str);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(context, RestaurantGoodsActivity.class);

        DataSpeAdapter.KV kv = adapter.getKV(i);
        StoreListEntityBean fvo = (StoreListEntityBean) adapter.getItemByFS(kv.fpos, -1);
        intent.putExtra("storeId", fvo.getStoreId());
        intent.putExtra("storeName", fvo.getStoreName());
        intent.putExtra("storeImg", fvo.getStoreImg());

//        如果判断成立标识点击的是商品否则认为点击的是商店
        if (kv.spos >= 0) {
            intent.putExtra("storeGoodsId", fvo.getGoodsList().get(kv.spos).getStoreGoodsId());
        }
        startActivity(intent);
    }

    private void getUserStoreGoodsSeclect(final String searchStr) {

        textViewToast.setVisibility(View.GONE);

        adapter.clearDatas();

        HttpAction.getInstance().getUserStoreGoodsSeclect(schoolId, searchStr, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {
                    if (!StringUtils.isEqual(searchStr, search_text)) {
                        return;
                    }

                    GetUserStoreGoodsSeclectBean vo = JsonMananger.jsonToBean(response.body(), GetUserStoreGoodsSeclectBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<StoreListEntityBean> datas = vo.getData().getStoreListEntity();
                    if (datas == null || datas.size() <= 0) {
                        textViewToast.setVisibility(View.VISIBLE);
                        return;
                    }
                    adapter.setDatas(datas);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onDestroy() {
        HttpAction.getInstance().getUserStoreGoodsSeclectCancel();
        super.onDestroy();
    }

}



