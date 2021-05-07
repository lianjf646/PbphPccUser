package com.pbph.pcc.application;


/**
 * Created by Administrator on 2017/11/1.
 */

public final class HttpRequestData {

    private final static String TOKEN = "token";


    public String getToken() {
        return PccApplication.mSharedPreferences.getString(TOKEN, "");
    }

    public void setToken(String token) {
        PccApplication.mSharedPreferences.edit().putString(TOKEN, token).commit();
    }

}
