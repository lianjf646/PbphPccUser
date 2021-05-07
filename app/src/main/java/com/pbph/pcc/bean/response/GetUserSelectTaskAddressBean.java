package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetUserSelectTaskAddressBean extends BaseResponseBean {

    /**
     * data : {"TaskAddressEntity":[{"addId":12,"addName":"顺丰"},{"addId":13,"addName":"圆通"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<TaskAddressEntityBean> TaskAddressEntity;

        public List<TaskAddressEntityBean> getTaskAddressEntity() {
            return TaskAddressEntity;
        }

        public void setTaskAddressEntity(List<TaskAddressEntityBean> TaskAddressEntity) {
            this.TaskAddressEntity = TaskAddressEntity;
        }

        public static class TaskAddressEntityBean {
            /**
             * addId : 12
             * addName : 顺丰
             */

            private int addId;
            private String addName;

            public int getAddId() {
                return addId;
            }

            public void setAddId(int addId) {
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
}
