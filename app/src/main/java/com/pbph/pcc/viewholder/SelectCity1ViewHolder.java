package com.pbph.pcc.viewholder;

import android.view.View;
import android.widget.TextView;

import com.android.adapter.ViewHolder;
import com.pbph.pcc.R;


public class SelectCity1ViewHolder extends ViewHolder {

    public TextView sep;

    @Override
    protected int getLayout() {
        return R.layout.listview_group_sep;
    }

    @Override
    protected void getView(View view) {
        sep = view.findViewById(R.id.textViewsep);
    }

    @Override
    protected void showView() {

        String vo = (String) item;
        sep.setText(vo);
    }
}
