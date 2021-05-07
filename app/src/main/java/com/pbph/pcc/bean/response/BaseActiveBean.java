package com.pbph.pcc.bean.response;

import com.pbph.pcc.bean.ABean;

/**
 * Created by Administrator on 2017/9/21.
 */

public abstract class BaseActiveBean extends ABean {

    String image = "";
    String title = "";
    String content = "";
    String time = "";

    public abstract String getImage();

    public abstract String getTitle();

    public abstract String getContent();

    public abstract String getTime();


    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
