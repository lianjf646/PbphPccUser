package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetNoticeWebResponseBean extends BaseResponseBean {

    /**
     * msg : 成功
     * code : 200
     * data : {"result":[{"noticeContent":"<p>测试用<br><\/p>","noticeImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1509950289751.png","noticePreview":"测试用","noticeCreatetime":"2017-11-06 14:38:03","noticeType":"","userName":"admin","noticeId":"20","noticeTitle":"测试用"}],"newTime":"2017-11-06 14:38:03"}
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
         * result : [{"noticeContent":"<p>测试用<br><\/p>","noticeImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1509950289751.png","noticePreview":"测试用","noticeCreatetime":"2017-11-06 14:38:03","noticeType":"","userName":"admin","noticeId":"20","noticeTitle":"测试用"}]
         * newTime : 2017-11-06 14:38:03
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
             * noticeContent : <p>测试用<br></p>
             * noticeImg : http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1509950289751.png
             * noticePreview : 测试用
             * noticeCreatetime : 2017-11-06 14:38:03
             * noticeType :
             * userName : admin
             * noticeId : 20
             * noticeTitle : 测试用
             */

            private String noticeContent;
            private String noticeImg;
            private String noticePreview;
            private String noticeCreatetime;
            private String noticeType;
            private String userName;
            private String noticeId;
            private String noticeTitle;

            public String getNoticeContent() {
                return noticeContent;
            }

            public void setNoticeContent(String noticeContent) {
                this.noticeContent = noticeContent;
                super.setContent(noticeContent);
            }

            public String getNoticeImg() {
                return noticeImg;
            }

            public void setNoticeImg(String noticeImg) {
                this.noticeImg = noticeImg;
                super.setImage(noticeImg);
            }

            public String getNoticePreview() {
                return noticePreview;
            }

            public void setNoticePreview(String noticePreview) {
                this.noticePreview = noticePreview;
            }

            public String getNoticeCreatetime() {
                return noticeCreatetime;
            }

            public void setNoticeCreatetime(String noticeCreatetime) {
                this.noticeCreatetime = noticeCreatetime;
                super.setTime(noticeCreatetime);
            }

            public String getNoticeType() {
                return noticeType;
            }

            public void setNoticeType(String noticeType) {
                this.noticeType = noticeType;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getNoticeId() {
                return noticeId;
            }

            public void setNoticeId(String noticeId) {
                this.noticeId = noticeId;
            }

            public String getNoticeTitle() {
                return noticeTitle;
            }

            public void setNoticeTitle(String noticeTitle) {
                this.noticeTitle = noticeTitle;
                super.setTitle(noticeTitle);
            }

            @Override
            public String getImage() {
                return noticeImg;
            }

            @Override
            public String getTitle() {
                return noticeTitle;
            }

            @Override
            public String getContent() {
                return noticeContent;
            }

            @Override
            public String getTime() {
                return noticeCreatetime;
            }
        }
    }
}
