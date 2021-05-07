package com.pbph.pcc.bean.response;

import com.pbph.pcc.bean.MyUserInfo;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetMyInfoResponseBean extends BaseResponseBean {

    /**
     * code : 200
     * data : {"userSex":"1","receivePhone":"15636899875","userImg":"","isCertification":"","roleId":"4","raddressName":"我在找找","userName":"mmmm","userAge":"","receiveId":"80","isOrderSwitch":"1","receiveName":"我在找找","receiveSex":"1","schoolName":"哈工大"}
     * msg : 成功
     */


    private MyUserInfo data;


    public MyUserInfo getData() {
        return data;
    }

    public void setData(MyUserInfo data) {
        this.data = data;
    }


}
