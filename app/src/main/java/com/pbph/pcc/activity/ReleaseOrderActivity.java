package com.pbph.pcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.ui.OnSingleClickListener;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.fragment.CommissionOrderFragment;
import com.pbph.pcc.fragment.ExpressOrderFragment;
import com.pbph.pcc.fragment.MarketOrderFragment;
import com.pbph.pcc.fragment.RestaurantOrderFragment;
import com.pbph.pcc.jpush.MyReceiver;


public class ReleaseOrderActivity extends AppCompatActivity {

    private View titlebar1, titlebar2;

    private TextView tv_title;

    int position = 0;
    ///
    private FragmentTabHost mTabHost = null;
    private Class fragmentArray[] = {ExpressOrderFragment.class, RestaurantOrderFragment.class, CommissionOrderFragment.class, MarketOrderFragment.class};
    private int mImageViewArray[] = {R.drawable.relese_order_tab_3_bg_selecter, R.drawable.relese_order_tab_1_bg_selecter, R.drawable.relese_order_tab_4_bg_selecter, R.drawable.relese_order_tab_2_bg_selecter};
    private String mTextviewArray[] = {"快递代取", "食堂外卖", "跑腿代办", "超市代购"};
    private LayoutInflater layoutInflater = null;
    MyReceiver receiver;
    private PccApplication application = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getIntent().getIntExtra("position", 0);

        setContentView(R.layout.activity_relese_order);
        application = (PccApplication) getApplication();
        initTitlebar();
        changeTitlebar(position);
        initView();
    }


    public void setTab(int position) {
        mTabHost.setCurrentTab(position);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
        //RongIM.getInstance().logout();
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
        //RongIM.getInstance().logout();
    }

    private void initTitlebar() {
        titlebar1 = findViewById(R.id.includetitlebar1);
        View left = titlebar1.findViewById(R.id.titlebar_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(onTitleClick);
        View right = titlebar1.findViewById(R.id.titlebar_right);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(onTitleClick);

        EditText tb_center = titlebar1.findViewById(R.id.mall_go_search);
        tb_center.setText("点击搜索");
        tb_center.setFocusable(false);
        tb_center.setClickable(true);
        tb_center.setOnClickListener(onTitleClick);


        titlebar2 = findViewById(R.id.includetitlebar2);
        left = titlebar2.findViewById(R.id.btn_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(onTitleClick);

        tv_title = titlebar2.findViewById(R.id.tv_title);
    }

    OnSingleClickListener onTitleClick = new OnSingleClickListener() {
        @Override
        public void onCanClick(View view) {
            switch (view.getId()) {
                case R.id.titlebar_left: {
                    finish();
                }
                break;
                case R.id.btn_left: {
                    finish();
                }
                break;
                case R.id.titlebar_right: {
                    if (position == 1) {
                        startActivity(new Intent(ReleaseOrderActivity.this, ReleaseOrderRestaurantSearchActivity.class));
                        return;
                    }
                    if (position == 3) {


                        MarketOrderFragment fragment = (MarketOrderFragment) getSupportFragmentManager().findFragmentByTag("3");

                        if (fragment.productCategorizes == null) {
                            return;
                        }

                        Intent intent = new Intent(ReleaseOrderActivity.this, MarketGoodsSearchActivity.class);

                        PccApplication.setDataMapData("MarketGoodsSearchActivityList", fragment.productCategorizes);

                        startActivity(intent);
                        return;
                    }
                }
                break;
                case R.id.mall_go_search: {
                    if (position == 1) {
                        startActivity(new Intent(ReleaseOrderActivity.this, ReleaseOrderRestaurantSearchActivity.class));
                        return;
                    }
                    if (position == 3) {

                        MarketOrderFragment fragment = (MarketOrderFragment) getSupportFragmentManager().findFragmentByTag("3");

                        if (fragment.productCategorizes == null) {
                            return;
                        }
                        
                        Intent intent = new Intent(ReleaseOrderActivity.this, MarketGoodsSearchActivity.class);

                        PccApplication.setDataMapData("MarketGoodsSearchActivityList", fragment.productCategorizes);

                        startActivity(intent);
                        return;
                    }
                }
                break;

            }
        }
    };


    private void initView() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);
        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //getSupportFragmentManager().beginTransaction().
        //得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(String.valueOf(i)).setIndicator(getTabItemView(i));

            //将Tab按钮添加进Tab选项卡中
//            Bundle bundle = new Bundle();
//            bundle.putString("url", url[i]);
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.white);
        }
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.getTabWidget().setBackgroundResource(R.color.main_tab_color_bg);

        mTabHost.setCurrentTab(position);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                position = Integer.valueOf(tabId);
                changeTitlebar(position);
            }
        });
    }

    private void changeTitlebar(int position) {
        boolean b = true;
        switch (position) {
            case 0: {
                b = false;
                tv_title.setText("快递");
            }
            break;
            case 1: {
                b = true;
            }
            break;
            case 2: {
                b = false;
                tv_title.setText("任务");
            }
            break;
            case 3: {
                b = true;
            }
            break;
        }
        changeTitlebar(b);
    }

    private void changeTitlebar(boolean b) {
        titlebar1.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        titlebar2.setVisibility((!b) ? View.VISIBLE : View.INVISIBLE);
    }

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);
        TextView textView = view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);
        return view;
    }


}
