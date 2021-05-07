package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetFigureWebResponseBean extends BaseResponseBean {


    /**
     * code : 200
     * data : [{"figureId":"2","figureImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1504837347282.jpg","figureCreatetime":"2017-09-08 10:22:27","figureUrl":"www.jd.com"},{"figureId":"3","figureImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1504837453090.jpg","figureCreatetime":"2017-09-08 10:24:18","figureUrl":"www.jd.com"},{"figureId":"5","figureImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1504837663619.jpg","figureCreatetime":"2017-09-08 10:27:37","figureUrl":"www.jd.com"}]
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
         * figureId : 2
         * figureImg : http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1504837347282.jpg
         * figureCreatetime : 2017-09-08 10:22:27
         * figureUrl : www.jd.com
         */

        private String figureId;
        private String figureImg;
        private String figureCreatetime;
        private String figureUrl;

        public String getFigureId() {
            return figureId;
        }

        public void setFigureId(String figureId) {
            this.figureId = figureId;
        }

        public String getFigureImg() {
            return figureImg;
        }

        public void setFigureImg(String figureImg) {
            this.figureImg = figureImg;
        }

        public String getFigureCreatetime() {
            return figureCreatetime;
        }

        public void setFigureCreatetime(String figureCreatetime) {
            this.figureCreatetime = figureCreatetime;
        }

        public String getFigureUrl() {
            return figureUrl;
        }

        public void setFigureUrl(String figureUrl) {
            this.figureUrl = figureUrl;
        }
    }
}
