package com.pbph.pcc.application;


/**
 * Created by Administrator on 2017/11/1.
 */

public final class SettingData {

    private static final String SETMESSAGE = "setMessage";
    private static final String SETVOICE = "setVoice";
    private static final String SETSHACK = "setShack";


    public void setSetMessage(boolean b) {
        PccApplication.mSharedPreferences.edit().putBoolean(SETMESSAGE, b).commit();
    }

    public boolean getSetMessage() {
        return PccApplication.mSharedPreferences.getBoolean(SETMESSAGE, true);
    }

    public void setSetVoice(boolean b) {
        PccApplication.mSharedPreferences.edit().putBoolean(SETVOICE, b).commit();
    }

    public boolean getSetVoice() {
        return PccApplication.mSharedPreferences.getBoolean(SETVOICE, true);
    }

    public void setSetShack(boolean b) {
        PccApplication.mSharedPreferences.edit().putBoolean(SETSHACK, b).commit();
    }

    public boolean getSetShack() {
        return PccApplication.mSharedPreferences.getBoolean(SETSHACK, true);
    }


}
