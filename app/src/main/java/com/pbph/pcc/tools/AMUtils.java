package com.pbph.pcc.tools;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AMUtils {

    /**
     * 手机号正则表达式
     *
     *
     **/
//    public final static String MOBLIE_PHONE_PATTERN = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[7])|(17[0-9]))\\d{8}$";
    public final static String MOBLIE_PHONE_PATTERN = "^1[0-9]\\d{9}$";


    /**
     * 通过正则验证是否是合法手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isMobile(String phoneNumber) {
        Pattern p = Pattern.compile(MOBLIE_PHONE_PATTERN);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    /**
     * 通过正则验证是否是合法手机号码
     *
     * @param yzmStr
     * @return
     */
    public static boolean isYZm(String yzmStr) {
        if (StringUtils.isEmpty(yzmStr)) return false;
        if (yzmStr.length() != 6) return false;
        return StringUtils.isNumber(yzmStr);
    }


    public static void onInactive(Context context, EditText et) {

        if (et == null)
            return;

        et.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * @param context
     * @param et
     */
    public static void onActive(Context context, EditText et) {
        if (et == null)
            return;

        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, 0);

    }

    /**
     * 通过验证
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 验证editText里是否有表情
     *
     * @param content
     * @return
     */

    public static boolean hasEmoji(String content) {

        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {

            return true;
        }
        return false;
    }


    //验证身份证号码
    public static boolean idCardNumber(String number) {
        String rgx = "^\\d{15}|^\\d{17}([0-9]|X|x)$";

        return isCorrect(rgx, number);
    }

    //正则验证
    public static boolean isCorrect(String rgx, String res) {
        Pattern p = Pattern.compile(rgx);

        Matcher m = p.matcher(res);

        return m.matches();
    }

}
