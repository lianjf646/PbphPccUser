package com.pbph.pcc.viewholder;

import android.view.View;
import android.widget.TextView;

import com.android.adapter.ViewHolder;
import com.pbph.pcc.R;


public class SelectCity3ViewHolder extends ViewHolder {

    public TextView sep;

    @Override
    protected int getLayout() {
        return R.layout.listview_group_item;
    }

    @Override
    protected void getView(View view) {
        sep = view.findViewById(R.id.textViewitem);
        sep.setTextColor(adapter.context.getResources().getColor(R.color.sbc_header_text));
    }

    @Override
    protected void showView() {
        String vo = (String) item;
        sep.setText(vo);
    }
}
