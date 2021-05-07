package com.pbph.pcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.request.GetOosTokenRequestBean;
import com.pbph.pcc.bean.response.GetOosTokenResponseBean;
import com.pbph.pcc.bean.MyUserInfo;
import com.pbph.pcc.bean.response.QueryAgentBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.AliOss;
import com.pbph.pcc.tools.BitmapUtil;
import com.pbph.pcc.tools.WaitUI;
import com.utils.GlideImageLoaderUntil;
import com.utils.StringUtils;

import java.util.ArrayList;

/**
 * 申请代理
 * Created by Administrator on 2017/10/16.
 */

public class AgencyApplyActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_apply_name, tv_apply_sex, tv_apply_school, tv_apply_schoolno,
            tv_apply_phone, tv_apply_add_pcc;
    private PccApplication mApp;
    private QueryAgentBean mQueryAgentBean;

    private ImageView iv_apply_photo;

    private byte[] mHeardIcon;
    private ImagePicker imagePicker;
    private int maxImgCount = 1;               //允许选择图片最大数
    private ArrayList<ImageItem> images = null;//当前选择的所有图片
    private String authStudenCardImg = "";

    private View mHeardView;
    private ImageView mAgencyFinish;
    private TextView mAgencyTitle;
    private TextView tv_toast;

    private String aLiToken = "";
    MyUserInfo bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agencyapply);
        initView();
        initData();
    }

    private void initView() {
        tv_apply_name = (TextView) findViewById(R.id.tv_apply_name);
        tv_apply_sex = (TextView) findViewById(R.id.tv_apply_sex);
        tv_apply_school = (TextView) findViewById(R.id.tv_apply_school);
        tv_apply_schoolno = (TextView) findViewById(R.id.tv_apply_schoolno);
        tv_apply_phone = (TextView) findViewById(R.id.tv_apply_phone);
        iv_apply_photo = (ImageView) findViewById(R.id.iv_apply_photo);
        tv_apply_add_pcc = (TextView) findViewById(R.id.tv_apply_add_pcc);
        tv_toast = (TextView) findViewById(R.id.tv_toast);
        mHeardView = findViewById(R.id.include_title);
        mAgencyFinish = mHeardView.findViewById(R.id.btn_left);
        mAgencyTitle = mHeardView.findViewById(R.id.tv_title);


        iv_apply_photo.setOnClickListener(this);
        tv_apply_add_pcc.setOnClickListener(this);
        mAgencyFinish.setOnClickListener(this);
    }

    private void initData() {
        mApp = (PccApplication) getApplication();
        bean = application.getMyInfoData();
        if (null == bean) return;
        if (bean.getUserStatus() == 4) {
            tv_apply_add_pcc.setVisibility(View.GONE);
            tv_toast.setVisibility(View.GONE);
            iv_apply_photo.setClickable(false);
        }
        mAgencyTitle.setText("申请代理");
        initImagePager();
        getNetworkData();
    }

    private void initImagePager() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoaderUntil());
    }


    /**
     * 获取信息
     */
    public void getNetworkData() {
        WaitUI.Show(AgencyApplyActivity.this);
        HttpAction.getInstance().queryAgent(PccApplication.getUserid(), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    WaitUI.Cancel();
                    getOssToken();
                    mQueryAgentBean = JsonMananger.jsonToBean(response.body(), QueryAgentBean
                            .class);
                    if (mQueryAgentBean.getCode().equals("200") && mQueryAgentBean.getData() !=
                            null) {
                        tv_apply_name.setText(mQueryAgentBean.getData().getAuthName());
                        tv_apply_phone.setText(mQueryAgentBean.getData().getAuthPhone());
                        tv_apply_schoolno.setText(mQueryAgentBean.getData().getAuthStudentId());
                        tv_apply_sex.setText(mQueryAgentBean.getData().getAuthSex().equals("0") ?
                                "男" : "女");
                        tv_apply_school.setText(mApp.getMyInfoData().getSchoolName());
                        if (mQueryAgentBean.getData().getAuthIdCardImg().equals("")) {
                            return;
                        } else {
//                            iv_apply_photo.setClickable(false);
//                            tv_apply_add_pcc.setVisibility(View.GONE);
                            Glide.with(AgencyApplyActivity.this).load(mQueryAgentBean.getData()
                                    .getAuthIdCardImg()).into(iv_apply_photo);

                        }

                    } else {
                        Toast.makeText(mApp, mQueryAgentBean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                WaitUI.Cancel();

            }
        });
    }

    /**
     * 获取阿里云token
     */
    private void getOssToken() {
        WaitUI.Show(AgencyApplyActivity.this);
        GetOosTokenRequestBean bean = new GetOosTokenRequestBean();
        HttpAction.getInstance().getOssToken(bean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                WaitUI.Cancel();
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(AgencyApplyActivity.this, R.string.connect_error, Toast
                                .LENGTH_SHORT).show();
                        return;
                    }
                    LogUtils.e("res=  " + response.body());
                    GetOosTokenResponseBean bean = JsonMananger.jsonToBean(response.body(),
                            GetOosTokenResponseBean.class);
                    if (StringUtils.isEqual(bean.getCode(), "200")) {
                        aLiToken = bean.getData().getSecurityToken();
                        AliOss.getInstance().oosConfig(getApplicationContext(), bean.getData()
                                .getAccessKeyId(), bean.getData().getAccessKeySecret(), bean
                                .getData().getSecurityToken());
                    }

                } catch (Exception e) {
                    Toast.makeText(AgencyApplyActivity.this, R.string.connect_error, Toast
                            .LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void uplodaImage(final byte[] imagebytes) {
        if (aLiToken.equals("")) {
            return;
        }
        WaitUI.Show(this);
        new Thread() {
            @Override
            public void run() {
                AliOss.getInstance().uploadImage(System.currentTimeMillis() + ".jpg", imagebytes,
                        new AliOss.OnOosUploadListener() {
                            @Override
                            public void onSuccess(String path) {
                                LogUtils.e("path=  " + path);
                                authStudenCardImg = path;
                                WaitUI.Cancel();
                            }

                            @Override
                            public void onFailure(String msg) {
                                LogUtils.e("msg=  " + msg);
                            }
                        });
            }
        }.start();
    }

    private void getImageInfo() {
        imagePicker.setShowCamera(true);
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
//                Integer width = (int) (ScreenUtils.getScreenWidthdp(AddStoreTfAc.this) );
//                Integer height = (int) (ScreenUtils.getScreenWidthdp(AddStoreTfAc.this) * 9 /
// 16 );
        Integer width = 400;
        Integer height = 280;
        LogUtils.d("width:" + width);
        LogUtils.d("height:" + height);
//                int a = (int) (ScreenUtils.getScreenWidth(AddStoreTfAc.this) / density);
//                int b = (int) (ScreenUtils.getScreenWidth(AddStoreTfAc.this) * 9 / 16 / density);
//                Log.d("AddStoreTfAc", "a:" + a);
//                Log.d("AddStoreTfAc", "b:" + b);
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources
                ().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height,
                getResources().getDisplayMetrics());
        LogUtils.d("width:" + width);
        LogUtils.d("height:" + height);
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(height);
        imagePicker.setOutPutX(Integer.valueOf("400"));
        imagePicker.setOutPutY(Integer.valueOf("280"));
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
        //ImagePicker.getInstance().setSelectedImages(images);
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker
                        .EXTRA_RESULT_ITEMS);
//                imagePicker.getImageLoader().displayImage(CertificationActivity.this, images
// .get(0).path, mPhoto, 60, 60);
                Glide.with(AgencyApplyActivity.this).load(images.get(0).path).error(R.mipmap
                        .banner_zw).into(iv_apply_photo);
                mHeardIcon = BitmapUtil.bitmapToByteArray(images.get(0).path);

                uplodaImage(mHeardIcon);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_apply_photo:
                getImageInfo();
                break;
            case R.id.tv_apply_add_pcc:
                if (authStudenCardImg.equals("")) {
                    Toast.makeText(mApp, "请选择照片", Toast.LENGTH_SHORT).show();
                    return;
                }
                upLoadNetworkImage();
                break;
            case R.id.btn_left:
                finish();
                break;
        }
    }

    /**
     * 上传图片
     */
    private void upLoadNetworkImage() {
        WaitUI.Show(AgencyApplyActivity.this);
        HttpAction.getInstance().applyAgent(PccApplication.getUserid(), authStudenCardImg, new
                StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        WaitUI.Cancel();
                        if (response.body().contains("200")) {
                            Toast.makeText(mApp, "提交成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(mApp, "申请代理失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        WaitUI.Cancel();
                        LogUtils.e("onError: " + response.code());
                    }
                });

    }
}
