package com.pbph.pcc.viewholder;

import android.view.View;
import android.widget.TextView;

import com.android.adapter.ViewHolder;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.SchoolBean;


public class SelectCity2ViewHolder extends ViewHolder {

    public TextView sep;

    @Override
    protected int getLayout() {
        return R.layout.listview_group_item;
    }

    @Override
    protected void getView(View view) {
        sep = view.findViewById(R.id.textViewitem);
    }

    @Override
    protected void showView() {
        SchoolBean.DataBean vo = (SchoolBean.DataBean) item;
        sep.setText(vo.getSchoolName());
    }
}
