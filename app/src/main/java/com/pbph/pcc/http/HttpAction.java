package com.pbph.pcc.http;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.request.BaseRequestBean;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.MD5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HttpAction {
    private static HttpAction instance;

    public static HttpAction getInstance() {
        if (instance == null) {
            synchronized (HttpAction.class) {
                if (instance == null) {
                    instance = new HttpAction();
                }
            }
        }
        return instance;
    }


    private HttpAction() {

    }

    public String downLoadFile(String url, HttpService.OnDownLoadListener onListener) {
        HttpService service = new HttpService();
        return service.downLoadFile(url, onListener);
    }

    public void cancelCallService(String callId) {
        HttpService service = new HttpService();
        service.cancelCallService(callId);
    }

    public void cancelRequest(Object tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    public void cancelALLRequest() {
        OkGo.getInstance().cancelAll();
    }

    public void sendSmsValCode(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e(object.toString());
        OkGo.<String>post(ConstantData.sendSmsValCode).tag(ConstantData.sendSmsValCode).upJson(object).execute(callBack);
    }

    public void userLogin(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e(object.toString());
        LogUtils.e(ConstantData.appUserLogin);
        OkGo.<String>post(ConstantData.appUserLogin).tag(ConstantData.appUserLogin).upJson(object).execute(callBack);

    }

    public void getAppAuthorize(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e("getAppAuthorize" + object.toString());
        OkGo.<String>post(ConstantData.getAppAuthorize).tag(ConstantData.getAppAuthorize).upJson(object).execute(callBack);

    }

    public void messageWeb(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        Log.e("messageWeb", object.toString());
        OkGo.<String>post(ConstantData.messageWeb).tag(ConstantData.messageWeb).upJson(object).execute(callBack);

    }

    public void messageRead(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        Log.e("messageRead", object.toString());
        OkGo.<String>post(ConstantData.messageRead).tag(ConstantData.messageRead).upJson(object).execute(callBack);

    }

    public void sendRed(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e(object.toString());
        OkGo.<String>post(ConstantData.sendRed).tag(ConstantData.sendRed).upJson(object).execute(callBack);

    }

    public void queryMyInfo(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e(object.toString());
        try {
            LogUtils.e(ConstantData.queryMyInfo);
            OkGo.<String>post(ConstantData.queryMyInfo).tag(ConstantData.queryMyInfo).upJson(object).execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getOssToken(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e("getOssToken" + object.toString());
        try {
            OkGo.<String>post(ConstantData.getOssToken).tag(ConstantData.getOssToken).upJson(object).execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getJpushId(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e("getJpushId" + object.toString());
        OkGo.<String>post(ConstantData.getJpushId).tag(ConstantData.getJpushId).upJson(object).execute(callBack);
    }

    public void figureWeb(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e(object.toString());
        OkGo.<String>post(ConstantData.figureWeb).tag(ConstantData.figureWeb).upJson(object).execute(callBack);
    }

    public void queryRongYun(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e("queryRongYun " + object.toString());
        OkGo.<String>post(ConstantData.queryRongYun).tag(ConstantData.queryRongYun).upJson(object).execute(callBack);
    }

    public void queryTakeAddress(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e(object.toString());
        try {
            OkGo.<String>post(ConstantData.queryTakeAddress).tag(ConstantData.queryTakeAddress).upJson(object).execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void myTakeAddress(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e("myTakeAddress " + object.toString());
        try {
            OkGo.<String>post(ConstantData.myTakeAddress).tag(ConstantData.myTakeAddress).upJson(object).execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void noticeWeb(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e(object.toString());
        try {
            OkGo.<String>post(ConstantData.noticeWeb).tag(ConstantData.noticeWeb).upJson(object).execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatusWeb(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        Log.e("updateStatusWeb", object.toString());
        try {
            OkGo.<String>post(ConstantData.updateStatusWeb).tag(ConstantData.updateStatusWeb).upJson(object).execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activeWeb(BaseRequestBean bean, Callback callBack) {
        JSONObject object = bean.toJson();
        LogUtils.e(object.toString());
        try {
            OkGo.<String>post(ConstantData.activeWeb).tag(ConstantData.activeWeb).upJson(object).execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加收货地址
     *
     * @param taskName
     * @param taskSex
     * @param taskPhone
     * @param userId
     * @param addrId
     * @param addrName
     * @param defaultAdd
     * @param callBack
     */
    public void addTakeAddress(String taskName, String taskSex, String taskPhone, int userId, int addrId
            , String addrName, int defaultAdd, String schoolId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("taskName", taskName);
            object.put("taskSex", taskSex);
            object.put("taskPhone", taskPhone);
            object.put("userId", userId);
            object.put("addrId", addrId);
            object.put("addrName", addrName);
            object.put("defaultAddress", defaultAdd);
            object.put("schoolId", schoolId);
            OkGo.<String>post(ConstantData.addTakeAddress).upJson(getRequestData(object)).execute(callBack);
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    /**
     * 修改收货地址
     *
     * @param receiveId
     * @param taskName
     * @param taskSex
     * @param taskPhone
     * @param userId
     * @param addrId
     * @param addrName
     * @param defaultAdd
     * @param callBack
     */
    public void updateTakeAddress(int receiveId, String taskName, String taskSex, String taskPhone,
                                  int userId, int addrId, String addrName, int defaultAdd, String schoolId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("receiveId", receiveId);
            object.put("taskName", taskName);
            object.put("taskSex", taskSex);
            object.put("taskPhone", taskPhone);
            object.put("userId", userId);
            object.put("addrId", addrId);
            object.put("addrName", addrName);
            object.put("defaultAddress", defaultAdd);
            object.put("schoolId", schoolId);

            OkGo.<String>post(ConstantData.updateTakeAddress).upJson(getRequestData(object)).execute(callBack);
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }


    /**
     * 删除地址
     *
     * @param receivingId
     * @param callBack
     */
    public void delTakeAddressByAddId(int receivingId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("receiveId", receivingId);
            OkGo.<String>post(ConstantData.delTakeAddressByAddId).upJson(getRequestData(object)).execute(callBack);
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onError(null);
        }

    }

    /**
     * 修改默认地址
     *
     * @param receiveId
     * @param userId
     * @param callback
     */
    public void defaultAddress(String receiveId, String userId, String schoolId, Callback callback) {

        try {
            JSONObject object = new JSONObject();
            object.put("receiveId", receiveId);
            object.put("userId", userId);
            object.put("schoolId", schoolId);
//            Log.e("FFFFFFFFFFFFFF", "defaultAddress: " + receiveId + "??" + userId);
            OkGo.<String>post(ConstantData.defaultAddress).upJson(getRequestData(object)).execute(callback);

        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError(null);
        }

    }

    /**
     * 实名认证 创创入住
     *
     * @param authStudenCardImg
     * @param authName
     * @param authSex
     * @param authSchool
     * @param authNum
     * @param authPhone
     * @param authCode
     * @param userId
     * @param callback
     */
    public void identification(String authStudenCardImg, String authName, String authSex, String authSchool,
                               String authNum, String authPhone, String authCode, String userId, Callback callback) {
        try {
            com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
            object.put("authStudenCardImg", authStudenCardImg);
            object.put("authName", authName);
            object.put("authSex", authSex);
            object.put("authSchool", authSchool);
            object.put("authNum", authNum);
            object.put("authPhone", authPhone);
            object.put("authCode", authCode);
            object.put("userId", userId);
            OkGo.<String>post(ConstantData.identification).upJson(getRequestData(object).toJSONString()).execute(callback);//ConstantData.identification
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError(null);
        }
    }

    /**
     * 申请代理，显示学生信息
     *
     * @param userId
     * @param callback
     */
    public void queryAgent(String userId, Callback callback) {

        try {
            JSONObject object = new JSONObject();
            object.put("userId", userId);
            LogUtils.e("queryAgent: " + userId);
            OkGo.<String>post(ConstantData.queryAgent).upJson(getRequestData(object)).execute(callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 代理申请 上传图片
     */
    public void applyAgent(String userId, String idCardImg, Callback callback) {
        try {
            com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
            object.put("userId", userId);
            object.put("idCardImg", idCardImg);
            OkGo.<String>post(ConstantData.applyAgent).upJson(getRequestData(object).toJSONString()).execute(callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void userLogin(String phone, String code, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("userPhone", phone);
            object.put("validCode", code);
            OkGo.<String>post(ConstantData.appUserLogin).upJson(getRequestData(object)).execute(callBack);
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    private JSONObject getRequestData(JSONObject object) throws JSONException {
        JSONObject jsonData = getPublicRequestData(object, PccApplication.getUserid(), PccApplication.httpRequestData.getToken(), PccApplication.getImei());
        LogUtils.e("getRequestData " + jsonData.toString());
        return jsonData;
    }

    private com.alibaba.fastjson.JSONObject getRequestData(com.alibaba.fastjson.JSONObject object) throws JSONException {
        com.alibaba.fastjson.JSONObject jsonData = getPublicRequestData(object, PccApplication.getUserid(), PccApplication.httpRequestData.getToken(), PccApplication.getImei());
        LogUtils.e("getRequestData " + jsonData.toString());
        return jsonData;
    }

    private JSONObject getPublicRequestData(@NonNull JSONObject data, String userid, String token, String deviceId) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("userid", userid);
        object.put("token", token);
        object.put("sign", MD5.encrypt(data.toString()).toUpperCase());
        object.put("timestamp", System.currentTimeMillis());
        object.put("type", "1");
        object.put("deviceId", deviceId);
        object.put("data", data);
        return object;
    }

    private com.alibaba.fastjson.JSONObject getPublicRequestData(@NonNull com.alibaba.fastjson.JSONObject data, String userid, String token, String deviceId) throws JSONException {
        com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
        object.put("userid", userid);
        object.put("token", token);
        object.put("sign", MD5.encrypt(data.toString()).toUpperCase());
        object.put("timestamp", System.currentTimeMillis());
        object.put("type", "1");
        object.put("deviceId", deviceId);
        object.put("data", data);
        return object;
    }

    public void querySchool(Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            okgo(ConstantData.QUERYSCHOOL, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void querySchoolCancel() {
        cancel(ConstantData.QUERYSCHOOL);
    }


    public void queryNotOpenSchool(Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            okgo(ConstantData.QUERYNOTOPENSCHOOL, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void queryNotOpenSchoolCancel() {
        cancel(ConstantData.QUERYNOTOPENSCHOOL);
    }


    public void getUserStore(int schoolId
            , int startRowNum
            , int endRowNum
            , Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            object.put("startRowNum", startRowNum);
            object.put("endRowNum", endRowNum);
            okgo(ConstantData.GETUSERSTORE, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserStoreCancel() {
        cancel(ConstantData.GETUSERSTORE);
    }


    public void getUserStoreInfo(int storeId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("storeId", storeId);
            okgo(ConstantData.GETUSERSTOREINFO, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserStoreInfoCancel() {
        cancel(ConstantData.GETUSERSTOREINFO);
    }


    public void getUserStoreGoods(int storeId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("storeId", storeId);
            okgo(ConstantData.GETUSERSTOREGOODS, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserStoreGoodsCancel() {
        cancel(ConstantData.GETUSERSTOREGOODS);
    }

    public void getUserMarketGoods(int cityId, String marketGoodsName, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("cityId", cityId);
            object.put("marketGoodsName", marketGoodsName);
            okgo(ConstantData.GETUSERMARKETGOODS, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserMarketGoodsCancel() {
        cancel(ConstantData.GETUSERMARKETGOODS);
    }


    public void getUserSelectExpress(int schoolId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            okgo(ConstantData.GETUSERSELECTEXPRESS, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserSelectExpressCancel() {
        cancel(ConstantData.GETUSERSELECTEXPRESS);
    }


    public void getUserSelectTaskType(int schoolId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            okgo(ConstantData.GETUSERSELECTTASKTYPE, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserSelectTaskTypeCancel() {
        cancel(ConstantData.GETUSERSELECTTASKTYPE);
    }


    public void getUserSelectTaskAddress(int schoolId, int addrTypeId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            object.put("addrTypeId", addrTypeId);
            okgo(ConstantData.GETUSERSELECTTASKADDRESS, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserSelectTaskAddressCancel() {
        cancel(ConstantData.GETUSERSELECTTASKADDRESS);
    }


    public void getSendedOrderList(int userId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("userId", userId);
            okgo(ConstantData.GETSENDEDORDERLIST, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getSendedOrderListCancel() {
        cancel(ConstantData.GETSENDEDORDERLIST);
    }


    public void getReceivedOrderList(int userId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("userId", userId);
            okgo(ConstantData.GETRECEIVEDORDERLIST, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getReceivedOrderListCancel() {
        cancel(ConstantData.GETRECEIVEDORDERLIST);
    }


    public void changePhoneOne(String userId, String userPhoneOne, String verifyCodeOne, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("userId", userId);
            object.put("userPhoneOne", userPhoneOne);
            object.put("verifyCodeOne", verifyCodeOne);

            okgo(ConstantData.CHANGEPHONEONE, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void changePhoneOneCancel() {
        cancel(ConstantData.CHANGEPHONEONE);
    }

    public void changePhoneTwo(String userId, String userPhoneTwo, String verifyCodeTwo, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("userId", userId);
            object.put("userPhoneTwo", userPhoneTwo);
            object.put("verifyCodeTwo", verifyCodeTwo);
            okgo(ConstantData.CHANGEPHONETWO, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void changePhoneTwoCancel() {
        cancel(ConstantData.CHANGEPHONETWO);
    }


    public void updateUser(int userId,
                           String userImg,
                           String userName,
                           String userSex,
                           String userAge,
                           Callback callBack) {
        try {

            com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
            object.put("userId", userId);
            object.put("userImg", userImg);
            object.put("userName", userName);
            object.put("userSex", userSex);
            object.put("userAge", userAge);


            OkGo.<String>post(ConstantData.UPDATEUSER).tag(ConstantData.UPDATEUSER).upJson(getRequestData(object).toJSONString()).execute(callBack);
//            okgo(ConstantData.UPDATEUSER, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void updateUserCancel() {
        cancel(ConstantData.UPDATEUSER);
    }


    public void myPurse(int userId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("userId", userId);
            okgo(ConstantData.MYPURSE, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void myPurseCancel() {
        cancel(ConstantData.MYPURSE);
    }

    //0关闭接单，1开启接单
    public void isOrderSwitch(int userId, int orderSwitch, String uuId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("userId", userId);
            object.put("orderSwitch", orderSwitch);
            object.put("uuId", uuId);
            okgo(ConstantData.ISORDERSWITCH, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void isOrderSwitchCancel() {
        cancel(ConstantData.ISORDERSWITCH);
    }

    public void queryMyInfostatus(int userId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("userId", userId);
            okgo(ConstantData.QUERYMYINFOSTATUS, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void queryMyInfostatusCancel() {
        cancel(ConstantData.QUERYMYINFOSTATUS);
    }


    public void getOrderDetailById(int orderId, int orderType, int orderFrom, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("orderId", orderId);
            object.put("orderType", orderType);
            object.put("orderFrom", orderFrom);
            okgo(ConstantData.GETORDERDETAILBYID, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getOrderDetailByIdCancel() {
        cancel(ConstantData.GETORDERDETAILBYID);
    }


    public void getUserTakeoutInfo(int schoolId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            okgo(ConstantData.GETUSERTAKEOUTINFO, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserTakeoutInfoCancel() {
        cancel(ConstantData.GETUSERTAKEOUTINFO);
    }


    public void getUserSupermarketInfo(int cityId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("cityId", cityId);
            okgo(ConstantData.GETUSERSUPERMARKETINFO, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserSupermarketInfoCancel() {
        cancel(ConstantData.GETUSERSUPERMARKETINFO);
    }


    public void getUserExpressInfo(int schoolId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            okgo(ConstantData.GETUSEREXPRESSINFO, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserExpressInfoCancel() {
        cancel(ConstantData.GETUSEREXPRESSINFO);
    }


    public void getUserTaskInfo(int schoolId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            okgo(ConstantData.GETUSERTASKINFO, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserTaskInfoCancel() {
        cancel(ConstantData.GETUSERTASKINFO);
    }


    public void cancelOrder(int orderId, String cancelReason, String cancelDetail, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("orderId", orderId);
            object.put("cancelReason", cancelReason);
            object.put("cancelDetail", cancelDetail);
            okgo(ConstantData.CANCELORDER, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void cancelOrderCancel() {
        cancel(ConstantData.CANCELORDER);
    }


    public void appraiseOrder(int orderId, String appraiseStar, String appraiseContent, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("orderId", orderId);
            object.put("appraiseStar", appraiseStar);
            object.put("appraiseContent", appraiseContent);
            okgo(ConstantData.APPRAISEORDER, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void appraiseOrderCancel() {
        cancel(ConstantData.APPRAISEORDER);
    }


    public void toRobOrder(int orderId, int userId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("orderId", orderId);
            object.put("userId", userId);
            okgo(ConstantData.TOROBORDER, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void toRobOrderCancel() {
        cancel(ConstantData.TOROBORDER);
    }


    public void toOrderFinish(int orderId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("orderId", orderId);
            okgo(ConstantData.TOORDERFINISH, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void toOrderFinishCancel() {
        cancel(ConstantData.TOORDERFINISH);
    }


    public void deliveredOrder(int orderId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("orderId", orderId);
            okgo(ConstantData.DELIVEREDORDER, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void deliveredOrderCancel() {
        cancel(ConstantData.DELIVEREDORDER);
    }


    public void toOrderFilter(String schoolId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            okgo(ConstantData.TOORDERFILTER, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void toOrderFilterCancel() {
        cancel(ConstantData.TOORDERFILTER);
    }

    public void toGetOrderFilter(String schoolId, String orderType, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            object.put("orderType", orderType);
            okgo(ConstantData.TOGETORDERFILTER, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void toGetOrderFilterCancel() {
        cancel(ConstantData.TOGETORDERFILTER);
    }


    public void getAcceptOrderList(String schoolId,
                                   String userId,
                                   String location,
                                   int pageIndex,
                                   String orderType,
                                   String getAddressId,
                                   String sendAddressId,
                                   String gettingAmountMin,
                                   String gettingAmountMax, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            object.put("userId", userId);
            object.put("location", location);
            object.put("pageIndex", pageIndex);
            object.put("orderType", orderType);
            object.put("getAddressId", getAddressId);
            object.put("sendAddressId", sendAddressId);
            object.put("gettingAmountMin", gettingAmountMin);
            object.put("gettingAmountMax", gettingAmountMax);
            okgo(ConstantData.GETACCEPTORDERLIST, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getAcceptOrderListCancel() {
        cancel(ConstantData.GETACCEPTORDERLIST);
    }

    public void setSchoolInfo(String userId, String schoolId, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            object.put("userId", userId);
            okgo(ConstantData.SETSCHOOLINFO, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void setSchoolInfoCancel() {
        cancel(ConstantData.SETSCHOOLINFO);
    }


    public void getUserStoreGoodsSeclect(int schoolId, String result, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            object.put("result", result);
            okgo(ConstantData.GETUSERSTOREGOODSSECLECT, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void getUserStoreGoodsSeclectCancel() {
        cancel(ConstantData.GETUSERSTOREGOODSSECLECT);
    }


    ///////////////
    private void okgo(String url, JSONObject object, Callback callBack) throws Exception {

        LogUtils.e(url);

        OkGo.<String>post(url).tag(url).upJson(getRequestData(object)).execute(callBack);
    }

    //插入订单 mqk
    /*
     *   sendAddressId	送餐地址Id
     *  storeId	商家Id
     *  remarkInfo	备注信息
     *  dinnerAmount	餐品金额
     *   shippingAmount	配送金额
     *   takeoutSubList	外卖明细
     *  schoolId	学校Id
     *   userId	用户Id
     *   orderType	订单类型
     *   goodListArray	商品列表
     */
    public void addNewOrderInfo(String schoolId, String sendAddressId, String storeId, String remarkInfo
            , double dinnerAmount, double shippingAmount, JSONArray takeoutSubList, String userId, int orderType,String expectedDeliveryTime, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("schoolId", schoolId);
            object.put("sendAddressId", sendAddressId);
            object.put("storeId", storeId);
            object.put("remarkInfo", remarkInfo);
            object.put("shippingAmount", shippingAmount);
            object.put("userId", userId);
            object.put("orderType", orderType);
            object.put("expectedDeliveryTime", expectedDeliveryTime);
            if (orderType == 1) {
                object.put("dinnerAmount", dinnerAmount);
                object.put("takeoutSubList", takeoutSubList);
            } else if (orderType == 2) {
                object.put("goodAmount", dinnerAmount);
                object.put("marketSubList", takeoutSubList);
            }
            okgo(ConstantData.ADD_NEW_ORDER_INFO, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void addNewOrderInfo_Express(String userId,
                                        String schoolId,
                                        String orderType,
                                        String sendAddressId,
                                        String expressNameId,
                                        String expressInfo,
                                        String receiveTimeRemark,
                                        String isUpstairs,
                                        String surplusAmount,
                                        String remarkInfo,
                                        String shippingAmount, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("userId", userId);
            object.put("schoolId", schoolId);
            object.put("orderType", orderType);
            object.put("sendAddressId", sendAddressId);
            object.put("expressNameId", expressNameId);
            object.put("expressInfo", expressInfo);
            object.put("receiveTimeRemark", receiveTimeRemark);
            object.put("isUpstairs", isUpstairs);
            object.put("surplusAmount", surplusAmount);
            object.put("remarkInfo", remarkInfo);
            object.put("shippingAmount", shippingAmount);
            okgo(ConstantData.ADD_NEW_ORDER_INFO, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void addNewOrderInfo_Task(String userId,
                                     String schoolId,
                                     String orderType,
                                     String taskType,
                                     String taskDescribe,
                                     String taskAddressId,
                                     String taskAddressDetail,
                                     String taskTelephone,
                                     String taskAmount,
                                     String taskStatus, Callback callBack) {
        try {
            JSONObject object = new JSONObject();
            object.put("userId", userId);
            object.put("schoolId", schoolId);
            object.put("orderType", orderType);

            object.put("taskType", taskType);
            object.put("taskDescribe", taskDescribe);
            object.put("taskAddressId", taskAddressId);
            object.put("taskAddressDetail", taskAddressDetail);
            object.put("taskTelephone", taskTelephone);
            object.put("taskAmount", taskAmount);
            object.put("taskStatus", taskStatus);
            okgo(ConstantData.ADD_NEW_ORDER_INFO, object, callBack);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(null);
        }
    }

    public void cancel(String url) {
        try {
            OkGo.getInstance().cancelTag(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
