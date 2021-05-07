package com.pbph.pcc.bean.response;


import java.util.List;

public class SchoolBean extends BaseResponseBean {

    /**
     * code : 200
     * data : [{"schoolId":"1","schoolName":"黑龙江科技大学"},{"schoolId":"2","schoolName":"哈工大"},{"schoolId":"7","schoolName":"北大"},{"schoolId":"8","schoolName":"123123"},{"schoolId":"9","schoolName":"qinghua"},{"schoolId":"10","schoolName":"清华大学好啊"},{"schoolId":"11","schoolName":"123123"},{"schoolId":"12","schoolName":"33"},{"schoolId":"13","schoolName":"33"},{"schoolId":"14","schoolName":"1"},{"schoolId":"15","schoolName":"1"},{"schoolId":"16","schoolName":"1"},{"schoolId":"17","schoolName":"1"},{"schoolId":"18","schoolName":"1"}]
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
         * schoolId : 1
         * schoolName : 黑龙江科技大学
         */

        private String schoolId;
        private String schoolName;

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }
    }
}
