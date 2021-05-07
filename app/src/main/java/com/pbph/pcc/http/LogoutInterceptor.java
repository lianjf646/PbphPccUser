package com.pbph.pcc.http;


import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.utils.IOUtils;
import com.pbph.pcc.activity.LoginActivity;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.tools.ConstantData;
import com.utils.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import io.rong.imkit.RongIM;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;


public class LogoutInterceptor implements Interceptor {


    private static final Charset UTF8 = Charset.forName("UTF-8");

    private Context mContext;

    public LogoutInterceptor(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {


        Request request = chain.request();


        //请求前
        Response response = chain.proceed(request);
//        请求后
        byte[] bytes = buildResponse(response);

        MediaType contentType = response.body().contentType();

        ResponseBody responseBody = ResponseBody.create(contentType, bytes);

        response = response.newBuilder().body(responseBody).build();

///
        String body = new String(bytes, getCharset(contentType));

        //这个接口就不做这个判断了。
        if (StringUtils.isEqual(request.url().toString(), ConstantData.appUserLogin)) {
            return response;
        }
        if (StringUtils.isEqual(request.url().toString(), ConstantData.queryMyInfo)) {
            return response;
        }
 
        if (StringUtils.isEqual(getCode1010(body), "1010")) {

            PccApplication.setUserID("");
            RongIM.getInstance().logout();
            Intent loginActivityIntent = new Intent();
            loginActivityIntent.setClass(mContext, LoginActivity.class);
            loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            loginActivityIntent.putExtra("kickedByOtherClient", true);

            mContext.startActivity(loginActivityIntent);

            throw new IOException();

//            responseBody = ResponseBody.create(contentType, new byte[]{});
//            response = response.newBuilder().body(responseBody).build();
        }

        return response;
    }


    private String getCode1010(String str) {

        if (StringUtils.isEmpty(str)) return null;

        JSONObject jsonObject = JSON.parseObject(str);

        return jsonObject.getString("code");
    }

    private byte[] buildResponse(Response response) throws IOException {

        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        ResponseBody responseBody = clone.body();

        if (!HttpHeaders.hasBody(clone)) return null;

        if (!isPlaintext(responseBody.contentType())) return null;

        if (responseBody == null) return null;

        byte[] bytes = IOUtils.toByteArray(responseBody.byteStream());

        return bytes;
    }

    private static boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) return false;
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        String subtype = mediaType.subtype();
        if (subtype != null) {
            subtype = subtype.toLowerCase();
            if (subtype.contains("x-www-form-urlencoded") || subtype.contains("json") || subtype.contains("xml") || subtype.contains("html")) //
                return true;
        }
        return false;
    }

    private static Charset getCharset(MediaType contentType) {
        Charset charset = contentType != null ? contentType.charset(UTF8) : UTF8;
        if (charset == null) charset = UTF8;
        return charset;
    }

}
