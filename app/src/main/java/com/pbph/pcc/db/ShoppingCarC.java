package com.pbph.pcc.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SHOPPING_CAR_C.
 */
public class ShoppingCarC {

    private Long id;
    private String shopId;
    private String goodName;
    private String goodId;
    private String schoolId;
    private String userId;
    private String goodImgUrl;
    private Double goodPrice;
    private Boolean goodIsChecked;
    private Integer goodNum;

    public ShoppingCarC() {
    }

    public ShoppingCarC(Long id) {
        this.id = id;
    }

    public ShoppingCarC(Long id, String shopId, String goodName, String goodId, String schoolId, String userId, String goodImgUrl, Double goodPrice, Boolean goodIsChecked, Integer goodNum) {
        this.id = id;
        this.shopId = shopId;
        this.goodName = goodName;
        this.goodId = goodId;
        this.schoolId = schoolId;
        this.userId = userId;
        this.goodImgUrl = goodImgUrl;
        this.goodPrice = goodPrice;
        this.goodIsChecked = goodIsChecked;
        this.goodNum = goodNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoodImgUrl() {
        return goodImgUrl;
    }

    public void setGoodImgUrl(String goodImgUrl) {
        this.goodImgUrl = goodImgUrl;
    }

    public Double getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(Double goodPrice) {
        this.goodPrice = goodPrice;
    }

    public Boolean getGoodIsChecked() {
        return goodIsChecked;
    }

    public void setGoodIsChecked(Boolean goodIsChecked) {
        this.goodIsChecked = goodIsChecked;
    }

    public Integer getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(Integer goodNum) {
        this.goodNum = goodNum;
    }

}
