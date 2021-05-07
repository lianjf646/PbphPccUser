package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetUserTaskInfoBean extends BaseResponseBean {

    /**
     * data : {"TaskInfoEntity":{"taskPrice":"1.50","taskScope":"5","taskNoresponseTime":"1","taskOrderLoseTime":"","taskStatus":"0","taskResortTime":"1"}}
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
         * TaskInfoEntity : {"taskPrice":"1.50","taskScope":"5","taskNoresponseTime":"1","taskOrderLoseTime":"","taskStatus":"0","taskResortTime":"1"}
         */

        private TaskInfoEntityBean TaskInfoEntity;

        public TaskInfoEntityBean getTaskInfoEntity() {
            return TaskInfoEntity;
        }

        public void setTaskInfoEntity(TaskInfoEntityBean TaskInfoEntity) {
            this.TaskInfoEntity = TaskInfoEntity;
        }

        public static class TaskInfoEntityBean {
            /**
             * taskPrice : 1.50
             * taskScope : 5
             * taskNoresponseTime : 1
             * taskOrderLoseTime :
             * taskStatus : 0
             * taskResortTime : 1
             */

            private String taskPrice;
            private String taskScope;
            private String taskNoresponseTime;
            private String taskOrderLoseTime;
            private String taskStatus;
            private String taskResortTime;

            public String getTaskPrice() {
                return taskPrice;
            }

            public void setTaskPrice(String taskPrice) {
                this.taskPrice = taskPrice;
            }

            public String getTaskScope() {
                return taskScope;
            }

            public void setTaskScope(String taskScope) {
                this.taskScope = taskScope;
            }

            public String getTaskNoresponseTime() {
                return taskNoresponseTime;
            }

            public void setTaskNoresponseTime(String taskNoresponseTime) {
                this.taskNoresponseTime = taskNoresponseTime;
            }

            public String getTaskOrderLoseTime() {
                return taskOrderLoseTime;
            }

            public void setTaskOrderLoseTime(String taskOrderLoseTime) {
                this.taskOrderLoseTime = taskOrderLoseTime;
            }

            public String getTaskStatus() {
                return taskStatus;
            }

            public void setTaskStatus(String taskStatus) {
                this.taskStatus = taskStatus;
            }

            public String getTaskResortTime() {
                return taskResortTime;
            }

            public void setTaskResortTime(String taskResortTime) {
                this.taskResortTime = taskResortTime;
            }
        }
    }
}
