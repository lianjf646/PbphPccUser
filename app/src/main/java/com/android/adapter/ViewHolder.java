package com.android.adapter;

import  android.view.View;

public abstract class ViewHolder {

	protected DataAdapter adapter;

	protected int position;

	protected Object item;

	protected abstract int getLayout();

	protected abstract void getView(View view);

	protected abstract void showView();

}