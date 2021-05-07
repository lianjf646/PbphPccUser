package com.pbph.pcc.bean.dao;

import com.pbph.pcc.bean.ABean;
import com.pbph.pcc.db.ShoppingCarC;
import com.pbph.pcc.db.ShoppingCarP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class ShoppingCarBean extends ABean {
    public ShoppingCarP shoppingCarP;

    public ShoppingCarP getShoppingCarP() {
        return shoppingCarP;
    }

    public void setShoppingCarP(ShoppingCarP shoppingCarP) {
        this.shoppingCarP = shoppingCarP;
    }

    public List<ShoppingCarC> shoppingCarCs = new ArrayList<ShoppingCarC>();

    public List<ShoppingCarC> getShoppingCarCs() {
        return shoppingCarCs;
    }

    public void setShoppingCarCs(List<ShoppingCarC> shoppingCarCs) {
        this.shoppingCarCs = shoppingCarCs;
    }
}
