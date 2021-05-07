package com.pbph.pcc.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.vo.OrderBean;
import com.pbph.pcc.view.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class ConfirmOrderListViewAdapter extends BaseExpandableListAdapter{
    List<OrderBean> orderBeanList = new ArrayList<OrderBean>();
    Context context;
    ChildViewHolder childViewHolder = null;
    GroupViewHolder groupViewHolder = null;
    public ConfirmOrderListViewAdapter(List<OrderBean> orderBeanList, Context context){
        super();
        this.context = context;
        this.orderBeanList = orderBeanList;
    }
    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        return orderBeanList.get(parentPos).getChildBeens().get(childPos);
    }

    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return orderBeanList.size();
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        return orderBeanList.get(parentPos).getChildBeens().size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        return orderBeanList.get(parentPos);
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

    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        return false;
    }

    //  获得父项显示的view
    @Override
    public View getGroupView(final int parentPos, boolean b, View convertView, ViewGroup viewGroup) {
        groupViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_parent_item, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.imgP = convertView.findViewById(R.id.img_p);
            groupViewHolder.nameP = convertView.findViewById(R.id.name_p);
            groupViewHolder.typeP = convertView.findViewById(R.id.type_p);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        Glide.with(context)
                .load(orderBeanList.get(parentPos).getImgUrlP()).transform(new GlideCircleTransform(context)).placeholder(R.mipmap.banner_zw).into(groupViewHolder.imgP);
        groupViewHolder.nameP.setText(orderBeanList.get(parentPos).getNameP());
        groupViewHolder.typeP.setText(orderBeanList.get(parentPos).getTypeP());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        convertView.setTag(groupViewHolder);
        return convertView;
    }

    //  获得子项显示的view
    @Override
    public View getChildView(final int parentPos, final int childPos, boolean b, View convertView, ViewGroup viewGroup) {
        childViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_child_item, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.imgC = convertView.findViewById(R.id.img_c);
            childViewHolder.nameC = convertView.findViewById(R.id.name_c);
            childViewHolder.shippingAmountC = convertView.findViewById(R.id.shipping_amount);
            childViewHolder.priceC = convertView.findViewById(R.id.price_c);
            childViewHolder.numC = convertView.findViewById(R.id.num_c);
            childViewHolder.lay = convertView.findViewById(R.id.child_lay2);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (childPos == orderBeanList.get(parentPos).getChildBeens().size() -1 ){
            childViewHolder.lay.setVisibility(View.VISIBLE);
        }else{
            childViewHolder.lay.setVisibility(View.GONE);
        }
        childViewHolder.nameC.setText(""+orderBeanList.get(parentPos).getChildBeens().get(childPos).getNameC());
        childViewHolder.numC.setText("*"+orderBeanList.get(parentPos).getChildBeens().get(childPos).getNumC());
        childViewHolder.shippingAmountC.setText("￥"+orderBeanList.get(parentPos).getShippingAmount());
        Glide.with(context)
                .load(orderBeanList.get(parentPos).getChildBeens().get(childPos).getImgUrlC())/*.transform(new GlideCircleTransform(context))*/.placeholder(R.mipmap.banner_zw).into(childViewHolder.imgC);
        childViewHolder.priceC.setText("￥"+(orderBeanList.get(parentPos).getChildBeens().get(childPos).getPriceC() * orderBeanList.get(parentPos).getChildBeens().get(childPos).getNumC()));
        convertView.setTag(childViewHolder);
        return convertView;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
    class GroupViewHolder {
        TextView nameP;
        TextView typeP;
        ImageView imgP;
    }

    class ChildViewHolder {
        TextView nameC,shippingAmountC;
        TextView priceC;
        TextView numC;
        ImageView imgC;
        ConstraintLayout lay;
    }
}
