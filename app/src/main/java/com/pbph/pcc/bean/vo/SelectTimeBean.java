package com.pbph.pcc.bean.vo;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class SelectTimeBean {
    Date date ;
    boolean isChecked;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
