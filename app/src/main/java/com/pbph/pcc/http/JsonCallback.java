/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pbph.pcc.http;


import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;
import com.pbph.pcc.json.JsonMananger;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Class<T> clazz;

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return null;

        String s = new StringConvert().convertResponse(response);
        T data = JsonMananger.jsonToBean(s, clazz);

        return data;
    }
}
