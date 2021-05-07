package com.android.adapter;

import android.view.View;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSpeAdapter extends DataAdapter {


    Map<Integer, KV> adapter_pos_2_true_pos_map = new HashMap<>();
    int dataCount = 0;

    OnGetSon onGetSon;


    public DataSpeAdapter(Object activity, View view, Class farViewHolder, Class sonViewHolder, OnGetSon onGetSon) {
        super(activity, view, 2);

        viewTypeCount.add(0, farViewHolder);
        viewTypeCount.add(1, sonViewHolder);

        this.onGetSon = onGetSon;
    }

    public KV getKV(int adapter_pos) {
        return adapter_pos_2_true_pos_map.get(adapter_pos);
    }


    public Object getItemByKV(KV kv) {
        Object obj = viewDataList.get(kv.fpos);
        if (kv.spos == -1)
            return obj;

        return onGetSon.getSonList(obj).get(kv.spos);

    }

    public Object getItemByFS(int fpos, int spos) {
        Object obj = viewDataList.get(fpos);
        if (spos == -1)
            return obj;

        return onGetSon.getSonList(obj).get(spos);

    }

    @Override
    public int getCount() {
        return dataCount;
    }

    @Override
    public Object getItem(int adapter_pos) {
        return getItemByKV(getKV(adapter_pos));
    }


    ////////////

    @Override
    public int getItemViewType(int adapter_pos) {
        KV vo = getKV(adapter_pos);
        if (vo.spos == -1) {
            return 0;
        } else {
            return 1;
        }
    }

    public Class getItemViewTypeClass(int adapter_pos) {
        return viewTypeCount.get(getItemViewType(adapter_pos));
    }

    private void flushDatas() {

        adapter_pos_2_true_pos_map.clear();
        dataCount = 0;

        clearChoices();


        int adapter_pos = -1;

        for (int i = 0, counti = viewDataList.size(); i < counti; i++) {

            adapter_pos += 1;

            putPosMap(adapter_pos, i, -1);

            List<Object> sonList = onGetSon.getSonList(viewDataList.get(i));
            if (sonList == null || sonList.size() <= 0) continue;

            for (int j = 0, countj = sonList.size(); j < countj; j++) {
                adapter_pos += 1;
                putPosMap(adapter_pos, i, j);
            }
        }
        dataCount += adapter_pos + 1;
    }


    private void putPosMap(int adapter_pos, int i, int j) {
        KV vo = new KV();
        vo.fpos = i;
        vo.spos = j;
        adapter_pos_2_true_pos_map.put(adapter_pos, vo);
    }

    public <T extends Object> void setDatas(List<T> datas) {

        clearDatas();

        addDatas(datas);

        notifyDataSetChanged();
    }

    //
    public <T extends Object> void addDatas(List<T> datas) {
        super.addDatas(datas);
        flushDatas();
    }

    //
    public <T extends Object> void addData(T data) {
        viewDataList.add(data);

        flushDatas();
        notifyDataSetChanged();
    }

    public <T extends Object> void addData(int true_fpos, T data) {
        if (true_fpos >= viewDataList.size()) {
            true_fpos = viewDataList.size();
        }

        viewDataList.add(true_fpos, data);
        flushDatas();
        notifyDataSetChanged();
    }

    public <T extends Object> void addSonData(int true_fpos, T data) {
        Object obj = viewDataList.get(true_fpos);
        List<Object> list = onGetSon.getSonList(obj);
        list.add(data);

        flushDatas();
        notifyDataSetChanged();
    }

    public <T extends Object> void addSonData(int true_fpos, int true_spos, T data) {

        Object obj = viewDataList.get(true_fpos);

        List<Object> list = onGetSon.getSonList(obj);

        if (true_spos >= list.size()) {
            true_spos = list.size();
        }
        list.add(true_spos, data);

        flushDatas();
        notifyDataSetChanged();
    }

    public <T extends Object> void addData(T data, Class viewholder) {
        addData(data);
    }

    public <T extends Object> void addData(int true_fpos, T data, Class viewholder) {
        addData(true_fpos, data);
    }

    //
    public void removeData(int adapter_pos) {

        KV vo = getKV(adapter_pos);
        if (vo.spos == -1) {
            viewDataList.remove(vo.fpos);
        } else {
            List<Object> list = onGetSon.getSonList(viewDataList.get(vo.fpos));
            list.remove(vo.spos);
        }

        flushDatas();
        notifyDataSetChanged();


    }

    public <T extends Object> void removeData(T vo) {
    }

    //
    public void clearDatas() {

        adapter_pos_2_true_pos_map.clear();
        dataCount = 0;

        viewDataList.clear();
        if (viewTypeList != null) {
            viewTypeList.clear();
        }

        clearChoices();

        notifyDataSetChanged();
    }

    public void clearDatasNotify() {

        clearDatas();

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

    public void putChoiced(int adapter_pos) {

        KV vo = getKV(adapter_pos);

        switch (choice_type) {
            case ListView.CHOICE_MODE_SINGLE: {
                choiceMap.clear();
                choiceMap.put(adapter_pos, vo);
            }
            break;
            case ListView.CHOICE_MODE_MULTIPLE: {
                Object obj = choiceMap.get(adapter_pos);
                if (obj != null) {
                    choiceMap.remove(adapter_pos);
                } else {
                    choiceMap.put(adapter_pos, vo);
                }
            }
            break;
        }
    }

    public void putChoicedNotify(int adapter_pos) {
        putChoiced(adapter_pos);
        notifyDataSetChanged();
    }

    public boolean isChoiced(int adapter_pos) {
        return choiceMap.get(adapter_pos) != null;
    }


    public final class KV {
        public int fpos = -1;
        public int spos = -1;
    }

    public interface OnGetSon {
        public List getSonList(Object obj);
    }

}