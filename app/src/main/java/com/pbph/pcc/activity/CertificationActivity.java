package com.pbph.pcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.pbph.pcc.bean.request.GetValidCodeRequestBean;
import com.pbph.pcc.bean.response.AuthenticationBean;
import com.pbph.pcc.bean.response.GetOosTokenResponseBean;
import com.pbph.pcc.bean.response.GetValidCodeResponseBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.AMUtils;
import com.pbph.pcc.tools.AliOss;
import com.pbph.pcc.tools.BitmapUtil;
import com.pbph.pcc.tools.DownTimer;
import com.pbph.pcc.tools.WaitUI;
import com.utils.GlideImageLoaderUntil;
import com.utils.StringUtils;

import java.util.ArrayList;

/**
 * 实名认证
 */
public class CertificationActivity extends BaseActivity implements OnClickListener, DownTimer.DownTimerListener, AdapterView.OnItemSelectedListener {

    private EditText mNameEditText, mPhoneEditText, mClassIDEditText, mCodeEditText;
    private TextView mSchoolTextView;
    private ImageView mPhoto;
    private Spinner mSexSpinner;
    private TextView mAddTextView, mGetCodeButton;
    private DownTimer downTimer = new DownTimer();
    private String authStudenCardImg = "";
    private String authName = "";
    private String authSex = "";
    private String authSchool = "";
    private String authNum = "";
    private String authPhone = "";
    private String authCode = "";
    private PccApplication mApp;
    private View mHeardLinear;
    private ImageView mIvFinish;
    private TextView mTitle;

    private byte[] mHeardIcon;
    private ImagePicker imagePicker;
    private int maxImgCount = 1;               //允许选择图片最大数
    private ArrayList<ImageItem> images = null;//当前选择的所有图片
    private String imagePath = "";
    private AuthenticationBean mAuthenticationBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (PccApplication) getApplication();
        setContentView(R.layout.activity_certification);
        downTimer.setListener(this);
        initView();
        initImagePager();
        getOssToken();


    }

    private void initImagePager() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoaderUntil());
    }

    private void initView() {
        mSexSpinner = (Spinner) findViewById(R.id.sp_certification_sex);
        mNameEditText = (EditText) findViewById(R.id.et_certification_name);
        mSchoolTextView = (TextView) findViewById(R.id.tv_certification_school);
        mClassIDEditText = (EditText) findViewById(R.id.et_certification_schoolno);
        mCodeEditText = (EditText) findViewById(R.id.et_certification_code);
        mPhoneEditText = (EditText) findViewById(R.id.et_certification_phone);
        mGetCodeButton = (TextView) findViewById(R.id.tv_certification_get_code);
        mAddTextView = (TextView) findViewById(R.id.tv_certification_add_pcc);
        mPhoto = (ImageView) findViewById(R.id.iv_certification_photo);
        mHeardLinear = findViewById(R.id.include_title);
        mIvFinish = mHeardLinear.findViewById(R.id.btn_left);
        mTitle = mHeardLinear.findViewById(R.id.tv_title);
        mIvFinish.setOnClickListener(this);
        mTitle.setText("实名认证");
        mGetCodeButton.setOnClickListener(this);
        mAddTextView.setOnClickListener(this);
        mPhoto.setOnClickListener(this);
        mSexSpinner.setOnItemSelectedListener(this);

        mSchoolTextView.setText(mApp.getMyInfoData().getSchoolName());

    }

    private void getOssToken() {
        GetOosTokenRequestBean bean = new GetOosTokenRequestBean();
        HttpAction.getInstance().getOssToken(bean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(CertificationActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogUtils.e("res=  " + response.body());
                    GetOosTokenResponseBean bean = JsonMananger.jsonToBean(response.body(), GetOosTokenResponseBean.class);
                    if (StringUtils.isEqual(bean.getCode(),"200")) {
                        AliOss.getInstance().oosConfig(getApplicationContext(), bean.getData().getAccessKeyId(), bean.getData().getAccessKeySecret(), bean.getData().getSecurityToken());
                    }
                } catch (Exception e) {
                    Toast.makeText(CertificationActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }

    private void getValidCode(String phone) {
        WaitUI.Show(this);
        GetValidCodeRequestBean bean = new GetValidCodeRequestBean(phone);
        HttpAction.getInstance().sendSmsValCode(bean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                WaitUI.Cancel();
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(CertificationActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogUtils.e("res=  " + response.body());
                    GetValidCodeResponseBean bean = JsonMananger.jsonToBean(response.body(), GetValidCodeResponseBean.class);
                    if (StringUtils.isEqual(bean.getCode(), "200")) {

                        downTimer.startDown(60 * 1000);
                    } else {
                        Toast.makeText(CertificationActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(CertificationActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                WaitUI.Cancel();
                Toast.makeText(mApp, "网络状态异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uplodaImage(final byte[] imagebytes) {
        WaitUI.Show(this);
        new Thread() {
            @Override
            public void run() {
                AliOss.getInstance().uploadImage(System.currentTimeMillis() + ".jpg", imagebytes, new AliOss.OnOosUploadListener() {
                    @Override
                    public void onSuccess(String path) {
                        LogUtils.e("onSuccess" + "path=  " + path);
                        authStudenCardImg = path;
                        WaitUI.Cancel();

                    }

                    @Override
                    public void onFailure(String msg) {
                        LogUtils.e("onFailure" + "msg=  " + msg);
                        WaitUI.Cancel();
                    }
                });
            }
        }.start();

    }


    /**
     * 实名认证
     */
    public void upLoadIdentification() {
        WaitUI.Show(CertificationActivity.this);
        HttpAction.getInstance().identification(authStudenCardImg, authName, authSex, authSchool,
                authNum, authPhone, authCode, PccApplication.getUserid(),
                new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            WaitUI.Cancel();
                            mAuthenticationBean = JsonMananger.jsonToBean(response.body(), AuthenticationBean.class);
                            if (mAuthenticationBean.getCode().equals("200") && mAuthenticationBean.getMsg().equals("成功")) {
                                mApp.getMyInfoData().setUserStatus(Integer.valueOf(mAuthenticationBean.getData().getAuthStatus()));
                                Toast.makeText(mApp, "上传成功", Toast.LENGTH_SHORT).show();
                                finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                imagePicker.getImageLoader().displayImage(CertificationActivity.this, images.get(0).path, mPhoto, 60, 60);
                Glide.with(CertificationActivity.this).load(images.get(0).path).error(R.mipmap.banner_zw).into(mPhoto);
                mHeardIcon = BitmapUtil.bitmapToByteArray(images.get(0).path);
                uplodaImage(mHeardIcon);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }

//        switch (requestCode) {
//            case ConstantData.PHOTO:
//                if (null == data) {
//                    Toast.makeText(this, "获取照片失败，请重新操作", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                try {
//                    Bitmap bitmap = BitmapUtil.getBitmapFormUri(this, data.getData());
//                    LogUtils.e("bitmap length  " + bitmap.getByteCount());
//                    byte[] imagebytes = BitmapUtil.getImageByte(bitmap);
//                    LogUtils.e("imagebytes length  " + imagebytes.length);
////                    photoZoom(data.getData());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case ConstantData.CROP:
//                if (null != data) {
//                    Bundle extras = data.getExtras();
//                    if (extras != null) {
//                        Bitmap photo = extras.getParcelable("data");
//                        if (null != photo) {
//                            byte[] imagebytes = BitmapUtil.getImageByte(photo);
//                            LogUtils.e("imagebytes length  " + imagebytes.length);
//                            uplodaImage(imagebytes);
//                            mPhoto.setImageBitmap(photo);
//                        } else {
//                            Toast.makeText(this, "获取照片失败，请重新操作", Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    }
//                    return;
//                }
//                Toast.makeText(this, "获取照片失败，请重新操作", Toast.LENGTH_SHORT).show();
//                break;
//        }
    }

//    private void photoZoom(Uri uri) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 2);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高是裁剪图片宽高
//        intent.putExtra("outputX", 300);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, ConstantData.CROP);
//    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_certification_get_code:
                String phone = mPhoneEditText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, R.string.phone_number_is_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!AMUtils.isMobile(phone)) {
                    Toast.makeText(this, R.string.Illegal_phone_number, Toast.LENGTH_SHORT).show();
                    return;
                }
                getValidCode(phone);
                break;
            case R.id.iv_certification_photo:
//                Intent intent = new Intent(
//                        Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("image/*");
//                startActivityForResult(intent,
//                        ConstantData.PHOTO);
                getImageInfo();


                break;
            case R.id.tv_certification_add_pcc:
                authName = mNameEditText.getText().toString();
                authSchool = mApp.getMyInfoData().getSchoolId();
                authNum = mClassIDEditText.getText().toString();
                authPhone = mPhoneEditText.getText().toString();
                authCode = mCodeEditText.getText().toString();
                if (AMUtils.hasEmoji(authName)) {
                    Toast.makeText(this, "不支持输入表情", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (authName.equals("")) {
                    Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (authStudenCardImg.equals("")) {
                    Toast.makeText(this, "请选择照片", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (authSex.equals("")) {
                    Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (authSchool.equals("")) {

                }
                if (authNum.equals("")) {
                    Toast.makeText(mApp, "请填写学号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!AMUtils.isMobile(authPhone)) {
                    Toast.makeText(mApp, "请填写正确手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (authCode.equals("")) {
                    Toast.makeText(mApp, "请填写验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!AMUtils.isYZm(authCode)) {
                    Toast.makeText(mApp, "验证码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }


                upLoadIdentification();
                break;
            case R.id.btn_left:
                finish();
                break;
        }
    }

    private void getImageInfo() {
        imagePicker.setShowCamera(true);
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
//                Integer width = (int) (ScreenUtils.getScreenWidthdp(AddStoreTfAc.this) );
//                Integer height = (int) (ScreenUtils.getScreenWidthdp(AddStoreTfAc.this) * 9 / 16 );
        Integer width = 400;
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
        imagePicker.setOutPutX(Integer.valueOf("400"));
        imagePicker.setOutPutY(Integer.valueOf("280"));
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
        //ImagePicker.getInstance().setSelectedImages(images);
        startActivityForResult(intent, 100);

    }

    @Override
    public void onTick(long millisUntilFinished) {
        mGetCodeButton.setBackgroundResource(R.color.dark_gray);
        mGetCodeButton.setText(String.valueOf(millisUntilFinished / 1000) + "秒后可重发");
        mGetCodeButton.setClickable(false);
    }

    @Override
    public void onFinish() {
        mGetCodeButton.setBackgroundResource(R.color.main_tab_color_bg);
        mGetCodeButton.setText(R.string.btn_login_getcode_text);
        mGetCodeButton.setClickable(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            authSex = "0";
        } else {
            authSex = "1";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
