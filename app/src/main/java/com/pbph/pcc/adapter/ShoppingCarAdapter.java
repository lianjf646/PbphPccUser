package com.pbph.pcc.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pbph.pcc.R;
import com.pbph.pcc.activity.ConfirmOrderActivity;
import com.pbph.pcc.activity.ReleaseOrderActivity;
import com.pbph.pcc.activity.RestaurantGoodsActivity;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.dao.ShoppingCarBean;
import com.pbph.pcc.db.DaoSession;
import com.pbph.pcc.db.ShoppingCarCDao;
import com.pbph.pcc.db.ShoppingCarPDao;
import com.pbph.pcc.tools.MoneyUtils;
import com.pbph.pcc.view.GlideCircleTransform;
import com.pbph.pcc.view.RxDialogSureCancel;
import com.pbph.pcc.view.ShoppingCarDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class ShoppingCarAdapter extends BaseExpandableListAdapter {
    List<ShoppingCarBean> shoppingCarList = new ArrayList<ShoppingCarBean>();
    Context context;
    ShoppingCarPDao shoppingCarPDao;
    ShoppingCarCDao shoppingCarCDao;
    ChildViewHolder childViewHolder = null;
    GroupViewHolder groupViewHolder = null;
    private OnDeleteRefreshListener deleteRefreshListener = null;

    public ShoppingCarAdapter(List<ShoppingCarBean> shoppingCarList, Context context) {
        super();
        this.context = context;
        this.shoppingCarList = shoppingCarList;
        DaoSession daoSession = PccApplication.getDaoSession();
        shoppingCarPDao = daoSession.getShoppingCarPDao();
        shoppingCarCDao = daoSession.getShoppingCarCDao();
    }

    public void setDeleteRefreshListener(OnDeleteRefreshListener deleteRefreshListener) {
        this.deleteRefreshListener = deleteRefreshListener;
    }

    public void onDeleteRefresh() {
        if (deleteRefreshListener != null) {
            deleteRefreshListener.onDeleteRefresh();
        }
    }

    public interface OnDeleteRefreshListener {
        public void onDeleteRefresh();
    }

    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        return shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos);
    }

    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return shoppingCarList.size();
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        return shoppingCarList.get(parentPos).getShoppingCarCs().size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        return shoppingCarList.get(parentPos);
    }

    //  获得某个父项的id
    @Override
    public long getGroupId(int parentPos) {
        return parentPos;
    }

    //  获得某个父项的某个子项的id
    @Override
    public long getChildId(int parentPos, int childPos) {
        return childPos;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //  获得父项显示的view
    @Override
    public View getGroupView(final int parentPos, boolean b, View convertView, ViewGroup viewGroup) {
        groupViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shopping_parent_item, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.nameP = convertView
                    .findViewById(R.id.parent_title);
            groupViewHolder.deleteP = convertView
                    .findViewById(R.id.delete);
            groupViewHolder.checkP = convertView
                    .findViewById(R.id.check);
            groupViewHolder.imgP = convertView.findViewById(R.id.img);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.checkP.setChecked(shoppingCarList.get(parentPos).getShoppingCarP().getShopIsChecked());
        groupViewHolder.checkP.setTag(parentPos);
        groupViewHolder.checkP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = !shoppingCarList.get(parentPos).getShoppingCarP().getShopIsChecked();
                if (b) {
                    for (int i = 0; i < shoppingCarList.get(parentPos).getShoppingCarCs().size(); i++) {
                        shoppingCarList.get(parentPos).getShoppingCarCs().get(i).setGoodIsChecked(true);
                        if (shoppingCarList.get(parentPos).getShoppingCarCs().get(i).getGoodNum() == 0) {
                            shoppingCarList.get(parentPos).getShoppingCarCs().get(i).setGoodNum(1);
                        }
                    }
                } else {
                    for (int i = 0; i < getChildrenCount(parentPos); i++) {
                        shoppingCarList.get(parentPos).getShoppingCarCs().get(i).setGoodIsChecked(false);
                        shoppingCarList.get(parentPos).getShoppingCarCs().get(i).setGoodNum(0);
                    }
                }
                shoppingCarList.get(parentPos).getShoppingCarP().setShopIsChecked(b);
                Double price = changeTotlePrice(parentPos);
                shoppingCarList.get(parentPos).getShoppingCarP().setTotlePrice(price);
                Double shippingAmount = 0.0;
                for (int i = 0; i < shoppingCarList.get(parentPos).getShoppingCarCs().size(); i++) {
                    shippingAmount += MoneyUtils.scaleCost(shoppingCarList.get(parentPos).getShoppingCarCs().get(i).getGoodNum()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeSoodsPrice()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeStoreNum()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeSoodsPrices());
                }
                shoppingCarList.get(parentPos).getShoppingCarP().setShippingAmount(shippingAmount);
                notifyDataSetChanged();
            }
        });
        groupViewHolder.deleteP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RxDialogSureCancel rxDialogSureCancel = ShoppingCarDialog.getDialog(context, "是否删除该商品？");
                rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shoppingCarPDao.delete(shoppingCarList.get(parentPos).getShoppingCarP());
                        for (int i = 0; i < shoppingCarList.get(parentPos).getShoppingCarCs().size(); i++) {
                            shoppingCarCDao.delete(shoppingCarList.get(parentPos).getShoppingCarCs().get(i));
                        }
                        shoppingCarList.remove(parentPos);
                        if (shoppingCarList.size() == 0) {
                            onDeleteRefresh();
                        }
                        notifyDataSetChanged();
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.show();
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int storeId = Integer.parseInt(shoppingCarList.get(parentPos).getShoppingCarP().getShopId());
                if (storeId == -1) {
                    Intent intent = new Intent(context, ReleaseOrderActivity.class);
                    intent.putExtra("position", 3);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, RestaurantGoodsActivity.class);
                    intent.putExtra("storeId", storeId);
                    context.startActivity(intent);
                }
            }
        });
        int storeId = Integer.parseInt(shoppingCarList.get(parentPos).getShoppingCarP().getShopId());
        if (storeId == -1) {
            Glide.with(context)
                    .load(R.drawable.release_order_tab_icon_2_selected).transform(new GlideCircleTransform(context)).placeholder(R.mipmap.banner_zw).into(groupViewHolder.imgP);
        } else {
            Glide.with(context)
                    .load(shoppingCarList.get(parentPos).getShoppingCarP().getShopImgUrl()).transform(new GlideCircleTransform(context)).placeholder(R.mipmap.banner_zw).into(groupViewHolder.imgP);
        }
        groupViewHolder.nameP.setText(shoppingCarList.get(parentPos).getShoppingCarP().getShopName());
        convertView.setTag(groupViewHolder);
        return convertView;
    }

    //  获得子项显示的view
    @Override
    public View getChildView(final int parentPos, final int childPos, boolean b, View convertView, ViewGroup viewGroup) {
        childViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shopping_child_item, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.nameC = convertView
                    .findViewById(R.id.child_title);
            childViewHolder.priceC = convertView
                    .findViewById(R.id.child_price);
            childViewHolder.numC = convertView
                    .findViewById(R.id.num);
            childViewHolder.priceTotleC = convertView
                    .findViewById(R.id.price_totle);
            childViewHolder.settlement = convertView
                    .findViewById(R.id.settlement);
            childViewHolder.shippingAmount = convertView
                    .findViewById(R.id.shipping_amount);
            childViewHolder.checkC = convertView
                    .findViewById(R.id.check);
            childViewHolder.reduce = convertView
                    .findViewById(R.id.reduce);
            childViewHolder.plus = convertView
                    .findViewById(R.id.plus);
            childViewHolder.imgC = convertView
                    .findViewById(R.id.img);
            childViewHolder.settlementLayP = convertView.findViewById(R.id.settlement_lay);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.priceC.setText("￥" + shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).getGoodPrice());
        childViewHolder.priceC.setTag(shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).getGoodPrice());
        int n = shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).getGoodNum();
        childViewHolder.numC.setText("" + n);
        if (n == 0) {
            shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).setGoodIsChecked(false);
        }
        if (childPos == shoppingCarList.get(parentPos).getShoppingCarCs().size() - 1) {
            childViewHolder.settlementLayP.setVisibility(View.VISIBLE);
            DecimalFormat df = new DecimalFormat("#0.00");
            childViewHolder.priceTotleC.setText("￥" + df.format(shoppingCarList.get(parentPos).getShoppingCarP().getTotlePrice()));
            childViewHolder.shippingAmount.setText("另需" + df.format(shoppingCarList.get(parentPos).getShoppingCarP().getShippingAmount()) + "元配送费");
            childViewHolder.settlement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int totNum = 0;
                    for (int i = 0; i < shoppingCarList.get(parentPos).getShoppingCarCs().size(); i++) {
                        totNum += shoppingCarList.get(parentPos).getShoppingCarCs().get(i).getGoodNum();
                    }
                    if (totNum > 10) {
                        Toast.makeText(context, "每单最多只能添加10件商品", Toast.LENGTH_LONG).show();
                    } else if (totNum < 1) {
                        Toast.makeText(context, "请选择要购买的商品", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(context, ConfirmOrderActivity.class);
                        intent.putExtra("shopId", shoppingCarList.get(parentPos).getShoppingCarP().getShopId());
                        String storeId = shoppingCarList.get(parentPos).getShoppingCarP().getShopId();
                        int orderType = 1;
                        if ("-1".equals(storeId)) {
                            orderType = 2;
                        }
                        intent.putExtra("orderType", orderType);
                        context.startActivity(intent);
                    }
                }
            });
        } else {
            childViewHolder.settlementLayP.setVisibility(View.GONE);
        }
        childViewHolder.checkC.setChecked(shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).getGoodIsChecked());
        Glide.with(context)
                .load(shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).getGoodImgUrl()).placeholder(R.mipmap.banner_zw).into(childViewHolder.imgC);
        childViewHolder.nameC.setText(shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).getGoodName());
        childViewHolder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).getGoodNum();
                if (n == 1) {
                    final RxDialogSureCancel rxDialogSureCancel = ShoppingCarDialog.getDialog(context, "是否删除该商品？");
                    rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rxDialogSureCancel.cancel();
                        }
                    });
                    rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            shoppingCarCDao.delete(shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos));
                            shoppingCarList.get(parentPos).getShoppingCarCs().remove(childPos);
                            if (shoppingCarList.get(parentPos).getShoppingCarCs().size() == 0) {
                                shoppingCarPDao.delete(shoppingCarList.get(parentPos).getShoppingCarP());
                                shoppingCarList.remove(parentPos);
                            }
                            if (shoppingCarList.size() == 0) {
                                onDeleteRefresh();
                            }
                            notifyDataSetChanged();
                            rxDialogSureCancel.cancel();
                        }
                    });
                    rxDialogSureCancel.show();
                    return;
                }
                if (n > 1) {
                    n--;
                }
                if (n == 0) {
                    shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).setGoodIsChecked(false);
                }
                shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).setGoodNum(n);
                Double price = changeTotlePrice(parentPos);
                shoppingCarList.get(parentPos).getShoppingCarP().setTotlePrice(price);
                Double shippingAmount = 0.0;
                for (int i = 0; i < shoppingCarList.get(parentPos).getShoppingCarCs().size(); i++) {
                    shippingAmount += MoneyUtils.scaleCost(shoppingCarList.get(parentPos).getShoppingCarCs().get(i).getGoodNum()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeSoodsPrice()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeStoreNum()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeSoodsPrices());
                }
                shoppingCarList.get(parentPos).getShoppingCarP().setShippingAmount(shippingAmount);
                notifyDataSetChanged();
            }
        });
        childViewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totNum = 0;
                for (int i = 0; i < shoppingCarList.get(parentPos).getShoppingCarCs().size(); i++) {
                    totNum += shoppingCarList.get(parentPos).getShoppingCarCs().get(i).getGoodNum();
                }
                if (totNum >= 10) {
                    Toast.makeText(context, "每单最多只能添加10件商品", Toast.LENGTH_LONG).show();
                    return;
                }
                int n = shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).getGoodNum();
                n++;
                shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).setGoodNum(n);
                shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).setGoodIsChecked(true);
                Double price = changeTotlePrice(parentPos);
                shoppingCarList.get(parentPos).getShoppingCarP().setTotlePrice(price);
                Double shippingAmount = 0.0;
                for (int i = 0; i < shoppingCarList.get(parentPos).getShoppingCarCs().size(); i++) {
                    shippingAmount += MoneyUtils.scaleCost(shoppingCarList.get(parentPos).getShoppingCarCs().get(i).getGoodNum()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeSoodsPrice()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeStoreNum()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeSoodsPrices());
                }
                shoppingCarList.get(parentPos).getShoppingCarP().setShippingAmount(shippingAmount);
                totNum++;
                notifyDataSetChanged();
            }
        });
        childViewHolder.checkC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n;
                if (shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).getGoodIsChecked()) {
                    n = 0;
                    shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).setGoodNum(n);
                    shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).setGoodIsChecked(false);
                } else {
                    n = 1;
                    shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).setGoodNum(n);
                    shoppingCarList.get(parentPos).getShoppingCarCs().get(childPos).setGoodIsChecked(true);
                }
                Double price = changeTotlePrice(parentPos);
                shoppingCarList.get(parentPos).getShoppingCarP().setTotlePrice(price);
                Double shippingAmount = 0.0;
                for (int i = 0; i < shoppingCarList.get(parentPos).getShoppingCarCs().size(); i++) {
                    shippingAmount += MoneyUtils.scaleCost(shoppingCarList.get(parentPos).getShoppingCarCs().get(i).getGoodNum()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeSoodsPrice()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeStoreNum()
                            , shoppingCarList.get(parentPos).getShoppingCarP().getTakeSoodsPrices());
                }
                shoppingCarList.get(parentPos).getShoppingCarP().setShippingAmount(shippingAmount);
                notifyDataSetChanged();
            }
        });
        convertView.setTag(childViewHolder);
        return convertView;
    }

    public Double changeTotlePrice(int i) {
        Double price = 0.0;
        int num = 0;
        DecimalFormat df = new DecimalFormat("#0.00");
        for (int j = 0; j < shoppingCarList.get(i).getShoppingCarCs().size(); j++) {
            if (shoppingCarList.get(i).getShoppingCarCs().get(j).getGoodIsChecked()) {
                num += shoppingCarList.get(i).getShoppingCarCs().get(j).getGoodNum();
                price += shoppingCarList.get(i).getShoppingCarCs().get(j).getGoodPrice() * shoppingCarList.get(i).getShoppingCarCs().get(j).getGoodNum();
            }
        }
        return price;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    class GroupViewHolder {
        TextView nameP, deleteP;
        CheckBox checkP;
        ImageView imgP;
    }

    class ChildViewHolder {
        TextView nameC;
        TextView priceC;
        TextView numC, priceTotleC, settlement, shippingAmount;
        ImageView plus;
        ImageView reduce;
        ImageView imgC;
        CheckBox checkC;
        ConstraintLayout settlementLayP;
    }
}
