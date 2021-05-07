package com.pbph.pcc.wxutil;

import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.pbph.pcc.R;
import com.pbph.pcc.tools.BitmapUtil;
import com.pbph.pcc.tools.ConstantData;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/9.
 */

public class WechatUtils {
    private static final int THUMB_SIZE = 150;
    public static final String TAG = "WechatUtils"; // 日志输出类名。
    private Context context;
    private IWXAPI wxApi; // IWXAPI 是第三方app和微信通信的openapi接口
    private String APP_ID = ConstantData.WX_APP_ID;
    private static WechatUtils instance = null;

    public static WechatUtils getInstance(Context context) {
        if (null == instance) {
            instance = new WechatUtils(context);
        }
        return instance;
    }

    private WechatUtils(Context context) {
        this.context = context;
        wxApi = WXAPIFactory.createWXAPI(context, APP_ID, false);
        wxApi.registerApp(APP_ID);
    }

    public void shareImages(Bitmap bmp, int type) {
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.title = "";
        msg.description = "";
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        msg.thumbData = bitmap2Bytes(thumbBmp, 31);
        bmp.recycle();
        thumbBmp.recycle();
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = type;
        wxApi.sendReq(req);
    }

    public void shareImages(String path, int type) {
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.title = "苗大大";
        msg.description = "每天看一看，精神一整天，每天都参与，财富在身边！";
        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = type;
        wxApi.sendReq(req);
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb && options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        return output.toByteArray();
    }

    public void shareImages() {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putExtra("Kdescription", "");
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
//        for (File f : files) {
//            imageUris.add(Uri.fromFile(f));
//        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        context.startActivity(intent);
    }

    public void shareWebPage(String url, int type) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "小阿光";
        msg.description = "我们不是跑单，而是跑腿";
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, THUMB_SIZE, THUMB_SIZE, true);
        thumb.recycle();
        msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
//        req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        wxApi.sendReq(req);
    }


    /*
     * 复制内容到剪切板
     */
    public void copyPhone(String text) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(context, "已复制到剪贴板…", Toast.LENGTH_LONG).show();
            copy(text, context); // 把关键字存入剪切板
        }
    }


    @SuppressWarnings("deprecation")
    @TargetApi(11)
    public void copy(String text, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(text.trim());
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
