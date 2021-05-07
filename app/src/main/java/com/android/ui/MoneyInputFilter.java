package com.android.ui;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Administrator on 2017/11/7.
 * use it like this
 * //设置Input的类型两种都要
 * editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
 * //设置字符过滤
 * editText.setFilters(new InputFilter[]{new MoneyInputFilter()});
 */

public class MoneyInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        String str = dest.toString();

        if (source.equals(".") && str.length() == 0) {
            return "0.";
        }

        int index = str.indexOf(".");
        if (index < 0) {
            if (str.length() >= 4) {
                return "";
            }
        } else {
            String temp = str.substring(0, index);

            int length = temp.length();
            if (dstart <= index && length >= 4) {
                return "";
            }
        }

        if (!str.contains(".")) {
            return null;
        }

        String temp = str.substring(index);
        int length = temp.length();
        if (dstart > index && length >= 3) {
            return "";
        }
        return null;
    }
}
