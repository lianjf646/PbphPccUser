/*
    ShengDao Android Client, JsonMananger
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.pbph.pcc.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.android.utils.LogUtils;

import java.util.List;

public class JsonMananger {

    static {
        TypeUtils.compatibleWithJavaBean = true;
    }

    private static final String tag = JsonMananger.class.getSimpleName();

    /**
     * 将json字符串转换成java对象
     *
     * @param json
     * @param cls
     * @return
     * @throws
     */
    public static <T> T jsonToBean(String json, Class<T> cls) throws Exception {
        return JSON.parseObject(json, cls);
    }

    /**
     * 将json字符串转换成java List对象
     *
     * @param json
     * @param cls
     * @return
     * @throws Exception
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) throws Exception {
        return JSON.parseArray(json, cls);
    }

    /**
     * 将bean对象转化成json字符串
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String beanToJson(Object obj) throws Exception {
        String result = JSON.toJSONString(obj);
        LogUtils.e( "beanToJson: " + result);
        return result;
    }

}
