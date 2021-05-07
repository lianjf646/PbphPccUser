package com.pbph.pcc.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.OnSingleClickListener;
import com.pbph.pcc.R;
import com.pbph.pcc.adapter.MyShopCarAdapter;
import com.pbph.pcc.adapter.ShopSearchAllAdapter;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.GetUserStoreGoodsBean.DataBean.GoodsListEntityBean;
import com.pbph.pcc.bean.response.GetUserStoreGoodsBean.DataBean.GoodsListEntityBean.StoreGoodsListBean;
import com.pbph.pcc.db.ShoppingCarC;
import com.pbph.pcc.db.ShoppingCarCDao;
import com.pbph.pcc.db.ShoppingCarP;
import com.pbph.pcc.db.ShoppingCarPDao;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.MoneyUtils;
import com.pbph.pcc.viewholder.MyShopCarRestaurantViewHolder;
import com.utils.DoubleUtil;
import com.utils.StringUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RestaurantGoodsSearchActivity extends AppCompatActivity implements ShopSearchAllAdapter.OnCallBackListener, MyShopCarAdapter.OnFlushDataListener {

    private PccApplication application = null;
    Context context = this;

    //max_goods_count用作点击加减按钮判断最大商品数时使用。
//    max_goods_count_temp只是最终点击结算的时候判断用的
//    如果以后希望点击加减也可也动态判断，那么就把他俩统一起来并删除其中一个就好。
    private final int max_goods_count = 10;
//    private final int max_goods_count_temp = 10;

    private int now_goods_count = 0;

    String schoolId;
    String userId;

    int takeStoreNum = 0;
    double takeSoodsPrice = 0;
    double takeSoodsPrices = 0;

    int storeId;
    String storeName;
    String storeImg;

    //所有商品信息
    private List<GoodsListEntityBean> productCategorizes;


    /////////////////

    private EditText tb_center;


    /////////////////
    private ListView listView;
    private ShopSearchAllAdapter adapter;


    ///////////////////////
//    底部 购物车拦
    private ImageView shopping_cart;//购物车
    private TextView shoppingNum;//购物车件数

    private TextView shoppingPrise;//购物车价格
    private TextView shoppingToast;//购物车提示
    private TextView settlement;//去结算

    ////////////////////////
//购物车列表pop
    private View cardLayout;

    private View bg_layout;

    private LinearLayout cardShopLayout;

    private ListView shopListView;
    private MyShopCarAdapter shopAdapter;

    private TextView defaultText;


    ///////////////
    // 正在执行的动画数量
    private int number = 0;
    // 是否完成清理
    private boolean isClean = false;
    private FrameLayout animation_viewGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (PccApplication) getApplication();

        schoolId = application.getMyInfoData().getSchoolId();
        userId = application.getUserid();

        Intent intent = getIntent();
        storeId = intent.getIntExtra("storeId", -1);

        storeName = intent.getStringExtra("storeName");
        storeImg = intent.getStringExtra("storeImg");


        productCategorizes = (List<GoodsListEntityBean>) PccApplication.getDataMapData("RestaurantGoodsSearchActivityList");

        if (storeId <= -1) {
            Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
            return;
        }
        animation_viewGroup = createAnimLayout();

        setContentView(R.layout.activity_restaurantgoodssearch);

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

        shopping_cart = (ImageView) findViewById(R.id.shopping_cart);
        shoppingPrise = (TextView) findViewById(R.id.shoppingPrise);
        shoppingToast = (TextView) findViewById(R.id.shoppingToast);
        shoppingNum = (TextView) findViewById(R.id.shoppingNum);
        settlement = (TextView) findViewById(R.id.settlement);


        cardLayout = findViewById(R.id.layout_settle_account_list);

        bg_layout = findViewById(R.id.bg_layout);
        cardShopLayout = (LinearLayout) findViewById(R.id.cardShopLayout);
        shopListView = (ListView) findViewById(R.id.shopproductListView);
        defaultText = (TextView) findViewById(R.id.defaultText);
        shopListView.setEmptyView(defaultText);

        initData();

        flushDatas();
    }


    private Map<Integer, ShoppingCarC> getDaoGoodRecords() {

        now_goods_count = 0;

        ShoppingCarCDao dao = PccApplication.getDaoSession().getShoppingCarCDao();
        List<ShoppingCarC> list = dao.queryBuilder().where(
                ShoppingCarCDao.Properties.ShopId.eq(storeId),
                ShoppingCarCDao.Properties.UserId.eq(userId),
                ShoppingCarCDao.Properties.SchoolId.eq(schoolId)).list();

        if (list == null || list.size() <= 0) return null;

        Map<Integer, ShoppingCarC> map = new HashMap<>();


        for (int i = 0, count = list.size(); i < count; i++) {
            ShoppingCarC vo = list.get(i);
            map.put(Integer.parseInt(vo.getGoodId()), vo);

            now_goods_count += vo.getGoodNum();
        }
        return map;
    }


    volatile boolean isInsert = false;

    ShoppingCarP shoppingCarP;

    private void insertStore() {

        if (isInsert) return;

        ShoppingCarPDao dao = PccApplication.getDaoSession().getShoppingCarPDao();

        List<ShoppingCarP> list = dao.queryBuilder().where(
                ShoppingCarPDao.Properties.ShopId.eq(storeId),
                ShoppingCarPDao.Properties.UserId.eq(userId),
                ShoppingCarPDao.Properties.SchoolId.eq(schoolId)
        ).list();
        if (list == null || list.size() <= 0) {
            shoppingCarP = new ShoppingCarP();

            shoppingCarP.setShopId(String.valueOf(storeId));
            shoppingCarP.setShopName(storeName);
            shoppingCarP.setShopImgUrl(storeImg);
            shoppingCarP.setShopIsChecked(true);

            shoppingCarP.setUserId(String.valueOf(userId));
            shoppingCarP.setSchoolId(String.valueOf(schoolId));


            shoppingCarP.setTakeStoreNum(takeStoreNum);
            shoppingCarP.setTakeSoodsPrice(takeSoodsPrice);
            shoppingCarP.setTakeSoodsPrices(takeSoodsPrices);


            long id = dao.insert(shoppingCarP);

            shoppingCarP.setId(id);
        } else {
//            ShopSearchRecord vo = list.get(0);
//            vo.setCreateTime(new Date());
//            dao.update(vo);
            shoppingCarP = list.get(0);
        }
        isInsert = true;
    }

    private void updateGood(GoodsListEntityBean.StoreGoodsListBean bean) {

        insertStore();

        ShoppingCarCDao dao = PccApplication.getDaoSession().getShoppingCarCDao();
        List<ShoppingCarC> list = dao.queryBuilder().where(
                ShoppingCarCDao.Properties.GoodId.eq(bean.getStoreGoodsId()),
                ShoppingCarCDao.Properties.UserId.eq(userId),
                ShoppingCarCDao.Properties.SchoolId.eq(schoolId)
        ).list();
        if (bean.getNumber() <= 0 && list != null && list.size() > 0) {
            dao.deleteByKey(bean.id);
            bean.id = null;
        } else if (bean.getNumber() > 0) {
            if (list == null || list.size() <= 0) {
                bean.id = dao.insert(bean2Vo(bean));
            } else {
                if (bean.id == null) bean.id = list.get(0).getId();
                dao.update(bean2Vo(bean));
            }
        }
    }

    private void vo2Bean(ShoppingCarC vo, GoodsListEntityBean.StoreGoodsListBean bean) {

        bean.id = vo.getId();

        bean.setNumber(vo.getGoodNum());

    }

    private ShoppingCarC bean2Vo(GoodsListEntityBean.StoreGoodsListBean bean) {
        ShoppingCarC vo = new ShoppingCarC();

        vo.setId(bean.id);

        vo.setShopId(String.valueOf(storeId));
        vo.setGoodId(String.valueOf(bean.getStoreGoodsId()));
        vo.setGoodImgUrl(bean.getStoreGoodsImg());
        vo.setGoodName(bean.getStoreGoodsName());
        vo.setGoodPrice(bean.getStoreGoodsPrice());
        vo.setGoodIsChecked(true);

        vo.setUserId(String.valueOf(userId));
        vo.setSchoolId(String.valueOf(schoolId));


        vo.setGoodNum(bean.getNumber());

        return vo;
    }


    OnSingleClickListener onTitleClick = new OnSingleClickListener() {
        @Override
        public void onCanClick(View view) {
            switch (view.getId()) {
                case R.id.titlebar_left: {
                    finish();
                }
                break;
                case R.id.titlebar_right: {
                    doSearch(tb_center.getText().toString());
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
                doSearch(tb_center.getText().toString());
            }
            return false;
        }
    };

    private void doSearch(String str) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) imm.h(, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(tb_center.getWindowToken(), 0);

        if (StringUtils.isEmpty(str)) return;

        adapter.searchList.clear();

        for (int i = 0, ic = productCategorizes.size(); i < ic; i++) {

            List<StoreGoodsListBean> list = productCategorizes.get(i).getStoreGoodsList();

            if (list == null || list.size() <= 0) continue;

            for (int j = 0, jc = list.size(); j < jc; j++) {
                StoreGoodsListBean vo = list.get(j);

                if (vo.getStoreGoodsName().indexOf(str) == -1) {
                    continue;
                }

                adapter.searchList.add(vo);
            }
        }
        adapter.notifyDataSetChanged();

    }


    private void flushDatas() {

        now_goods_count = 0;
        shopAdapter.clearDatas();
        clearData();

        if (productCategorizes == null || productCategorizes.size() <= 0) {
            shopAdapter.notifyDataSetChanged();
            return;
        }

        Map<Integer, ShoppingCarC> map = getDaoGoodRecords();


        if (map != null && map.size() > 0) {
            for (int i = 0, counti = productCategorizes.size(); i < counti; i++) {

                List<GoodsListEntityBean.StoreGoodsListBean> list = productCategorizes.get(i).getStoreGoodsList();
                if (list == null || list.size() <= 0) continue;

                for (int j = 0, countj = list.size(); j < countj; j++) {

                    GoodsListEntityBean.StoreGoodsListBean gvo = list.get(j);

                    ShoppingCarC scc = map.get(gvo.getStoreGoodsId());
                    if (scc != null) {
                        vo2Bean(scc, gvo);
                        shopAdapter.addData(gvo);
                    }
                }

            }
        }

        insertStore();
        setPrise();

        shopAdapter.notifyDataSetChanged();
    }


    public void initData() {


        adapter = new ShopSearchAllAdapter(context);

        adapter.SetOnSetHolderClickListener(new ShopSearchAllAdapter.HolderClickListener() {

            @Override
            public void onHolderClick(Drawable drawable, int[] start_location) {
                if (beyond) return;

                doAnim(drawable, start_location);
            }

        });
        listView.setAdapter(adapter);
        adapter.setCallBackListener(this);


        shopAdapter = new MyShopCarAdapter(this, shopListView, MyShopCarRestaurantViewHolder.class);
        shopAdapter.setOnFlushDataListener(this);
        shopListView.setAdapter(shopAdapter);

        bg_layout.setOnClickListener(onSingleClickListener);
        settlement.setOnClickListener(onSingleClickListener);
        shopping_cart.setOnClickListener(onSingleClickListener);

    }


    /**
     * 回调函数更新购物车和价格显示状态
     *
     * @param product
     * @param type
     */
    @Override
    public void updateProduct(GoodsListEntityBean.StoreGoodsListBean product, String type) {
        if (type.equals("1")) {

            boolean b = scaleMax(product, true);
            if (b) {
                if (!shopAdapter.contains(product)) {
                    shopAdapter.addData(product);
                }
            } else {
//                shopAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }


        } else if (type.equals("2")) {

            boolean b = scaleMax(product, false);
            if (b) {
                if (shopAdapter.contains(product)) {
                    if (product.getNumber() <= 0) {
                        product.setNumber(0);
                        shopAdapter.removeData(product);
                    }
                }
            } else {
//                shopAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        }
        shopAdapter.notifyDataSetChanged();

        updateGood(product);
        setPrise();
    }


    @Override
    public void onFlushData(Object obj, boolean type) {
        GoodsListEntityBean.StoreGoodsListBean product = (GoodsListEntityBean.StoreGoodsListBean) obj;
        if (type) {
            boolean b = scaleMax(product, true);
            if (b) {
                if (!shopAdapter.contains(product)) {
                    shopAdapter.addData(product);
                }
            } else {
                shopAdapter.notifyDataSetChanged();
//                allAdapter.notifyDataSetChanged();
            }
        } else {
            boolean b = scaleMax(product, false);
            if (b) {
                if (shopAdapter.contains(product)) {
                    if (product.getNumber() <= 0) {
                        product.setNumber(0);
                        shopAdapter.removeData(product);
                        shopAdapter.notifyDataSetChanged();
                    }
                }
            } else {
                shopAdapter.notifyDataSetChanged();
//                allAdapter.notifyDataSetChanged();
            }

        }
        adapter.notifyDataSetChanged();

        updateGood(product);
        setPrise();
    }


    private boolean beyond = false;

    private boolean scaleMax(GoodsListEntityBean.StoreGoodsListBean product, boolean add) {
        if (add) {
            beyond = false;
            now_goods_count++;
            if (now_goods_count <= max_goods_count) return true;

            beyond = true;
            now_goods_count--;
            product.setNumber(product.getNumber() - 1);

            Toast.makeText(context, "一单最多选择10件商品", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            now_goods_count--;
            if (now_goods_count >= 0) return true;

            now_goods_count++;
            product.setNumber(product.getNumber() + 1);

            return false;
        }
    }

    /**
     * 更新购物车价格
     */

    public void setPrise() {
        double money = 0;
        double cost;
        int shopNum = 0;


        for (int i = 0, c = shopAdapter.getCount(); i < c; i++) {
            GoodsListEntityBean.StoreGoodsListBean pro = (GoodsListEntityBean.StoreGoodsListBean) shopAdapter.getItem(i);

            double tempMoney = MoneyUtils.scaleMoney(pro.getStoreGoodsPrice(), pro.getNumber());


            money = DoubleUtil.sum(money, tempMoney);


            shopNum = shopNum + pro.getNumber();
        }

        cost = MoneyUtils.scaleCost(shopNum, takeSoodsPrice, takeStoreNum, takeSoodsPrices);

        shoppingNum.setVisibility(shopNum > 0 ? View.VISIBLE : View.GONE);
        shoppingPrise.setVisibility(money > 0 ? View.VISIBLE : View.GONE);

        shoppingPrise.setText("¥ " + (new DecimalFormat("0.00")).format(money));

        shoppingToast.setText("另需配送费");
        shoppingToast.append((new DecimalFormat("0.00")).format(cost));
        shoppingToast.append("元");

        shoppingNum.setText(String.valueOf(shopNum));

        shoppingCarP.setTotlePrice(money);
        shoppingCarP.setShippingAmount(cost);

        doInsertStore();
    }

    private void doInsertStore() {
        ShoppingCarPDao dao = PccApplication.getDaoSession().getShoppingCarPDao();

        List<ShoppingCarP> list = dao.queryBuilder().where(
                ShoppingCarPDao.Properties.ShopId.eq(storeId),
                ShoppingCarPDao.Properties.UserId.eq(userId),
                ShoppingCarPDao.Properties.SchoolId.eq(schoolId)
        ).list();
        if (list == null || list.size() <= 0) {

            shoppingCarP.setId(-1L);
            long id = dao.insert(shoppingCarP);
            shoppingCarP.setId(id);
        } else {
            dao.update(shoppingCarP);
        }
    }

    OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onCanClick(View v) {

            switch (v.getId()) {
                case R.id.shopping_cart:
                    if (shopAdapter == null || shopAdapter.getCount() <= 0) {
                        defaultText.setVisibility(View.VISIBLE);
                    } else {
                        defaultText.setVisibility(View.GONE);
                    }

                    if (cardLayout.getVisibility() == View.GONE) {
                        cardLayout.setVisibility(View.VISIBLE);

                        // 加载动画
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in);
                        // 动画开始
                        cardShopLayout.setVisibility(View.VISIBLE);
                        cardShopLayout.startAnimation(animation);
                        bg_layout.setVisibility(View.VISIBLE);

                    } else {
                        cardLayout.setVisibility(View.GONE);
                        bg_layout.setVisibility(View.GONE);
                        cardShopLayout.setVisibility(View.GONE);
                    }
                    break;

                case R.id.settlement:
                    if (shopAdapter == null || shopAdapter.getCount() <= 0) {
                        return;
                    }

                    if (now_goods_count > max_goods_count) {
                        Toast.makeText(context, "一单最多选择10件商品", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    register();

                    Intent intent = new Intent(context, ConfirmOrderActivity.class);
                    intent.putExtra("shopId", String.valueOf(storeId));
                    intent.putExtra("orderType", 1);

                    startActivity(intent);
                    break;

                case R.id.bg_layout:
                    cardLayout.setVisibility(View.GONE);
                    bg_layout.setVisibility(View.GONE);
                    cardShopLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };


/////////////////////////////////////////////////////////////////

    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private FrameLayout createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;

    }

    private void doAnim(Drawable drawable, int[] start_location) {
        if (!isClean) {
            setAnim(drawable, start_location);
        } else {
            try {
                animation_viewGroup.removeAllViews();
                isClean = false;
                setAnim(drawable, start_location);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isClean = true;
            }
        }
    }

    /**
     * 动画效果设置
     *
     * @param drawable       将要加入购物车的商品
     * @param start_location 起始位置
     */
    @SuppressLint("NewApi")
    private void setAnim(Drawable drawable, int[] start_location) {
        Animation mScaleAnimation = new ScaleAnimation(1.2f, 0.6f, 1.2f, 0.6f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mScaleAnimation.setFillAfter(true);

        final ImageView iview = new ImageView(context);
        iview.setImageDrawable(drawable);
        final View view = addViewToAnimLayout(animation_viewGroup, iview,
                start_location);

        view.setAlpha(0.6f);

        int[] end_location = new int[2];
        settlement.getLocationInWindow(end_location);

        // 计算位移
        int endX = 0 - start_location[0] + 40;// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);


        Animation mRotateAnimation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF,
                0.3f);
        mRotateAnimation.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(mRotateAnimation);
        set.addAnimation(mScaleAnimation);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(500);// 动画的执行时间
        view.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                number++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                number--;
                if (number == 0) {
                    isClean = true;
                    myHandler.sendEmptyMessage(0);
                }

                ObjectAnimator.ofFloat(shopping_cart, "translationY", 0, 4, -2, 0).setDuration(400).start();
                ObjectAnimator.ofFloat(shoppingNum, "translationY", 0, 4, -2, 0).setDuration(400).start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });

    }

    /**
     * @param vg       动画运行的层 这里是frameLayout
     * @param view     要运行动画的View
     * @param location 动画的起始位置
     * @return
     * @deprecated 将要执行动画的view 添加到动画层
     */
    private View addViewToAnimLayout(ViewGroup vg, View view, int[] location) {
        int x = location[0];
        int y = location[1];
        vg.addView(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setPadding(5, 5, 5, 5);
        view.setLayoutParams(lp);

        return view;

    }

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 用来清除动画后留下的垃圾
            try {
                animation_viewGroup.removeAllViews();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isClean = false;
        }
    };

    /**
     * 内存过低时及时处理动画产生的未处理冗余
     */
    @Override
    public void onLowMemory() {
        isClean = true;
        try {
            animation_viewGroup.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isClean = false;
        super.onLowMemory();
    }


    @Override
    public void onDestroy() {
        unregister();
        super.onDestroy();
    }


    private void clearData() {

        for (int i = 0, count = shopAdapter.getCount(); i < count; i++) {
            GoodsListEntityBean.StoreGoodsListBean pro = (GoodsListEntityBean.StoreGoodsListBean) shopAdapter.getItem(i);

            pro.setNumber(0);
        }
        shopAdapter.clearDatas();

        adapter.notifyDataSetChanged();

        shoppingNum.setVisibility(View.GONE);
        shoppingPrise.setVisibility(View.GONE);

        shoppingPrise.setText("¥ 0");

        shoppingToast.setText("另需配送费");
        shoppingToast.append("0");
        shoppingToast.append("元");

        shoppingNum.setText(String.valueOf(0));
    }

    public class PayReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String action = intent.getAction();
            if (ConstantData.WXPAY_RESULT.equals(action)) {
                clearData();
                unregister();
            }
        }
    }

    PayReceiver payReceiver;

    private void register() {
        payReceiver = new PayReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantData.WXPAY_RESULT);
        registerReceiver(payReceiver, intentFilter);
    }

    private void unregister() {
        if (null != payReceiver) {
            try {
                unregisterReceiver(payReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}



