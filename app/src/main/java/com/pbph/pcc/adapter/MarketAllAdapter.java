package com.pbph.pcc.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ui.PinnedHeaderAdapter;
import com.bumptech.glide.Glide;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.GetUserMarketGoodsBean.DataBean.MarkeGoodsListEntityBean;

import java.util.List;

public class MarketAllAdapter extends PinnedHeaderAdapter {

    List<MarkeGoodsListEntityBean> pruductCagests;
    private HolderClickListener mHolderClickListener;
    private Context context;
    private LayoutInflater mInflater;


    private OnCallBackListener callBackListener;

    public void setCallBackListener(OnCallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }


    public MarketAllAdapter(Context context, List<MarkeGoodsListEntityBean> pruductCagests) {
        this.context = context;
        this.pruductCagests = pruductCagests;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int section, int position) {
        return pruductCagests.get(section).getMarketGoodsList().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return pruductCagests.size();
    }

    @Override
    public int getCountForSection(int section) {
        return pruductCagests.get(section).getMarketGoodsList().size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_all, null);
            viewHolder = new ViewHolder();
            viewHolder.head = convertView.findViewById(R.id.head);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.desc = convertView.findViewById(R.id.desc);

            viewHolder.sell = convertView.findViewById(R.id.sell);


            viewHolder.prise = convertView.findViewById(R.id.prise);
            viewHolder.increase = convertView.findViewById(R.id.increase);
            viewHolder.reduce = convertView.findViewById(R.id.reduce);
            viewHolder.shoppingNum = convertView.findViewById(R.id.shoppingNum);
            /////////////
            viewHolder.onIncreaseListener = new OnIncreaseListener();
            viewHolder.increase.setOnClickListener(viewHolder.onIncreaseListener);
            viewHolder.onReduceListener = new OnReduceListener();
            viewHolder.reduce.setOnClickListener(viewHolder.onReduceListener);
/////////
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MarkeGoodsListEntityBean.MarketGoodsListBean vo = pruductCagests.get(section).getMarketGoodsList().get(position);

        //////////////
        viewHolder.onIncreaseListener.viewHolder = viewHolder;
        viewHolder.onIncreaseListener.vo = vo;

        viewHolder.onReduceListener.viewHolder = viewHolder;
        viewHolder.onReduceListener.vo = vo;
/////////////

        Glide.with(context).load(vo.getMarketGoodsImg()).placeholder(R.mipmap.banner_zw).error(R.mipmap.banner_zw).into(viewHolder.head);

        viewHolder.name.setText(vo.getMarketGoodsName());
        viewHolder.desc.setText(vo.getMarketGoodsDescribe());

        viewHolder.sell.setText(String.valueOf(vo.getSales()));

        viewHolder.prise.setText(String.valueOf(vo.getMarketGoodsPrice()));
        viewHolder.shoppingNum.setText(String.valueOf(vo.getNumber()));


        return convertView;
    }

    class ViewHolder {
        /**
         * 商品图片
         */
        public ImageView head;
        /**
         * 商品名称
         */
        public TextView name;

        public TextView desc;

        public TextView sell;
        /**
         * 商品价格
         */
        public TextView prise;
        /**
         * 增加
         */
        public TextView increase;
        OnIncreaseListener onIncreaseListener;
        /**
         * 商品数目
         */
        public TextView shoppingNum;
        /**
         * 减少
         */
        public TextView reduce;
        OnReduceListener onReduceListener;
    }


    class OnIncreaseListener implements View.OnClickListener {

        public ViewHolder viewHolder;

        public MarkeGoodsListEntityBean.MarketGoodsListBean vo;

        @Override
        public void onClick(View view) {
            int num = vo.getNumber();
            num++;
            vo.setNumber(num);
            viewHolder.shoppingNum.setText(vo.getNumber() + "");
            if (callBackListener != null) {
                callBackListener.updateProduct(vo, "1");
            } else {
            }
            if (mHolderClickListener != null) {
                int[] start_location = new int[2];
                viewHolder.shoppingNum.getLocationInWindow(start_location);//获取点击商品图片的位置
                Drawable drawable = context.getResources().getDrawable(R.drawable.adddetail);//复制一个新的商品图标
                //TODO:解决方案，先监听到左边ListView的Item中，然后在开始动画添加
                mHolderClickListener.onHolderClick(drawable, start_location);
            }
        }
    }

    class OnReduceListener implements View.OnClickListener {

        public ViewHolder viewHolder;

        public MarkeGoodsListEntityBean.MarketGoodsListBean vo;

        @Override
        public void onClick(View view) {
            int num = vo.getNumber();
            if (num > 0) {
                num--;
                vo.setNumber(num);
                viewHolder.shoppingNum.setText(vo.getNumber() + "");
                if (callBackListener != null) {
                    callBackListener.updateProduct(vo, "2");
                } else {
                }
            }
        }
    }

    public void SetOnSetHolderClickListener(HolderClickListener holderClickListener) {
        this.mHolderClickListener = holderClickListener;
    }

    public interface HolderClickListener {
        public void onHolderClick(Drawable drawable, int[] start_location);
    }


    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.listview_head, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText(pruductCagests.get(section).getTypeName());
        return layout;
    }

    public interface OnCallBackListener {
        /**
         * Type表示添加或减少
         *
         * @param product
         * @param type
         */
        void updateProduct(MarkeGoodsListEntityBean.MarketGoodsListBean product, String type);
    }
}
