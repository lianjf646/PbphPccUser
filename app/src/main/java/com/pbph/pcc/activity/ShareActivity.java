package com.pbph.pcc.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pbph.pcc.R;
import com.pbph.pcc.tools.BitmapUtil;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.wxutil.WechatUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * 分享
 * Created by Administrator on 2017/10/9.
 */

public class ShareActivity extends BaseActivity implements View.OnClickListener, PopupWindow.OnDismissListener {
    private View mHeardLinear;
    private ImageView mShareFinish;
    private TextView mTitle;
    private Button mBtnShare;
    private PopupWindow mSharePopup;
    private View mView;
    private TextView mWx, mFriend, mQQZ, mQQ, mShareDismiss;
    private WindowManager.LayoutParams lp;
    private Tencent mTencent;
    private BaseUiListener listener;
    private String tencent_APP_ID = "1106389657";
    private ImageView iv_share;
    private ImageView qRCodeIv;
    private Bitmap shareBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
// 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(tencent_APP_ID, this.getApplicationContext());
// 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
// 初始化视图
        listener = new BaseUiListener();
        initView();
        initPopup();
        initData();
    }


    private void initView() {
        iv_share = (ImageView) findViewById(R.id.iv_share);
        mHeardLinear = findViewById(R.id.share_include_title);
        mTitle = mHeardLinear.findViewById(R.id.tv_title);
        mShareFinish = mHeardLinear.findViewById(R.id.btn_left);
        qRCodeIv = (ImageView) findViewById(R.id.qr_code_iv);
        mShareFinish.setOnClickListener(this);
        iv_share.setOnClickListener(this);
    }

    private void initPopup() {
        mView = LayoutInflater.from(ShareActivity.this).inflate(R.layout.pupup_share, null);
        mSharePopup = new PopupWindow(this);
        mSharePopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mSharePopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mSharePopup.setContentView(mView);
        mSharePopup.setBackgroundDrawable(new ColorDrawable(0xffffff));
        mSharePopup.setFocusable(true);
        mSharePopup.setOnDismissListener(this);
        mWx = mView.findViewById(R.id.tv_share_wx);
        mFriend = mView.findViewById(R.id.tv_share_friend);
        mQQZ = mView.findViewById(R.id.tv_share_qqZ);
        mQQ = mView.findViewById(R.id.tv_share_qq);
        mShareDismiss = mView.findViewById(R.id.tv_share_dismiss);
        mWx.setOnClickListener(this);
        mFriend.setOnClickListener(this);
        mQQZ.setOnClickListener(this);
        mQQ.setOnClickListener(this);
        mShareDismiss.setOnClickListener(this);
    }

    /**
     * 显示popup
     *
     * @param view
     */
    private void showPopup(View view) {
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        mSharePopup.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mSharePopup.showAsDropDown(view);
    }

    private void initData() {
        mTitle.setText("分享");
        lp = getWindow().getAttributes();
        Glide.with(this).load(ConstantData.SHARE_QR_CODE).error(R.mipmap.banner_zw).into(qRCodeIv);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                shareBitmap = BitmapUtil.getBitmapFromUrl(ConstantData.SHARE_WX_IMG);
            }
        }).start();

    }

    @Override
    public void onDismiss() {
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.iv_share:
                showPopup(view);
                break;
            case R.id.tv_share_wx:
                mSharePopup.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (shareBitmap != null) {
                            WechatUtils.getInstance(ShareActivity.this).shareImages(shareBitmap, SendMessageToWX.Req.WXSceneSession);
                        }
                    }
                }).start();
                break;
            case R.id.tv_share_friend:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (shareBitmap != null) {
                            WechatUtils.getInstance(ShareActivity.this).shareImages(shareBitmap, SendMessageToWX.Req.WXSceneTimeline);
                        }
                    }
                }).start();
                mSharePopup.dismiss();
                break;
            case R.id.tv_share_qqZ:
                shareToQzone();
                mSharePopup.dismiss();
                break;
            case R.id.tv_share_qq:
                shareToQQ();
                mSharePopup.dismiss();
                break;
            case R.id.tv_share_dismiss:
                mSharePopup.dismiss();
                break;
        }
    }

    public void shareToQQ() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "小阿光");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, ConstantData.SHARE_URL);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ConstantData.SHARE_LOGO);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "小阿光");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  "其他附加功能");
        mTencent.shareToQQ(this, params, listener);
//        Bundle params = new Bundle();
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,imageUrl.getText().toString());
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "小阿光");
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
//        mTencent.shareToQQ(ShareActivity.this, params, new BaseUiListener());
    }

    public void shareToQzone() {
        //分享类型
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "小阿光");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, ConstantData.SHARE_URL);//必填
        ArrayList dd = new ArrayList();
        dd.add(ConstantData.SHARE_LOGO);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, dd);
        mTencent.shareToQzone(this, params, listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Toast.makeText(application, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(application, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(application, "取消分享", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            Toast.makeText(ShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError e) {
            Toast.makeText(ShareActivity.this, "分享失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(ShareActivity.this, "取消分享", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        if (shareBitmap != null) {
            shareBitmap.recycle();
        }
        super.onDestroy();

    }
}
