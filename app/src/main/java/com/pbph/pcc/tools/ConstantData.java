package com.pbph.pcc.tools;

import android.os.Environment;

public class ConstantData {
    public final static int CAMERA = 0x1005;
    public final static int PHOTO = 0x1006;
    public final static int CROP = 0x1007;
    public final static int DOWN_LOAD_NO_FILE = 0x400;
    public final static String PATH = Environment.getExternalStorageDirectory()
            .toString() + "/pbph_pcc/";
    public final static String DOWN_LOAD_PATH = Environment
            .getExternalStorageDirectory().toString() + "/pbph_pcc/down/";
    public static final String gengXinUrl = "http://www.pbph.com.cn:9001/pcc/version.xml";
    public static final String WX_APP_ID = "wxe0766ba1744b0780";
    public static final String APP_SECRET = "8698f22a9e144e222a37fac8c0ca1cbb";
    public final static String POST = "POST";
    public final static String GET = "GET";

//    public final static String HOST = "http://pshop.pbphkj.com/xagservertest/";
    public final static String HOST = "http://www.pcc58.com/appserver/";


    //    public final static String SHARE_URL = "http://pshop.pbphkj.com/agent/pccDownload/download.html";
    public final static String SHARE_URL = "http://www.pcc58.com/xagagent/pccDownload/download.html";

    public final static String SHARE_QR_CODE = "http://www.pbph.com.cn:9001/pcc/code.png";
    public final static String SHARE_LOGO = "http://www.pbph.com.cn:9001/pcc/logo.png";
    public final static String SHARE_WX_IMG = "http://www.pbph.com.cn:9001/pcc/wechat_share_image.png";
    public final static String MAIN_BANNER_ZANWEI = "http://www.pbph.com.cn:9001/pcc/banner_def.jpeg";

    //    public final static String HOST = "http://192.168.21.119/pcc_web/";
    public final static String GET_UNIONID_ACTION = "get_unionid_action";
    public final static String REFRESH_BANNER_ACITON = "refresh_banner_aciton";
    public final static String ADD_MESSAGE_ACTION = "add_message_action";
    public final static String REFRESH_MESSAGE_LIST_ACITON = "refresh_message_list_aciton";
    public final static String REFRESH_UNREAD_MESSAGE_ACITON = "refresh_unread_message_aciton";
    public final static String UP_DATA_IMAGE = " com.pbph.pcc.activity.updata_image";
    public final static String sendSmsValCode = HOST + "sendSmsValCode.htm";
    public final static String appUserLogin = HOST + "appUserLogin.htm";
    public final static String queryMyInfo = HOST + "queryMyInfo.htm";
    public final static String getOssToken = HOST + "getOssToken.htm";
    public final static String queryTakeAddress = HOST + "queryTakeAddress.htm";
    public final static String myTakeAddress = HOST + "myTakeAddress.htm";
    public final static String noticeWeb = HOST + "noticeWeb.htm";
    public final static String activeWeb = HOST + "activeWeb.htm";
    public final static String addTakeAddress = HOST + "addTakeAddress.htm";
    public final static String delTakeAddressByAddId = HOST + "delTakeAddressByAddId.htm";
    public final static String updateTakeAddress = HOST + "updateTakeAddress.htm";
    public final static String getJpushId = HOST + "getJpushId.htm";
    public final static String figureWeb = HOST + "figureWeb.htm";
    public final static String defaultAddress = HOST + "defaultAddress.htm";
    public final static String identification = HOST + "identification.htm";
    public final static String queryRongYun = HOST + "queryRongYun.htm";
    public final static String queryAgent = HOST + "queryAgent.htm";
    public final static String applyAgent = HOST + "applyAgent.htm";
    public final static String getAppAuthorize = HOST + "getAppAuthorize.htm";
    public final static String sendRed = HOST + "sendRed.htm";
    public final static String messageWeb = HOST + "messageWeb.htm";
    public final static String messageRead = HOST + "messageRead.htm";
    public final static String updateStatusWeb = HOST + "updateStatusWeb.htm";


    public final static String WXPAY_RESULT = "PCC_WXPAY_RESULT";
    public final static String REFRESH_SHOPPING_CAR = "REFRESH_SHOPPING_CAR";
    // by gdl------------------
    public final static String QUERYSCHOOL = HOST + "querySchool.htm";

    public final static String QUERYNOTOPENSCHOOL = HOST + "queryNotOpenSchool.htm";


    public final static String GETUSERSTORE = HOST + "getUserStore.htm";

    public final static String GETUSERSTOREINFO = HOST + "getUserStoreInfo.htm";

    public final static String GETUSERSTOREGOODS = HOST + "getUserStoreGoods.htm";

    public final static String GETUSERMARKETGOODS = HOST + "getUserMarketGoods.htm";

    public final static String GETUSERSELECTEXPRESS = HOST + "getUserSelectExpress.htm";

    public final static String GETUSERSELECTTASKTYPE = HOST + "getUserSelectTaskType.htm";

    public final static String GETUSERSELECTTASKADDRESS = HOST + "getUserSelectTaskAddress.htm";

    public final static String GETUSERSTOREGOODSSECLECT = HOST + "getUserStoreGoodsSeclect.htm";
    //插入订单 mqk
    public final static String ADD_NEW_ORDER_INFO = HOST + "addNewOrderInfo.htm";

    public final static String GETRECEIVEDORDERLIST = HOST + "getReceivedOrderList.htm";

    public final static String GETSENDEDORDERLIST = HOST + "getSendedOrderList.htm";

    public final static String CHANGEPHONEONE = HOST + "changePhoneOne.htm";

    public final static String CHANGEPHONETWO = HOST + "changePhoneTwo.htm";

    public final static String UPDATEUSER = HOST + "updateUser.htm";

    public final static String MYPURSE = HOST + "myPurse.htm";

    public final static String ISORDERSWITCH = HOST + "isOrderSwitch.htm";

    public final static String GETORDERDETAILBYID = HOST + "getOrderDetailById.htm";

    public final static String GETUSERTAKEOUTINFO = HOST + "getUserTakeoutInfo.htm";

    public final static String GETUSERSUPERMARKETINFO = HOST + "getUserSupermarketInfo.htm";

    public final static String GETUSEREXPRESSINFO = HOST + "getUserExpressInfo.htm";

    public final static String GETUSERTASKINFO = HOST + "getUserTaskInfo.htm";

    public final static String CANCELORDER = HOST + "cancelOrder.htm";

    public final static String APPRAISEORDER = HOST + "appraiseOrder.htm";

    public final static String TOROBORDER = HOST + "toRobOrder.htm";

    public final static String TOORDERFINISH = HOST + "toOrderFinish.htm";

    public final static String DELIVEREDORDER = HOST + "deliveredOrder.htm";

    public final static String TOORDERFILTER = HOST + "toOrderFilter.htm";

    public final static String TOGETORDERFILTER = HOST + "toGetOrderFilter.htm";

    public final static String GETACCEPTORDERLIST = HOST + "getAcceptOrderList.htm";

    public final static String SETSCHOOLINFO = HOST + "setSchoolInfo.htm";

    public final static String QUERYMYINFOSTATUS = HOST + "queryMyInfostatus.htm";

}
