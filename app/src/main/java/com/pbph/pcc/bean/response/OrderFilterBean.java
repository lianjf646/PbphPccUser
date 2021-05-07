package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.
 */

public class OrderFilterBean extends BaseResponseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * addrFloor :
         * coordinate : 126.707842,45.766156
         * addrOn : 1
         * schoolId : 2
         * addrId : 12
         * addrLevel : b
         * type :
         * thestallNum :
         * addrName : 顺丰
         * addrType : 5
         */

        private String addrFloor;
        private String coordinate;
        private String addrOn;
        private String schoolId;
        private String addrId;
        private String addrLevel;
        private String type;
        private String thestallNum;
        private String addrName;
        private String addrType;

        public String getAddrFloor() {
            return addrFloor;
        }

        public void setAddrFloor(String addrFloor) {
            this.addrFloor = addrFloor;
        }

        public String getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(String coordinate) {
            this.coordinate = coordinate;
        }

        public String getAddrOn() {
            return addrOn;
        }

        public void setAddrOn(String addrOn) {
            this.addrOn = addrOn;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getAddrId() {
            return addrId;
        }

        public void setAddrId(String addrId) {
            this.addrId = addrId;
        }

        public String getAddrLevel() {
            return addrLevel;
        }

        public void setAddrLevel(String addrLevel) {
            this.addrLevel = addrLevel;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getThestallNum() {
            return thestallNum;
        }

        public void setThestallNum(String thestallNum) {
            this.thestallNum = thestallNum;
        }

        public String getAddrName() {
            return addrName;
        }

        public void setAddrName(String addrName) {
            this.addrName = addrName;
        }

        public String getAddrType() {
            return addrType;
        }

        public void setAddrType(String addrType) {
            this.addrType = addrType;
        }
    }
}
