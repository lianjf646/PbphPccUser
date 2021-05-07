package com.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAdapter extends BaseAdapter {

    public Object activity;
    public Context context;
    public LayoutInflater inflater;
    public Resources resources;

    public View view;// adapter 持有者控件

    protected int choice_type = ListView.CHOICE_MODE_NONE;

    //
    protected int view_type_count = 1;
    // key position value view type
    protected List<Class> viewTypeCount;
    // viewDataList.get(i)'s viewholder is viewTypeList.get(i)
    protected List<Integer> viewTypeList;
    // vo
    protected List<Object> viewDataList = new ArrayList<Object>(10);
    //

    // if CHOICE_MODE_NONE not use
    // if CHOICE_MODE_SINGLE it just has only one key that you choiced position.
    // if CHOICE_MODE_MULTIPLE has many choice positions wich you choiced.
    public Map<Integer, Object> choiceMap;

    public Map<Class, Boolean> enableClsMap = new HashMap<Class, Boolean>();

    private DataAdapter(Object activity, View view, Class viewholder,
                        int view_type_count, Class... clss) {

        this.activity = activity;
        if (activity instanceof Activity) {
            context = (Activity) activity;
        } else {
            context = ((Fragment) activity).getContext();
        }
        this.inflater = LayoutInflater.from(context);
        this.resources = (context).getResources();

        this.view = view;

        this.view_type_count = view_type_count;

        if (clss != null) {
            for (int i = 0; i < clss.length; i++) {
                enableClsMap.put(clss[i], false);
            }
        }

        viewTypeCount = new ArrayList<Class>(view_type_count);
        if (viewholder != null) {
            viewTypeCount.add(viewholder);
        }

        if (view_type_count > 1) {
            viewTypeList = new ArrayList<Integer>(10);
        }

        ((AbsListView) view).setAdapter(this);
        setChoice_type(((AbsListView) view).getChoiceMode());
    }

    public DataAdapter(Object activity, View view, Class viewholder) {
        this(activity, view, viewholder, 1);
    }

    public DataAdapter(Object activity, View view, int view_type_count) {
        this(activity, view, null, view_type_count);
    }

    @Override
    public int getViewTypeCount() {
        return view_type_count;
        // return viewTypeList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (view_type_count == 1) {
            return 0;
        }
        return viewTypeList.get(position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        Boolean bool = enableClsMap.get(getItemViewTypeClass(position));
        if (bool != null) {
            return bool;
        }
        return true;
    }

    @Override
    public int getCount() {
        return viewDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return viewDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Class getItemViewTypeClass(int position) {
        return viewTypeCount.get(getItemViewType(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            try {
                String clsName = getItemViewTypeClass(position).getName();
                holder = (ViewHolder) Class.forName(clsName).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.adapter = this;

            convertView = inflater.inflate(holder.getLayout(), null);

            holder.getView(convertView);

            convertView.setTag(holder);// important必须有

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;
        holder.item = getItem(position);

        holder.showView();

        return convertView;
    }

    // ////single adapter use.*if separator adapter use will get error;
    public <T extends Object> void setDatas(List<T> datas) {

        clearDatas();

        addDatas(datas);

        notifyDataSetChanged();
    }

    //
    public <T extends Object> void addDatas(List<T> datas) {
        if (datas == null || datas.size() <= 0) return;
        viewDataList.addAll(datas);
    }

    //
    public <T extends Object> void addData(T data) {
        viewDataList.add(data);
        notifyDataSetChanged();
    }

    public <T extends Object> void addDataJust(T data) {
        viewDataList.add(data);
    }

    public <T extends Object> void addData(int pos, T data) {
        if (pos >= viewDataList.size()) {
            pos = viewDataList.size();
        }
        viewDataList.add(pos, data);
    }

    public <T extends Object> void addData(T data, Class viewholder) {

        int type = viewTypeCount.indexOf(viewholder);
        if (type == -1) {
            viewTypeCount.add(viewholder);
            type = viewTypeCount.indexOf(viewholder);
        }
        viewDataList.add(data);
        viewTypeList.add(type);
    }

    public <T extends Object> void addData(int pos, T data, Class viewholder) {

        int type = viewTypeCount.indexOf(viewholder);
        if (type == -1) {
            viewTypeCount.add(viewholder);
            type = viewTypeCount.indexOf(viewholder);
        }
        if (pos >= viewDataList.size()) {
            pos = viewDataList.size();
        }
        viewDataList.add(pos, data);
        viewTypeList.add(pos, type);
    }

    //
    public void removeData(int pos) {
        viewDataList.remove(pos);
        if (viewTypeList != null) {
            viewTypeList.remove(pos);
        }
    }

    public <T extends Object> void removeData(T vo) {
        int pos = viewDataList.indexOf(vo);
        if (pos < 0) {
            return;
        }
        viewDataList.remove(pos);
        if (viewTypeList != null) {
            viewTypeList.remove(pos);
        }
    }

    //
    public void clearDatas() {
        viewDataList.clear();
        if (viewTypeList != null) {
            viewTypeList.clear();
        }
        clearChoices();

        notifyDataSetChanged();
    }

    //
    public void clearChoices() {
        if (choiceMap == null) {
            return;
        }
        choiceMap.clear();
        notifyDataSetChanged();
    }

    public void putChoiced(int pos) {
        switch (choice_type) {
            case ListView.CHOICE_MODE_SINGLE: {
                choiceMap.clear();
                choiceMap.put(pos, 0);
            }
            break;
            case ListView.CHOICE_MODE_MULTIPLE: {
                Object obj = choiceMap.get(pos);
                if (obj != null) {
                    choiceMap.remove(pos);
                } else {
                    choiceMap.put(pos, 0);
                }
            }
            break;
        }
    }

    public void putChoicedNotify(int pos) {
        putChoiced(pos);
        notifyDataSetChanged();
    }

    public boolean isChoiced(int pos) {
        return choiceMap.get(pos) != null;
    }

    //
    public void setChoice_type(int choice_type) {
        this.choice_type = choice_type;

        if (ListView.CHOICE_MODE_NONE != choice_type) {
            choiceMap = new HashMap<Integer, Object>(10);
        }

    }

    public boolean contains(Object obj) {
        return viewDataList.contains(obj);
    }

}