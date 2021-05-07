package com.pbph.pcc.bean.response;


import java.util.List;

public class SchoolNotOpenBean extends BaseResponseBean {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
