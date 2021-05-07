package com.pbph.pcc.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.OnSingleClickListener;
import com.android.ui.PinnedHeaderListView;
import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.activity.ConfirmOrderActivity;
import com.pbph.pcc.adapter.MarketAllAdapter;
import com.pbph.pcc.adapter.MarketTypeAdapter;
import com.pbph.pcc.adapter.MyShopCarAdapter;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.GetUserMarketGoodsBean;
import com.pbph.pcc.bean.response.GetUserMarketGoodsBean.DataBean.MarkeGoodsListEntityBean;
import com.pbph.pcc.bean.response.GetUserSupermarketInfoBean;
import com.pbph.pcc.db.ShoppingCarC;
import com.pbph.pcc.db.ShoppingCarCDao;
import com.pbph.pcc.db.ShoppingCarP;
import com.pbph.pcc.db.ShoppingCarPDao;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.MoneyUtils;
import com.pbph.pcc.viewholder.MyShopCarMarketViewHolder;
import com.utils.DoubleUtil;
import com.utils.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MarketOrderFragment extends Fragment implements MarketAllAdapter.OnCallBackListener, MyShopCarAdapter.OnFlushDataListener {


    private final int max_goods_count = 10;
//    private final int max_goods_count_temp = 10;

    private volatile int now_goods_count = 0;

    int cityId = 1;

    String schoolId;
    String userId;

    int goodsId = -1;


    int takeStoreNum = 0;
    double takeSoodsPrice = 0;
    double takeSoodsPrices = 0;

    //所有商品信息
    public List<MarkeGoodsListEntityBean> productCategorizes = new ArrayList<>();

    /////////////////
//主要的两个 listview左侧右侧
    private ListView typeListView;
    private MarketTypeAdapter typeAdapter;
    private PinnedHeaderListView allListView;
    private MarketAllAdapter allAdapter;

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

    private View include_cannot;

    ///////////////
    private boolean isScroll = true;
    ////////////
// 正在执行的动画数量
    private int number = 0;
    // 是否完成清理
    private boolean isClean = false;
    private FrameLayout animation_viewGroup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Object obj = PccApplication.getDataMapData(MarketOrderFragment.class.toString());
        if (obj != null) {
            try {
                goodsId = (int) obj;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        PccApplication application = (PccApplication) getActivity().getApplication();

        schoolId = application.getMyInfoData().getSchoolId();
        userId = PccApplication.getUserid();
        cityId = application.getCityId();

        animation_viewGroup = createAnimLayout();

        now_goods_count = 0;

        takeStoreNum = 0;
        takeSoodsPrice = 0;
        takeSoodsPrices = 0;

        View root = inflater.inflate(R.layout.fragment_market_order, container, false);

        typeListView = root.findViewById(R.id.classify_mainlist);
        allListView = root.findViewById(R.id.classify_morelist);

        shopping_cart = root.findViewById(R.id.shopping_cart);
        shoppingPrise = root.findViewById(R.id.shoppingPrise);
        shoppingToast = root.findViewById(R.id.shoppingToast);

        shoppingNum = root.findViewById(R.id.shoppingNum);
        settlement = root.findViewById(R.id.settlement);


        cardLayout = root.findViewById(R.id.layout_settle_account_list);

        bg_layout = root.findViewById(R.id.bg_layout);
        cardShopLayout = root.findViewById(R.id.cardShopLayout);
        shopListView = root.findViewById(R.id.shopproductListView);
        defaultText = root.findViewById(R.id.defaultText);
        shopListView.setEmptyView(defaultText);

        include_cannot = root.findViewById(R.id.include_cannot);
        include_cannot.setVisibility(View.VISIBLE);
        include_cannot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        initData();
        getUserSupermarketInfo();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        flushDatas();
    }

    @Override
    public void onDestroyView() {
        HttpAction.getInstance().getUserMarketGoodsCancel();
        HttpAction.getInstance().getUserSupermarketInfoCancel();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        unregister();
        super.onDestroy();
    }


    private void getUserSupermarketInfo() {
        HttpAction.getInstance().getUserSupermarketInfo(cityId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {

                    GetUserSupermarketInfoBean vo = JsonMananger.jsonToBean(response.body(), GetUserSupermarketInfoBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(MarketOrderFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEqual(vo.getData().getSupermarketEntity().getSupStatus(), "0")) {
                        include_cannot.setVisibility(View.VISIBLE);
                        return;
                    }
                    include_cannot.setVisibility(View.GONE);

                    takeStoreNum = Integer.parseInt(vo.getData().getSupermarketEntity().getSupSoodsNum());
                    takeSoodsPrice = Double.parseDouble(vo.getData().getSupermarketEntity().getSupSoodsPrice());
                    takeSoodsPrices = Double.parseDouble(vo.getData().getSupermarketEntity().getSupSoodsPrices());
                    getUserMarketGoods();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MarketOrderFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getUserMarketGoods() {
        productCategorizes.clear();
        shopAdapter.clearDatas();
        allAdapter.notifyDataSetChanged();
        typeAdapter.notifyDataSetChanged();

        HttpAction.getInstance().getUserMarketGoods(cityId, "", new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {

                    GetUserMarketGoodsBean vo = JsonMananger.jsonToBean(response.body(), GetUserMarketGoodsBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(MarketOrderFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<MarkeGoodsListEntityBean> datas = vo.getData().getMarkeGoodsListEntity();
                    if (datas == null || datas.size() <= 0) {
//                        tvLoading.setText("暂无数据");
                        return;
                    }

                    productCategorizes.clear();
                    productCategorizes.addAll(datas);

                    flushDatas();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MarketOrderFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void flushDatas() {

        now_goods_count = 0;
        shopAdapter.clearDatas();
        clearData();

        if (productCategorizes == null || productCategorizes.size() <= 0) {
            allAdapter.notifyDataSetChanged();
            typeAdapter.notifyDataSetChanged();
            shopAdapter.notifyDataSetChanged();
            return;
        }


        int finalPos = 0;
        Map<Integer, ShoppingCarC> map = getDaoGoodRecords();

        if (goodsId >= 0 || (map != null && map.size() > 0)) {

            int pos = -1;
            for (int i = 0, counti = productCategorizes.size(); i < counti; i++) {

                pos += 1;
                List<MarkeGoodsListEntityBean.MarketGoodsListBean> list = productCategorizes.get(i).getMarketGoodsList();
                if (list == null || list.size() <= 0) continue;

                for (int j = 0, countj = list.size(); j < countj; j++) {

                    pos += 1;
                    MarkeGoodsListEntityBean.MarketGoodsListBean gvo = list.get(j);

                    if (map != null && map.size() > 0) {
                        ShoppingCarC scc = map.get(gvo.getMarketGoodsId());
                        if (scc != null) {
                            vo2Bean(scc, gvo);
                            shopAdapter.addData(gvo);
                        }
                    }

                    if (gvo.getMarketGoodsId() == goodsId) {
                        finalPos = pos;
                    }
                }
            }

            insertStore();
            setPrise();
        }

        allAdapter.notifyDataSetChanged();
        typeAdapter.notifyDataSetChanged();
        shopAdapter.notifyDataSetChanged();

        if (goodsId == -1) return;
        goodsId = -1;
        try {
//                        这里-1 仅仅是为了让他不在最顶头
            allListView.setSelection(finalPos - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initData() {

        productCategorizes.clear();

        typeAdapter = new MarketTypeAdapter(getContext(), productCategorizes);
        typeListView.setAdapter(typeAdapter);

        allAdapter = new MarketAllAdapter(getContext(), productCategorizes);

        allAdapter.SetOnSetHolderClickListener(new MarketAllAdapter.HolderClickListener() {

            @Override
            public void onHolderClick(Drawable drawable, int[] start_location) {

                if (beyond) return;

                doAnim(drawable, start_location);
            }

        });
        allListView.setAdapter(allAdapter);
        allAdapter.setCallBackListener(this);


        shopAdapter = new MyShopCarAdapter(this, shopListView, MyShopCarMarketViewHolder.class);
        shopAdapter.setOnFlushDataListener(this);
        shopListView.setAdapter(shopAdapter);

        typeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                isScroll = false;

                for (int i = 0; i < typeListView.getChildCount(); i++) {

                    TextView item = (TextView) typeListView.getChildAt(i);
                    item.setBackgroundColor(i == position ?
                            Color.rgb(255, 255, 255) : Color.TRANSPARENT);
                    item.setTextColor(getResources().getColor(i == position ? R.color.text_black_color : R.color.black_gray));

                    item.getPaint().setFakeBoldText(i == position);

                }

                int rightSection = 0;
                for (int i = 0; i < position; i++) {
                    rightSection += allAdapter.getCountForSection(i) + 1;
                }
                allListView.setSelection(rightSection);

            }

        });

        allListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    for (int i = 0; i < typeListView.getChildCount(); i++) {

                        int position = allAdapter.getSectionForPosition(firstVisibleItem);

                        TextView item = (TextView) typeListView.getChildAt(i);
                        item.setBackgroundColor(i == position ?
                                Color.rgb(255, 255, 255) : Color.TRANSPARENT);
                        item.setTextColor(getResources().getColor(i == position ? R.color.text_black_color : R.color.black_gray));

                        item.getPaint().setFakeBoldText(i == position);
                    }
                } else {
                    isScroll = true;
                }
            }
        });


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
    public void updateProduct(MarkeGoodsListEntityBean.MarketGoodsListBean product, String type) {
        if (type.equals("1")) {

            boolean b = scaleMax(product, true);
            if (b) {
                if (!shopAdapter.contains(product)) {
                    shopAdapter.addData(product);
                }
            } else {
                allAdapter.notifyDataSetChanged();
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
                allAdapter.notifyDataSetChanged();
            }
        }
        shopAdapter.notifyDataSetChanged();

        updateGood(product);
        setPrise();
    }


    @Override
    public void onFlushData(Object obj, boolean type) {
        MarkeGoodsListEntityBean.MarketGoodsListBean product = (MarkeGoodsListEntityBean.MarketGoodsListBean) obj;
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
        allAdapter.notifyDataSetChanged();

        updateGood(product);
        setPrise();
    }


    private boolean beyond = false;

    private boolean scaleMax(MarkeGoodsListEntityBean.MarketGoodsListBean product, boolean add) {
        if (add) {
            beyond = false;
            now_goods_count++;
            if (now_goods_count <= max_goods_count) return true;

            beyond = true;
            now_goods_count--;
            product.setNumber(product.getNumber() - 1);

            Toast.makeText(getContext(), "一单最多选择10件商品", Toast.LENGTH_SHORT).show();
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
        double cost = 0;
        int shopNum = 0;

        for (int i = 0, c = shopAdapter.getCount(); i < c; i++) {
            MarkeGoodsListEntityBean.MarketGoodsListBean pro = (MarkeGoodsListEntityBean.MarketGoodsListBean) shopAdapter.getItem(i);
            double tempMoney = MoneyUtils.scaleMoney(pro.getMarketGoodsPrice(), pro.getNumber());
            double tempCost = MoneyUtils.scaleCost(pro.getNumber(), takeSoodsPrice, takeStoreNum, takeSoodsPrices);

            money = DoubleUtil.sum(money, tempMoney);
            cost = DoubleUtil.sum(cost, tempCost);

            shopNum = shopNum + pro.getNumber();
        }

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

            shoppingCarP.setId(null);
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
                        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.push_bottom_in);
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
                    if (shopAdapter == null || shopAdapter.getCount() <= 0) return;


                    if (now_goods_count > max_goods_count) {
                        Toast.makeText(getContext(), "一单最多选择10件商品", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    register();

                    Intent intent = new Intent(getContext(), ConfirmOrderActivity.class);
                    intent.putExtra("shopId", "-1");
                    intent.putExtra("orderType", 2);
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
        ViewGroup rootView = (ViewGroup) getActivity().getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(getContext());
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

        final ImageView iview = new ImageView(getContext());
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

    int storeId = -1;


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
            shoppingCarP.setShopName("超市");
            shoppingCarP.setShopImgUrl("");
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

    private void updateGood(MarkeGoodsListEntityBean.MarketGoodsListBean bean) {

        insertStore();

        ShoppingCarCDao dao = PccApplication.getDaoSession().getShoppingCarCDao();
        List<ShoppingCarC> list = dao.queryBuilder().where(
                ShoppingCarCDao.Properties.GoodId.eq(bean.getMarketGoodsId()),
                ShoppingCarCDao.Properties.UserId.eq(userId),
                ShoppingCarCDao.Properties.SchoolId.eq(schoolId)).list();
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


    private void vo2Bean(ShoppingCarC vo, MarkeGoodsListEntityBean.MarketGoodsListBean bean) {

        bean.id = vo.getId();

        bean.setNumber(vo.getGoodNum());

    }

    private ShoppingCarC bean2Vo(MarkeGoodsListEntityBean.MarketGoodsListBean bean) {
        ShoppingCarC vo = new ShoppingCarC();

        vo.setId(bean.id);

        vo.setShopId(String.valueOf(storeId));
        vo.setGoodId(String.valueOf(bean.getMarketGoodsId()));
        vo.setGoodImgUrl(bean.getMarketGoodsImg());
        vo.setGoodName(bean.getMarketGoodsName());
        vo.setGoodPrice(bean.getMarketGoodsPrice());
        vo.setGoodIsChecked(true);

        vo.setUserId(String.valueOf(userId));
        vo.setSchoolId(String.valueOf(schoolId));

        vo.setGoodNum(bean.getNumber());

        return vo;
    }

    private void clearData() {

        for (int i = 0, c = shopAdapter.getCount(); i < c; i++) {
            MarkeGoodsListEntityBean.MarketGoodsListBean pro = (MarkeGoodsListEntityBean.MarketGoodsListBean) shopAdapter.getItem(i);
            pro.setNumber(0);
        }
        shopAdapter.clearDatas();
        allAdapter.notifyDataSetChanged();

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
        getActivity().registerReceiver(payReceiver, intentFilter);
    }

    private void unregister() {
        if (null != payReceiver) {
            try {
                getActivity().unregisterReceiver(payReceiver);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

}
