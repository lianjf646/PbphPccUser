package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/22.
 */

public class QueryRongYunResponseBean extends BaseResponseBean {


    /**
     * code : 200
     * data : {"tokenId":"ERqQnfIBQpyh2NMfMK2zv5ybWdMCeAFri83vE2gZufVJHHM1Bw6/wa3E9pBczNmuhRSI2tZAx4Qd0JPNuhOlWw==","userId":"20"}
     * FBkHZw1Pv5fnT/OoQHHHXuYDrgkgbwtjjhjgsKeBfRcwR14UT/+LHrqCp20ocD1N2udHm3p2qtxvV4Y/7FS39T6XLkjSw0belyNjroJop4TmOj2Ed3OrRgpsNgX2HG7n
     * msg : 成功
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
         * tokenId : ERqQnfIBQpyh2NMfMK2zv5ybWdMCeAFri83vE2gZufVJHHM1Bw6/wa3E9pBczNmuhRSI2tZAx4Qd0JPNuhOlWw==
         * userId : 20
         */

        private String tokenId;
        private String userId;

        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
