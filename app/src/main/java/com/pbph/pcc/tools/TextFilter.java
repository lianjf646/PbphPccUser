package com.pbph.pcc.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/13 0013.
 */

public class TextFilter {
    public static boolean filter(String regEx ,String str){
//        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean b=  m.find();
        return b;
    }
}
