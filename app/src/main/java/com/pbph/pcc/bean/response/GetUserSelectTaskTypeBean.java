package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetUserSelectTaskTypeBean extends BaseResponseBean {

    /**
     * data : {"TaskTypeEntity":[{"referId":50,"referName":"跑步"},{"referId":51,"referName":"散步"},{"referId":52,"referName":"一起游泳"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<TaskTypeEntityBean> TaskTypeEntity;

        public List<TaskTypeEntityBean> getTaskTypeEntity() {
            return TaskTypeEntity;
        }

        public void setTaskTypeEntity(List<TaskTypeEntityBean> TaskTypeEntity) {
            this.TaskTypeEntity = TaskTypeEntity;
        }

        public static class TaskTypeEntityBean {
            /**
             * referId : 50
             * referName : 跑步
             */

            private int referId;
            private String referName;

            public int getReferId() {
                return referId;
            }

            public void setReferId(int referId) {
                this.referId = referId;
            }

            public String getReferName() {
                return referName;
            }

            public void setReferName(String referName) {
                this.referName = referName;
            }
        }
    }
}
