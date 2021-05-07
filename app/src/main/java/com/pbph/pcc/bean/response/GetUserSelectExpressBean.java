package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetUserSelectExpressBean extends BaseResponseBean {

    /**
     * data : {"ExpressListEntity":[{"addrId":12,"addrName":"顺丰"},{"addrId":13,"addrName":"圆通"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ExpressListEntityBean> ExpressListEntity;

        public List<ExpressListEntityBean> getExpressListEntity() {
            return ExpressListEntity;
        }

        public void setExpressListEntity(List<ExpressListEntityBean> ExpressListEntity) {
            this.ExpressListEntity = ExpressListEntity;
        }

        public static class ExpressListEntityBean {
            /**
             * addrId : 12
             * addrName : 顺丰
             */

            private int addrId;
            private String addrName;

            public int getAddrId() {
                return addrId;
            }

            public void setAddrId(int addrId) {
                this.addrId = addrId;
            }

            public String getAddrName() {
                return addrName;
            }

            public void setAddrName(String addrName) {
                this.addrName = addrName;
            }
        }
    }
}
