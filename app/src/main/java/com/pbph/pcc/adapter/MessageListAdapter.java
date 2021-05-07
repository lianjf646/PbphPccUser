package com.pbph.pcc.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pbph.pcc.R;
import com.pbph.pcc.activity.MessageListActivity;
import com.pbph.pcc.bean.response.GetMessageWebResponseBean;

import java.util.List;


public class MessageListAdapter extends BaseAdapter {
    private Context context = null;
    private List<GetMessageWebResponseBean.DataBean> list = null;
    private LayoutInflater inflater = null;

    public MessageListAdapter(Context context, List<GetMessageWebResponseBean.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = inflater.inflate(R.layout.adapter_message_list_item,
                    null);
        }
        final GetMessageWebResponseBean.DataBean bean = list.get(i);
        TextView time = view.findViewById(R.id.tv_adapter_message_list_time);
        TextView title = view.findViewById(R.id.tv_adapter_message_list_title);
        TextView desc = view.findViewById(R.id.tv_adapter_message_list_content);
        TextView refuse = view.findViewById(R.id.tv_adapter_message_list_refuse_order);
        TextView accept = view.findViewById(R.id.tv_adapter_message_list_accept_order);
        LinearLayout layout = view.findViewById(R.id.ll_adapter_message_list_order);
        LinearLayout contentLayout = view.findViewById(R.id.ll_adapter_message_list_content);
        if ("4".equals(bean.getMessageType())) {
            if ("2".equals(bean.getMessageIsRead())) {
                layout.setVisibility(View.GONE);
            } else {
                layout.setVisibility(View.VISIBLE);
            }
        } else {
            layout.setVisibility(View.GONE);
        }
        title.setText(bean.getTitle());
        time.setText(bean.getMessageCreateTime());
        desc.setText(bean.getMsg_content());
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus("0", bean.getRecordId(), bean.getMessageId());
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus("1", bean.getRecordId(), bean.getMessageId());
            }
        });
        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(bean.getOrderId())) {
                    openOrder(bean.getOrderId(), bean.getOrderType(), bean.getOrderStatus());
                }
            }
        });
        return view;
    }

    private void openOrder(String orderid, String orderType, String status) {
        MessageListActivity activity = (MessageListActivity) context;
        activity.openOrderDetail(orderid, orderType, status);
    }

    private void updateStatus(String status, String recordId, String messageId) {
        MessageListActivity activity = (MessageListActivity) context;
        activity.updateStatusWeb(status, recordId, messageId);
    }

    private static class ViewHolder {

        public static View get(View view, int id) {

            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return childView;
        }
    }
}
