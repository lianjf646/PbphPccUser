package com.pbph.pcc.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.OnSingleClickListener;
import com.android.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.request.GetOosTokenRequestBean;
import com.pbph.pcc.bean.response.BaseResponseBean;
import com.pbph.pcc.bean.response.GetOosTokenResponseBean;
import com.pbph.pcc.broadcast.BroadcastManager;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.AliOss;
import com.pbph.pcc.tools.BitmapUtil;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.WaitUI;
import com.utils.GlideImageLoaderUntil;
import com.utils.StringUtils;

import java.util.ArrayList;


/**
 * 用户信息
 */
public class UpdatePersonInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private PccApplication application;
    private Context context = this;
    private View mView;
    private ImageView mIvFinish;
    private TextView mTitle;
    private TextView mSex;//更换性别
    private String mSexType;//性别类型
    private TextView mGoCertification;//实名认证;
    private AlertDialog.Builder mPhotoBuilder, mSexBuilder;
    private LinearLayout mLinearHeardIcon;
    private TextView mSchoolName;
    private PccApplication mApp;
    private TextView mTvName;
    private TextView mTvAge;
    private ImageView iv_person_icon;
    private byte[] mHeardIcon;
    private ImagePicker imagePicker;
    private int maxImgCount = 1;               //允许选择图片最大数
    private ArrayList<ImageItem> images = null;//当前选择的所有图片
    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (PccApplication) getApplication();

        setContentView(R.layout.activity_update_person_info);
        initView();
        initImagePager();
        initDialog();
        initData();
    }

    private void initImagePager() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoaderUntil());
    }

    private void initView() {
        mView = findViewById(R.id.include_title);
        mIvFinish = mView.findViewById(R.id.btn_left);
        mTitle = mView.findViewById(R.id.tv_title);
        mSex = (TextView) findViewById(R.id.tv_person_info_sex);
        mGoCertification = (TextView) findViewById(R.id.tv_go_certification);
        mLinearHeardIcon = (LinearLayout) findViewById(R.id.linear_heard_icon);
        mSchoolName = (TextView) findViewById(R.id.tv_person_info_school);

        mTvName = (TextView) findViewById(R.id.tv_person_info_nickname);
        mTvAge = (TextView) findViewById(R.id.tv_person_info_age);
        iv_person_icon = (ImageView) findViewById(R.id.iv_person_icon);

        mTvName.setOnClickListener(this);
        mTvAge.setOnClickListener(this);
        mLinearHeardIcon.setOnClickListener(this);
        mIvFinish.setOnClickListener(this);
        mGoCertification.setOnClickListener(this);
        mSex.setOnClickListener(this);

        mSchoolName.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onCanClick(View v) {
                if (application.getMyInfoData().getUserStatus() != 0) {
                    Toast.makeText(context, "不可修改学校", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(context, ChooseSchoolActivity.class));
            }
        });
    }

    private void initDialog() {
        mSexBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        mSexBuilder.setTitle("请选择");
        mSexBuilder.setNegativeButton("女", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSexType = "1";
                mSex.setText("女");
                updateUser(imagePath, mTvName.getText().toString(), mSexType,
                        mTvAge.getText().toString());
            }
        }).setPositiveButton("男", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSexType = "0";
                mSex.setText("男");
                updateUser(imagePath, mTvName.getText().toString(), mSexType,
                        mTvAge.getText().toString());
            }
        });

    }

    private void initData() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        mApp = (PccApplication) getApplication();
        mTitle.setText("用户信息");
        mSchoolName.setText(mApp.getMyInfoData().getSchoolName());
        mTvName.setText(mApp.getMyInfoData().getUserName());
        mTvAge.setText(mApp.getMyInfoData().getUserAge());
        mSex.setText(mApp.getMyInfoData().getUserSex().equals("1") ? "女" : "男");
        mSexType = mApp.getMyInfoData().getUserSex();
        getOssToken();
        imagePath = mApp.getMyInfoData().getUserImg();
//
        Glide.with(this).load(mApp.getMyInfoData().getUserImg())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.ico_def_photo)
                .placeholder(R.drawable.ico_def_photo).
                into(new BitmapImageViewTarget(iv_person_icon) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(UpdatePersonInfoActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv_person_icon.setImageDrawable(circularBitmapDrawable);
                    }
                });

        if (mApp.getMyInfoData().getUserStatus() == 0 || mApp.getMyInfoData().getUserStatus() == 6) {
       
        } else {
            mGoCertification.setVisibility(View.GONE);
        }

//
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSchoolName.setText(mApp.getMyInfoData().getSchoolName());
    }

    /**
     * 保存信息
     *
     * @param userImg
     * @param userName
     * @param userSex
     * @param userAge
     */
    private void updateUser(String userImg, String userName, final String userSex, final String userAge) {
        HttpAction.getInstance().updateUser(Integer.parseInt(PccApplication.getUserid()), userImg, userName, userSex, userAge, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                WaitUI.Cancel();
                try {
                    BaseResponseBean vo = JsonMananger.jsonToBean(response.body(), BaseResponseBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Glide.with(UpdatePersonInfoActivity.this).load(imagePath).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_person_icon) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(UpdatePersonInfoActivity.this.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            iv_person_icon.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                    mApp.getMyInfoData().setUserImg(imagePath);
                    mApp.getMyInfoData().setUserName(mTvName.getText().toString());
                    mApp.getMyInfoData().setUserSex(userSex);
                    mApp.getMyInfoData().setUserAge(userAge);
                    Toast.makeText(context, "更改成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
                ;
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }

    /**
     * 获取oss token
     */
    private void getOssToken() {
        GetOosTokenRequestBean bean = new GetOosTokenRequestBean();
        HttpAction.getInstance().getOssToken(bean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(UpdatePersonInfoActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogUtils.e("res=  " + response.body());
                    GetOosTokenResponseBean bean = JsonMananger.jsonToBean(response.body(), GetOosTokenResponseBean.class);
                    if (StringUtils.isEqual(bean.getCode(), "200")) {
                        AliOss.getInstance().oosConfig(getApplicationContext(), bean.getData().getAccessKeyId(), bean.getData().getAccessKeySecret(), bean.getData().getSecurityToken());
                    }
                } catch (Exception e) {
                    Toast.makeText(UpdatePersonInfoActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        BroadcastManager.getInstance(UpdatePersonInfoActivity.this).sendBroadcast(ConstantData.UP_DATA_IMAGE);
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        HttpAction.getInstance().updateUserCancel();
        super.onDestroy();
    }

    /**
     * 上传阿里图片
     *
     * @param imagebytes
     */
    private void uplodaImage(final byte[] imagebytes) {
        WaitUI.Show(UpdatePersonInfoActivity.this);
        new Thread() {
            @Override
            public void run() {
                AliOss.getInstance().uploadImage(System.currentTimeMillis() + ".jpg", imagebytes, new AliOss.OnOosUploadListener() {
                    @Override
                    public void onSuccess(String path) {
                        imagePath = path;
                        updateUser(imagePath, mTvName.getText().toString(), mSexType,
                                mTvAge.getText().toString());

                    }

                    @Override
                    public void onFailure(String msg) {
                        LogUtils.e("onFailure" + "msg=  " + msg);
                    }
                });
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(UpdatePersonInfoActivity.this, ChangeMyInfoActivity.class);
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_go_certification:
                startActivity(new Intent(UpdatePersonInfoActivity.this, CertificationActivity.class));
                break;
            case R.id.tv_person_info_sex:
                mSexBuilder.show();
                break;
            case R.id.linear_heard_icon:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    Intent intent1 = new Intent();
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        intent1.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent1.setData(Uri.fromParts("package", getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        intent1.setAction(Intent.ACTION_VIEW);
                        intent1.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                        intent1.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                    }
                    Toast.makeText(context, "请开启相机与相册权限", Toast.LENGTH_SHORT).show();
                    startActivity(intent1);
                    return;
                }
                getImageInfo();
                break;
            case R.id.tv_person_info_nickname:
                intent.putExtra("userImg", imagePath);
                intent.putExtra("userName", mTvName.getText().toString());
                intent.putExtra("mSexType", mSexType);
                intent.putExtra("userAge", mTvAge.getText().toString());
                intent.putExtra("Type", "nickName");
//                startActivityForResult(intent, 300);
                startActivity(intent);
                break;
            case R.id.tv_person_info_age:
                intent.putExtra("userImg", imagePath);
                intent.putExtra("userName", mTvName.getText().toString());
                intent.putExtra("mSexType", mSexType);
                intent.putExtra("userAge", mTvAge.getText().toString());
                intent.putExtra("Type", "nickAge");
//                startActivityForResult(intent, 301);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取图片信息
     */

    private void getImageInfo() {
        imagePicker.setShowCamera(true);
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
//                Integer width = (int) (ScreenUtils.getScreenWidthdp(AddStoreTfAc.this) );
//                Integer height = (int) (ScreenUtils.getScreenWidthdp(AddStoreTfAc.this) * 9 / 16 );
        Integer width = 280;
        Integer height = 280;
        LogUtils.d("width:" + width);
        LogUtils.d("height:" + height);
//                int a = (int) (ScreenUtils.getScreenWidth(AddStoreTfAc.this) / density);
//                int b = (int) (ScreenUtils.getScreenWidth(AddStoreTfAc.this) * 9 / 16 / density);
//                Log.d("AddStoreTfAc", "a:" + a);
//                Log.d("AddStoreTfAc", "b:" + b);
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        LogUtils.d("width:" + width);
        LogUtils.d("height:" + height);
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(height);
        imagePicker.setOutPutX(Integer.valueOf("280"));
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
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                imagePicker.getImageLoader().displayImage(UpdatePersonInfoActivity.this, images.get(0).path, iv_person_icon, 60, 60);
                if (images.get(0).path.equals(".gif")) {
                    Toast.makeText(UpdatePersonInfoActivity.this, "不支持gif动态图", Toast.LENGTH_SHORT).show();
                    return;
                }
                mHeardIcon = BitmapUtil.bitmapToByteArray(images.get(0).path);
                uplodaImage(mHeardIcon);

            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
