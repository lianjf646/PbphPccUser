package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetActiveWebResponseBean extends BaseResponseBean {


    /**
     * msg : 成功
     * code : 200
     * data : {"result":[{"thirdActiveName":"测试活动","thirdActiveStarttime":"2017-11-06 01:05:00","thirdPhone":"15046810856","thirdId":"32","thirdActiveImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1509950256224.jpg","thirdActiveEndtime":"2017-12-07 10:50:00","thirdActiveStatus":"0","userName":"admin","thirdActiveCreatetime":"2017-11-06 14:37:37","thirdActiveContent":"<p>测试用 <br><\/p><p>&nbsp;活动<br><\/p>","thirdActivePreview":"测试活动"}],"newTime":"2017-11-06 14:37:37"}
     */


    private DataBean data;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * result : [{"thirdActiveName":"测试活动","thirdActiveStarttime":"2017-11-06 01:05:00","thirdPhone":"15046810856","thirdId":"32","thirdActiveImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1509950256224.jpg","thirdActiveEndtime":"2017-12-07 10:50:00","thirdActiveStatus":"0","userName":"admin","thirdActiveCreatetime":"2017-11-06 14:37:37","thirdActiveContent":"<p>测试用 <br><\/p><p>&nbsp;活动<br><\/p>","thirdActivePreview":"测试活动"}]
         * newTime : 2017-11-06 14:37:37
         */

        private String newTime;
        private List<ResultBean> result;

        public String getNewTime() {
            return newTime;
        }

        public void setNewTime(String newTime) {
            this.newTime = newTime;
        }

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean extends BaseActiveBean {
            /**
             * thirdActiveName : 测试活动
             * thirdActiveStarttime : 2017-11-06 01:05:00
             * thirdPhone : 15046810856
             * thirdId : 32
             * thirdActiveImg : http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1509950256224.jpg
             * thirdActiveEndtime : 2017-12-07 10:50:00
             * thirdActiveStatus : 0
             * userName : admin
             * thirdActiveCreatetime : 2017-11-06 14:37:37
             * thirdActiveContent : <p>测试用 <br></p><p>&nbsp;活动<br></p>
             * thirdActivePreview : 测试活动
             */

            private String thirdActiveName;
            private String thirdActiveStarttime;
            private String thirdPhone;
            private String thirdId;
            private String thirdActiveImg;
            private String thirdActiveEndtime;
            private String thirdActiveStatus;
            private String userName;
            private String thirdActiveCreatetime;
            private String thirdActiveContent;
            private String thirdActivePreview;

            public String getThirdActiveName() {
                return thirdActiveName;
            }

            public void setThirdActiveName(String thirdActiveName) {
                this.thirdActiveName = thirdActiveName;
                super.setTitle(thirdActiveName);
            }

            public String getThirdActiveStarttime() {
                return thirdActiveStarttime;
            }

            public void setThirdActiveStarttime(String thirdActiveStarttime) {
                this.thirdActiveStarttime = thirdActiveStarttime;
            }

            public String getThirdPhone() {
                return thirdPhone;
            }

            public void setThirdPhone(String thirdPhone) {
                this.thirdPhone = thirdPhone;
            }

            public String getThirdId() {
                return thirdId;
            }

            public void setThirdId(String thirdId) {
                this.thirdId = thirdId;
            }

            public String getThirdActiveImg() {
                return thirdActiveImg;
            }

            public void setThirdActiveImg(String thirdActiveImg) {
                this.thirdActiveImg = thirdActiveImg;
                super.setTime(thirdActiveCreatetime);
            }

            public String getThirdActiveEndtime() {
                return thirdActiveEndtime;
            }

            public void setThirdActiveEndtime(String thirdActiveEndtime) {
                this.thirdActiveEndtime = thirdActiveEndtime;
            }

            public String getThirdActiveStatus() {
                return thirdActiveStatus;
            }

            public void setThirdActiveStatus(String thirdActiveStatus) {
                this.thirdActiveStatus = thirdActiveStatus;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getThirdActiveCreatetime() {
                return thirdActiveCreatetime;
            }

            public void setThirdActiveCreatetime(String thirdActiveCreatetime) {
                this.thirdActiveCreatetime = thirdActiveCreatetime;
                super.setTime(thirdActiveCreatetime);
            }

            public String getThirdActiveContent() {
                return thirdActiveContent;
            }

            public void setThirdActiveContent(String thirdActiveContent) {
                this.thirdActiveContent = thirdActiveContent;
                super.setContent(thirdActiveContent);
            }

            public String getThirdActivePreview() {
                return thirdActivePreview;
            }

            public void setThirdActivePreview(String thirdActivePreview) {
                this.thirdActivePreview = thirdActivePreview;
            }

            @Override
            public String getImage() {
                return thirdActiveImg;
            }

            @Override
            public String getTitle() {
                return thirdActiveName;
            }

            @Override
            public String getContent() {
                return thirdActiveContent;
            }

            @Override
            public String getTime() {
                return thirdActiveCreatetime;
            }
        }
    }
}
