package com.pbph.pcc.viewholder;

import android.view.View;
import android.widget.TextView;

import com.android.adapter.ViewHolder;
import com.pbph.pcc.R;


public class SelectCityFastViewHolder extends ViewHolder {

	TextView title;

	@Override
	protected int getLayout() {
		return R.layout.listview_fastbar;
	}

	@Override
	protected void getView(View view) {
		title = view.findViewById(R.id.textViewfast);
	}

	@Override
	protected void showView() {

		String vo = (String) item;
		title.setText(vo);
	}
}
