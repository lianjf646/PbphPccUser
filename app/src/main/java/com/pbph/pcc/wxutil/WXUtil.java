package com.pbph.pcc.wxutil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.android.utils.LogUtils;
import com.pbph.pcc.BuildConfig;
import com.pbph.pcc.R;
import com.pbph.pcc.tools.BitmapUtil;
import com.pbph.pcc.tools.ConstantData;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import junit.framework.Assert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class WXUtil {
    private static WXUtil util = null;
    private IWXAPI api;
    private Context context;

    public static WXUtil getInstance(Context context) {
        if (null == util) {
            util = new WXUtil(context);
        }
        return util;
    }

    private WXUtil(Context context) {
        this.context = context;
        api = WXAPIFactory.createWXAPI(context, ConstantData.WX_APP_ID, false);
        api.registerApp(ConstantData.WX_APP_ID);
    }

    public void weiXinLogin() {
        api = WXAPIFactory.createWXAPI(context, ConstantData.WX_APP_ID, false);
        api.registerApp(ConstantData.WX_APP_ID);
        if (api.isWXAppInstalled()) {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "toutoule";
            api.sendReq(req);

        } else {
        }
    }

    public void shareImageBitmap(final Bitmap bitmap, int type) {
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.title = context.getResources().getString(R.string.app_name);
        msg.description = "";
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        LogUtils.e("thumbBmp count= " + thumbBmp.getByteCount());
        bitmap.recycle();
        msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);  // 设置缩略图
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = type;
        api.sendReq(req);
    }

    public void shareImageFile(String path, int type) {
        LogUtils.e("shareImage" + path);
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);
    }


    public void shareImage(String path, int type) {
        LogUtils.e("shareImage" + path);
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.title = context.getResources().getString(R.string.app_name);
        msg.description = "";
        Bitmap bmp = BitmapFactory.decodeFile(path);

//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 120, 120, true);
//        thumbBmp.getByteCount();
        Bitmap thumbBmp = BitmapUtil.resizeImage(path, 120, 120);
        LogUtils.e("thumbBmp count= " + thumbBmp.getByteCount());
        bmp.recycle();
        msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = type;
        api.sendReq(req);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void shareToFriend(File file, String type) {
        try {
            Intent intent = new Intent();
            ComponentName comp = new ComponentName("com.tencent.mm", "0".equals(type) ? "com.tencent.mm.ui.tools.ShareImgUI" : "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.setComponent(comp);
            intent.setAction("android.intent.action.SEND");
            intent.setType("image/*");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void shareTofriendsCircle(File file) {
        try {
            Intent intent = new Intent();
            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
            LogUtils.e("Circle path  " + file.getPath());
            ArrayList<Uri> imageUris = new ArrayList<Uri>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
                imageUris.add(contentUri);
            } else {
                imageUris.add(Uri.fromFile(file));
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            context.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendtoImage(String text) {
        WXImageObject im = new WXImageObject();
        WXMediaMessage mg = new WXMediaMessage();
        mg.title = "";

    }


    public void sendtoText(String text) {

        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;

        msg.title = "Will be ignored";
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

    }


    public void sendWebPage(String url, String title, String description) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        msg.thumbData = WXUtil.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);

        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);

        return data;
    }

    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            LogUtils.i("readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        LogUtils.e("readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

        if (offset < 0) {
            LogUtils.e("readFromFile invalid offset:" + offset);
            return null;
        }
        if (len <= 0) {
            LogUtils.e("readFromFile invalid len:" + len);
            return null;
        }
        if (offset + len > (int) file.length()) {
            LogUtils.e("readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // ���������ļ���С������
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            LogUtils.e("readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
            }

            LogUtils.d("extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            LogUtils.d("extractThumbNail: extract beX = " + beX + ", beY = " + beY);
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            LogUtils.i("bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                LogUtils.e("bitmap decode failed");
                return null;
            }

            LogUtils.i("bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
                LogUtils.i("bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            LogUtils.e("decode bitmap failed: " + e.getMessage());
        }

        return null;
    }
}
