package com.pbph.pcc.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.GetUserStoreGoodsBean.DataBean.GoodsListEntityBean.StoreGoodsListBean;

import java.util.ArrayList;
import java.util.List;

public class ShopSearchAllAdapter extends BaseAdapter {

    public List<StoreGoodsListBean> searchList = new ArrayList<>();
    private HolderClickListener mHolderClickListener;
    private Context context;
    private LayoutInflater mInflater;


    private OnCallBackListener callBackListener;

    public void setCallBackListener(OnCallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }


    public ShopSearchAllAdapter(Context context) {
        this.context = context;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return searchList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_all, null);
            viewHolder = new ViewHolder();
            viewHolder.head = convertView.findViewById(R.id.head);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.desc = convertView.findViewById(R.id.desc);

            viewHolder.prise = convertView.findViewById(R.id.prise);

            viewHolder.sell = convertView.findViewById(R.id.sell);

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
        final StoreGoodsListBean product = searchList.get(position);
        //////////////
        viewHolder.onIncreaseListener.viewHolder = viewHolder;
        viewHolder.onIncreaseListener.vo = product;

        viewHolder.onReduceListener.viewHolder = viewHolder;
        viewHolder.onReduceListener.vo = product;
/////////////

        Glide.with(context).load(product.getStoreGoodsImg()).placeholder(R.mipmap.banner_zw).error(R.mipmap.banner_zw).into(viewHolder.head);

        viewHolder.name.setText(product.getStoreGoodsName());
        viewHolder.desc.setText(product.getStoreGoodsDescribe());

        viewHolder.prise.setText(String.valueOf(product.getStoreGoodsPrice()));

        viewHolder.sell.setText(String.valueOf(product.getSales()));

        viewHolder.shoppingNum.setText(String.valueOf(product.getNumber()));


//        viewHolder.shoppingNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // ???????????????????????????????????????
//                } else {
//                    // ???????????????????????????????????????
//                    int shoppingNum = Integer.parseInt(viewHolder.shoppingNum.getText().toString());
//                }
//            }
//        });

        return convertView;
    }

    class ViewHolder {
        /**
         * ????????????
         */
        public ImageView head;
        /**
         * ????????????
         */
        public TextView name;

        public TextView desc;

        /**
         * ????????????
         */
        public TextView prise;

        public TextView sell;


        /**
         * ??????
         */
        public TextView increase;
        OnIncreaseListener onIncreaseListener;
        /**
         * ????????????
         */
        public TextView shoppingNum;
        /**
         * ??????
         */
        public TextView reduce;
        OnReduceListener onReduceListener;
    }


    class OnIncreaseListener implements View.OnClickListener {

        public ViewHolder viewHolder;

        public StoreGoodsListBean vo;

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
                viewHolder.shoppingNum.getLocationInWindow(start_location);//?????????????????????????????????
                Drawable drawable = context.getResources().getDrawable(R.drawable.adddetail);//??????????????????????????????
                //TODO:?????????????????????????????????ListView???Item?????????????????????????????????
                mHolderClickListener.onHolderClick(drawable, start_location);
            }
        }
    }

    class OnReduceListener implements View.OnClickListener {

        public ViewHolder viewHolder;

        public StoreGoodsListBean vo;

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
        void onHolderClick(Drawable drawable, int[] start_location);
    }


    public interface OnCallBackListener {
        /**
         * Type?????????????????????
         *
         * @param product
         * @param type
         */
        void updateProduct(StoreGoodsListBean product, String type);
    }
}
