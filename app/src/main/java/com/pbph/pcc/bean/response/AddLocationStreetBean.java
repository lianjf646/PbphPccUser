package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/29.
 */

public class AddLocationStreetBean extends BaseResponseBean {


    /**
     * code : 200
     * data : [{"addId":"16","addName":"第一食堂"}]
     * msg : 成功
     */


    private List<DataBean> data;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * addId : 16
         * addName : 第一食堂
         */

        private String addId;
        private String addName;

        public String getAddId() {
            return addId;
        }

        public void setAddId(String addId) {
            this.addId = addId;
        }

        public String getAddName() {
            return addName;
        }

        public void setAddName(String addName) {
            this.addName = addName;
        }
    }
}
