package com.pbph.pcc.bean.vo;

import com.pbph.pcc.bean.ABean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class OrderBean extends ABean {
    public String imgUrlP;
    public String nameP;
    public String typeP;
    public String shopId;
    public Double shippingAmount;

    public Double getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(Double shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<ChildBean> childBeens = new ArrayList<ChildBean>();

    public String getImgUrlP() {
        return imgUrlP;
    }

    public void setImgUrlP(String imgUrlP) {
        this.imgUrlP = imgUrlP;
    }

    public String getNameP() {
        return nameP;
    }

    public void setNameP(String nameP) {
        this.nameP = nameP;
    }

    public String getTypeP() {
        return typeP;
    }

    public void setTypeP(String typeP) {
        this.typeP = typeP;
    }

    public List<ChildBean> getChildBeens() {
        return childBeens;
    }

    public void setChildBeens(List<ChildBean> childBeens) {
        this.childBeens = childBeens;
    }

    public static class ChildBean {
        public String imgUrlC;
        public String nameC;
        public int numC;
        public Double priceC;

        public String getImgUrlC() {
            return imgUrlC;
        }

        public void setImgUrlC(String imgUrlC) {
            this.imgUrlC = imgUrlC;
        }

        public String getNameC() {
            return nameC;
        }

        public void setNameC(String nameC) {
            this.nameC = nameC;
        }

        public int getNumC() {
            return numC;
        }

        public void setNumC(int numC) {
            this.numC = numC;
        }

        public Double getPriceC() {
            return priceC;
        }

        public void setPriceC(Double priceC) {
            this.priceC = priceC;
        }
    }
}
