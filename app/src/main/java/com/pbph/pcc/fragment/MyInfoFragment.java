package com.pbph.pcc.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.activity.AgencyApplyActivity;
import com.pbph.pcc.activity.CertificationActivity;
import com.pbph.pcc.activity.ImmediatelyApplyActivity;
import com.pbph.pcc.activity.KnowMeActivity;
import com.pbph.pcc.activity.ReceivingLocationActivity;
import com.pbph.pcc.activity.SetAppActivity;
import com.pbph.pcc.activity.ShareActivity;
import com.pbph.pcc.activity.UpdatePersonInfoActivity;
import com.pbph.pcc.activity.UpdatePhoneActivity;
import com.pbph.pcc.activity.WalletActivity;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.MyUserInfo;
import com.pbph.pcc.bean.response.QueryMyInfostatusBean;
import com.pbph.pcc.bean.response.UUIDResultBean;
import com.pbph.pcc.broadcast.BroadcastManager;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.ConstantData;
import com.utils.StringUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;


public class MyInfoFragment extends Fragment implements View.OnClickListener, RongIM.UserInfoProvider {

    private String TAG = MyInfoFragment.class.getSimpleName();
    private View view = null;
    private PccApplication application = null;
    private TextView mNickNameTextView, mCertificationTextView, mCertificationStateTextView,
            mAddressTextView, mWalletTextView, mShareTextView, mCustomerCenterTextView, mUpdatePhoneTextView;
    private ImageView mPhotoImageView;
    private RatingBar mLevelRatingBar;
    private ImageView mSetTextView;
    private TextView mKnowMe;

    private CheckBox checkboxGbjd;
    MyUserInfo bean;
    private AlertDialog.Builder hintDialog;
    private TextView myInfoState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != view) {
//            ((MainTabActivity) getActivity()).getMainInfo(application.getUserid());
            return view;
        }
        view = inflater.inflate(R.layout.fragment_myinfo, container, false);
        initView();
        return view;
    }

    @TargetApi(21)
    private void initView() {
        initDialog();

        mLevelRatingBar = view.findViewById(R.id.ratingBar);
        mLevelRatingBar.setRating(application.getMyInfoData().getStarLevel());
        mCertificationTextView = view.findViewById(R.id.tv_myinfo_certification);
        mNickNameTextView = view.findViewById(R.id.tv_myinfo_name);
        mCertificationStateTextView = view.findViewById(R.id.tv_myinfo_certification_state);
        mAddressTextView = view.findViewById(R.id.tv_myinfo_address);
        mWalletTextView = view.findViewById(R.id.tv_myinfo_wallet);
        mShareTextView = view.findViewById(R.id.tv_myinfo_share);
        mCustomerCenterTextView = view.findViewById(R.id.tv_myinfo_customer_center);
        mUpdatePhoneTextView = view.findViewById(R.id.tv_myinfo_update_phone);
        mKnowMe = view.findViewById(R.id.tv_myinfo_know_me);
        mPhotoImageView = view.findViewById(R.id.iv_myinfo_photo);
        myInfoState = view.findViewById(R.id.tv_myinfo_state);
        LinearLayout myinfoLayout = view.findViewById(R.id.ll_myinfo_layout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mPhotoImageView.getLayoutParams().width = displayMetrics.widthPixels / 6;
        mPhotoImageView.getLayoutParams().height = displayMetrics.widthPixels / 6;
        myinfoLayout.getLayoutParams().height = displayMetrics.heightPixels / 5;
        mSetTextView = view.findViewById(R.id.tv_set);

        checkboxGbjd = view.findViewById(R.id.checkboxGbjd);

        checkboxGbjd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (application.getMyInfoData().getUserStatus() == 0) {
                    checkboxGbjd.setChecked(false);
                    startActivity(new Intent(getContext(), ImmediatelyApplyActivity.class));
                    return;
                }

                if (application.getMyInfoData().getUserStatus() == 6) {
                    startActivity(new Intent(getContext(), CertificationActivity.class));
                    return;
                }
                if (application.getMyInfoData().getUserStatus() == 1) {
                    checkboxGbjd.setChecked(false);
                    Toast.makeText(getContext(), "认证中，请等候", Toast.LENGTH_SHORT).show();
                    return;
                }

                int state = checkboxGbjd.isChecked() ? 1 : 0;
                application.getMyInfoData().setIsOrderSwitch(String.valueOf(state));
                isOrderSwitch(state, uuId = String.valueOf(System.currentTimeMillis()));
            }
        });

//        checkboxGbjd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//
//                if (application.getMyInfoData().getUserStatus() == 0) {
//                    checkboxGbjd.setChecked(false);
//                    startActivity(new Intent(getContext(), ImmediatelyApplyActivity.class));
//                    return;
//                }
//                if (application.getMyInfoData().getUserStatus() == 1) {
//                    checkboxGbjd.setChecked(false);
//                    Toast.makeText(getContext(), "认证中，请等候", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                int state = b ? 1 : 0;
//                application.getMyInfoData().setIsOrderSwitch(String.valueOf(state));
//                isOrderSwitch(state, uuId = String.valueOf(System.currentTimeMillis()));
//
//            }
//        });

        mCustomerCenterTextView.setOnClickListener(this);
        mCertificationTextView.setOnClickListener(this);
        mUpdatePhoneTextView.setOnClickListener(this);
        mWalletTextView.setOnClickListener(this);
        mPhotoImageView.setOnClickListener(this);
        mAddressTextView.setOnClickListener(this);
        mShareTextView.setOnClickListener(this);
        mSetTextView.setOnClickListener(this);
        mKnowMe.setOnClickListener(this);

        BroadcastManager.getInstance(getActivity()).addAction(ConstantData.UP_DATA_IMAGE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mNickNameTextView.setText(application.getMyInfoData().getUserName());
                setPhotoImage();
            }
        });
    }


    private void setPhotoImage() {
        Drawable drawable = getActivity().getResources().getDrawable(R.drawable.ico_def_photo);

        Glide.with(getActivity()).load(bean.getUserImg()).asBitmap().centerCrop().placeholder(R.drawable.ico_def_photo).into(new BitmapImageViewTarget(mPhotoImageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mPhotoImageView.setImageDrawable(circularBitmapDrawable);
            }
        });

//        mPhotoImageView.setImageDrawable(drawable);

    }


    private void initDialog() {
        hintDialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        hintDialog.setInverseBackgroundForced(true);
        hintDialog.setTitle("提示:");
        hintDialog.setMessage("您已经是代理了，无需重复申请，请去代理端管理你的团队吧！");
        hintDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

    }


//    private void bindWeChat(String userid, String json) {
//        WaitUI.Show(getActivity());
//        mBindWeChatUUID = HttpAction.getInstance().bindWeChat(userid, json, this);
//    }

//    private void saveJpushID(String userid, String rid) {
//        WaitUI.Show(getActivity());
//        mSaveJpushIDUUID = HttpAction.getInstance().saveJpushID(userid, rid, this);
//    }

    //    private void getMainInfo(String userid) {
//        WaitUI.Show(getActivity());
//        try {
//            mGetMainInfoUUID = HttpAction.getInstance().getMainInfo(userid, URLEncoder.encode(application.getReadTime(),"utf-8"), this);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        //  Log.e(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onPause() {
        //  Log.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        //  Log.e(TAG, "onResume");
        super.onResume();
        RongIM.setUserInfoProvider(this, false);
        setGbjd();
        queryMyInfostatus();
        flusahUserInfo();

    }


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (checkboxGbjd == null) return;
//        if (!isVisibleToUser) return;
//        setGbjd();
//        flusahUserInfo();
//    }

    private void setGbjd() {
        int state = Integer.parseInt(application.getMyInfoData().getIsOrderSwitch());
        if (state == 0) {
            checkboxGbjd.setChecked(false);
            checkboxGbjd.setText("开启接单");
        } else {
            checkboxGbjd.setChecked(true);
            checkboxGbjd.setText("关闭接单");
        }
    }

    private void flusahUserInfo() {
        bean = application.getMyInfoData();
        if (null == bean) {
            return;
        }

        mCertificationTextView.setText(bean.getUserStatus() < 2 || bean.getUserStatus() == 6 ? "实名认证" : "申请代理");
        mNickNameTextView.setText(bean.getUserName());
        flushUserStatus();
        setPhotoImage();

    }

    private void flushUserStatus() {

        String userState = "";
        switch (bean.getUserStatus()) {
            case 0:
                userState = "未认证";
                controlGone();
                break;
            case 1:
                userState = "认证中";
                controlGone();
                break;
            case 2:
                userState = "已认证";
                controlVisible();

                break;
            case 3:
                userState = "代理申请中";
                controlVisible();
                break;
            case 4:
                userState = "代理";
                controlVisible();
                break;
            case 5:
                userState = "总代理";
                mCertificationTextView.setVisibility(View.GONE);
                break;
            case 6:
                userState = "认证失败";
                controlGone();
                break;
            default:
                break;

        }
        mCertificationStateTextView.setText(userState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //  Log.e(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        //  Log.e(TAG, "onAttach");
        super.onAttach(context);
        application = (PccApplication) getActivity().getApplication();

    }


    @Override
    public void onDestroy() {
        BroadcastManager.getInstance(getActivity()).destroy(ConstantData.UP_DATA_IMAGE);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        //  Log.e(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        //  Log.e(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_myinfo_certification:
                LogUtils.e("onClick: " + bean.getUserStatus());
                if (bean.getUserStatus() == 0) {
                    intent.setClass(getActivity(), CertificationActivity.class);
                } else if (bean.getUserStatus() == 1) {
                    Toast.makeText(getActivity(), "正在审核中", Toast.LENGTH_SHORT).show();
                    return;
                } else if (bean.getUserStatus() == 2) {
                    intent.setClass(getActivity(), AgencyApplyActivity.class);
                } else if (bean.getUserStatus() == 3) {
                    Toast.makeText(getActivity(), "正在审核中", Toast.LENGTH_SHORT).show();
                    return;
                } else if (bean.getUserStatus() == 4) {
                    intent.setClass(getActivity(), AgencyApplyActivity.class);
                } else if (bean.getUserStatus() == 6) {
                    intent.setClass(getActivity(), CertificationActivity.class);
                } else {
                    return;
                }
                break;
            case R.id.tv_myinfo_address:
                intent.setClass(getActivity(), ReceivingLocationActivity.class).putExtra("ReceivingLocationActivity", "0");
                break;
            case R.id.tv_myinfo_wallet:

                if (application.getMyInfoData().getUserStatus() == 0) {
                    startActivity(new Intent(getContext(), ImmediatelyApplyActivity.class));
                    return;
                }
                if (application.getMyInfoData().getUserStatus() == 6) {
                    startActivity(new Intent(getContext(), CertificationActivity.class));
                    return;
                }
                if (application.getMyInfoData().getUserStatus() == 1) {
                    Toast.makeText(getContext(), "认证中，请等候", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.setClass(getActivity(), WalletActivity.class);
                break;
            case R.id.tv_myinfo_share:
                intent.setClass(getActivity(), ShareActivity.class);
                break;
            case R.id.tv_myinfo_customer_center:
                RongIM.getInstance().startCustomerServiceChat(getActivity(), PccApplication.RONGYUN_CUSTOMERSERVICEID, "在线客服", null);
                return;
            case R.id.tv_myinfo_update_phone:
                intent.setClass(getActivity(), UpdatePhoneActivity.class);
                break;
            case R.id.iv_myinfo_photo:
                intent.setClass(getActivity(), UpdatePersonInfoActivity.class);
                break;
            case R.id.tv_set:
                intent.setClass(getActivity(), SetAppActivity.class);
                break;
            case R.id.tv_myinfo_know_me:
                intent.setClass(getActivity(), KnowMeActivity.class);
                break;

        }
        startActivity(intent);
    }

    String uuId;

    //0关闭接单，1开启接单
    private void isOrderSwitch(final int orderSwitch, final String now_uuId) {
        HttpAction.getInstance().isOrderSwitchCancel();

        if (!StringUtils.isEqual(uuId, now_uuId)) {
            return;
        }
        HttpAction.getInstance().isOrderSwitch(Integer.parseInt(PccApplication.getUserid()), orderSwitch, now_uuId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

                LogUtils.e("res=  " + response.body());

                try {
                    if (!StringUtils.isEqual(uuId, now_uuId)) {
                        return;
                    }
                    UUIDResultBean vo = JsonMananger.jsonToBean(response.body(), UUIDResultBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        int temp = orderSwitch == 1 ? 0 : 1;
                        application.getMyInfoData().setIsOrderSwitch(String.valueOf(temp));
                        if (temp == 0) {
                            checkboxGbjd.setChecked(false);
                            checkboxGbjd.setText("开启接单");
                        } else {
                            checkboxGbjd.setChecked(true);
                            checkboxGbjd.setText("关闭接单");
                        }

                        Toast.makeText(MyInfoFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!StringUtils.isEqual(uuId, vo.getData().getUuId())) {
                        return;
                    }
                    application.getMyInfoData().setIsOrderSwitch(String.valueOf(orderSwitch));
                    if (orderSwitch == 0) {
                        checkboxGbjd.setChecked(false);
                        checkboxGbjd.setText("开启接单");
                    } else {
                        checkboxGbjd.setChecked(true);
                        checkboxGbjd.setText("关闭接单");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    int temp = orderSwitch == 1 ? 0 : 1;
                    application.getMyInfoData().setIsOrderSwitch(String.valueOf(temp));
                    if (temp == 0) {
                        checkboxGbjd.setChecked(false);
                        checkboxGbjd.setText("开启接单");
                    } else {
                        checkboxGbjd.setChecked(true);
                        checkboxGbjd.setText("关闭接单");
                    }
                    Toast.makeText(MyInfoFragment.this.getContext(), "系统错误!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void queryMyInfostatus() {
        HttpAction.getInstance().queryMyInfostatus(Integer.parseInt(PccApplication.getUserid()), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());

                try {
                    QueryMyInfostatusBean vo = JsonMananger.jsonToBean(response.body(), QueryMyInfostatusBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(MyInfoFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    application.getMyInfoData().setUserStatus(Integer.parseInt(vo.getData().getUserStatus()));
                    application.getMyInfoData().setIsOrderSwitch(vo.getData().getOrderSwitch());
                    application.getMyInfoData().setStarLevel(vo.getData().getStarLevel());
                    flushUserStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MyInfoFragment.this.getContext(), "系统错误.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 控件隐藏
     */
    private void controlGone() {
        checkboxGbjd.setVisibility(View.GONE);
        mLevelRatingBar.setVisibility(View.GONE);
        myInfoState.setVisibility(View.GONE);
    }

    /**
     * 显示显示
     */
    private void controlVisible() {
        checkboxGbjd.setVisibility(View.VISIBLE);
        mLevelRatingBar.setVisibility(View.VISIBLE);
        myInfoState.setVisibility(View.VISIBLE);
        mLevelRatingBar.setRating(application.getMyInfoData().getStarLevel());
    }


    @Override

    public UserInfo getUserInfo(String s) {
        return new UserInfo("1", "1", Uri.parse(application.getMyInfoData().getUserImg()));
    }
}
