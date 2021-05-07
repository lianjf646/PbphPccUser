package com.pbph.pcc.bean;

import com.utils.StringUtils;

/**
 * Created by Administrator on 2017/11/1.
 */

public class MyUserInfo extends ABean {


    private String userSex;
    private String receivePhone;
    private String userImg;
    private String isCertification;
    private String roleId;
    private String raddressName;
    private String userName;
    private String userAge;
    private String receiveId;
    private String isOrderSwitch;
    private String receiveName;
    private String receiveSex;
    private String schoolName;
    private int userStatus;
    private float starLevel = 0;
    private String schoolId;
    private String isSign;
    private String isRead;

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public float getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(float starLevel) {
        this.starLevel = starLevel;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getIsCertification() {
        return isCertification;
    }

    public void setIsCertification(String isCertification) {
        this.isCertification = isCertification;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRaddressName() {
        return raddressName;
    }

    public void setRaddressName(String raddressName) {
        this.raddressName = raddressName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getIsOrderSwitch() {
        if (StringUtils.isEmpty(isOrderSwitch)) {
            isOrderSwitch = "0";
        }
        return isOrderSwitch;
    }

    public void setIsOrderSwitch(String isOrderSwitch) {
        this.isOrderSwitch = isOrderSwitch;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveSex() {
        return receiveSex;
    }

    public void setReceiveSex(String receiveSex) {
        this.receiveSex = receiveSex;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
