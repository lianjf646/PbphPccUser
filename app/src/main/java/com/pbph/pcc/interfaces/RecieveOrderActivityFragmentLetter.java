package com.pbph.pcc.interfaces;

/**
 * Created by Administrator on 2017/10/17.
 */

public interface RecieveOrderActivityFragmentLetter {

    void flushData(
            String orderType,

            String getAddressId,
            String sendAddressId,

            String gettingAmountMin,
            String gettingAmountMax);

    void clearData();
}
