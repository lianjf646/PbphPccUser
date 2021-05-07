package com.pbph.pcc.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.pbph.pcc.bean.MyUserInfo;
import com.pbph.pcc.bean.response.QueryRongYunResponseBean;
import com.pbph.pcc.db.DaoMaster;
import com.pbph.pcc.db.DaoSession;
import com.pbph.pcc.http.LogoutInterceptor;
import com.pbph.pcc.rong.SealAppContext;
import com.pbph.pcc.tools.ConstantData;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imkit.utils.SystemUtils;
import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.push.RongPushClient;
import okhttp3.OkHttpClient;


public class PccApplication extends Application {

    //sp
    private final static String SP_FILE_NAME_DATA = "data";
    public static SharedPreferences mSharedPreferences = null;
    //sqlite
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private static DaoSession daoSession;

    ///////////////////////
    //每次翻页获取数据的条数--by gdl
    public static final int PAGE_COUNT = 20;
    public static final int PAGE_COUNT_10 = 10;

    //用于界面间数据透传--by gdl key the Class.getname value the data you want
    private static Map<String, Object> dataMap = new HashMap();

    ///////////////////
    //机器码
    private static String imei = "";
    //    极光

    //融云id和 融云信息
    public static final String RONGYUN_CUSTOMERSERVICEID = "KEFU151062899840463";
    public QueryRongYunResponseBean queryRongYunResponseBean = null;

    //    用户信息
    private MyUserInfo myInfoData = null;

    //定位信息
    public static MyLocationInfo locationInfo = new MyLocationInfo();

    //http请求数据data
    public static HttpRequestData httpRequestData;

    //    设置
    public static SettingData settingData;

    @Override
    public void onCreate() {
        super.onCreate();
        if (getApplicationInfo().packageName.equals(SystemUtils.getCurProcessName(getApplicationContext()))) {
            JPushInterface.setDebugMode(true);
            JPushInterface.init(this);
            mSharedPreferences = getSharedPreferences(SP_FILE_NAME_DATA, MODE_PRIVATE);
            httpRequestData = new HttpRequestData();
            settingData = new SettingData();
            createFolder();
            RongIM.init(this);
            RongPushClient.init(this, "e5t4ouvpeqsna");
            SealAppContext.init(this);
            Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));
            setupDatabase();
//            SDKInitializer.initialize(this);
            initOkGo();
        }
    }

    ///////////////
    private void setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "com.pbph.pcc.db", null);
        db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    ///////////////////
    private void createFolder() {
        File file = new File(ConstantData.PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        File down = new File(ConstantData.DOWN_LOAD_PATH);
        if (!down.exists()) {
            down.mkdirs();
        }
    }

    /////////////////////
    public static Object getDataMapData(String key) {
        Object obj = dataMap.get(key);
        dataMap.remove(key);
        return obj;
    }

    public static void setDataMapData(String key, Object val) {
        dataMap.put(key, val);
    }
///////////////////////

    public MyUserInfo getMyInfoData() {
        return myInfoData;
    }


    public void setMyInfoData(MyUserInfo myInfoData) {
        this.myInfoData = myInfoData;
    }

    public static String getImei() {
        return imei;
    }

    public static void setImei(String imei) {
        PccApplication.imei = imei;
    }

    //////////////////////////////
    private final static String USERID = "userid";
    private final static String USERNAME = "username";
    private final static String CITYID = "cityId";

    public static String getUserid() {
//        return "23";
        return mSharedPreferences.getString(USERID, "");
    }

    public static void setUserID(String userid) {
        mSharedPreferences.edit().putString(USERID, userid).commit();
    }


    public String getUsername() {
        return mSharedPreferences.getString(USERNAME, "");
    }

    public void setUsername(String username) {
        mSharedPreferences.edit().putString(USERNAME, username).commit();
    }


    public int getCityId() {
        return mSharedPreferences.getInt(CITYID, 1);
    }

    public void setCityId(int cityId) {
        mSharedPreferences.edit().putInt(CITYID, cityId).commit();
    }


    private void initOkGo() {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));
        builder.addInterceptor(new LogoutInterceptor(this));


        //超时时间设置，默认60秒
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

//        //https相关设置，以下几种方案根据需要自己设置
//        //方法一：信任所有证书,不安全有风险
//        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
//        //方法二：自定义信任规则，校验服务端证书
//        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
//        //方法三：使用预埋证书，校验服务端证书（自签名证书）
//        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
//        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
//        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
//        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(1)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数
    }

}
