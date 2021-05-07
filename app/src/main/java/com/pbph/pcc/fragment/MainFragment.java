package com.pbph.pcc.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.OnSingleClickListener;
import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.activity.CertificationActivity;
import com.pbph.pcc.activity.ChooseSchoolActivity;
import com.pbph.pcc.activity.ImmediatelyApplyActivity;
import com.pbph.pcc.activity.MessageListActivity;
import com.pbph.pcc.activity.MyBrowsersActivity;
import com.pbph.pcc.activity.RecieveOrderActivity;
import com.pbph.pcc.activity.ReleaseOrderActivity;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.request.BaseRequestBean;
import com.pbph.pcc.bean.request.GetFigureWebRequestBean;
import com.pbph.pcc.bean.response.GetFigureWebResponseBean;
import com.pbph.pcc.bean.response.QueryMyInfostatusBean;
import com.pbph.pcc.broadcast.BroadcastManager;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.WaitUI;
import com.utils.StringUtils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {

    private String TAG = MainFragment.class.getSimpleName();
    private View view = null;
    private Banner banner = null;
    private ImageView mMessageImageView = null;
    private ImageView mReleaseOrder, mTakeOrder = null;
    private TextView mSchoolTextView = null;
    private PccApplication application = null;
    private GetFigureWebResponseBean figureWebResponse = null;
    private AlertDialog.Builder builder = null;

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

        view = inflater.inflate(R.layout.fragment_main, container, false);
        initView();

        if (!StringUtils.isEmpty(application.getMyInfoData().getSchoolName())) {
            mSchoolTextView.setText(application.getMyInfoData().getSchoolName());
        }
        if ("0".equals(application.getMyInfoData().getIsRead())) {
            mMessageImageView.setImageResource(R.drawable.ico_message_unread);
        }
        return view;
    }

    private void initView() {
        mSchoolTextView = view.findViewById(R.id.tv_main_school);
        mSchoolTextView.setOnClickListener(onSingleClickListener);

        mMessageImageView = view.findViewById(R.id.iv_main_message);
        mReleaseOrder = view.findViewById(R.id.iv_main_release_order);
        mTakeOrder = view.findViewById(R.id.iv_main_make_money);
        banner = view.findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        mMessageImageView.setOnClickListener(onSingleClickListener);
        mReleaseOrder.setOnClickListener(onSingleClickListener);
        mTakeOrder.setOnClickListener(onSingleClickListener);
//        String array[] = getResources().getStringArray(R.array.url);
        ArrayList<String> list = new ArrayList<String>();

//        for (String string : array) {
//        list.add(ConstantData.MAIN_BANNER_ZANWEI);
//        }

        banner.setImages(list);
        banner.start();

//        if (list.size() != 0) {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                try {
                    if (figureWebResponse == null) return;
                    List<GetFigureWebResponseBean.DataBean> data = figureWebResponse.getData();
                    if (data == null) return;
                    if (position >= data.size()) return;
                    String url = data.get(position).getFigureUrl();
                    if (TextUtils.isEmpty(url)) return;

                    startActivity(new Intent(getActivity(), MyBrowsersActivity.class)
                            .putExtra("url",

                                    url).putExtra("title", "内容"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
//        }
        initDialog();
    }


    private void refreshBanner(GetFigureWebResponseBean figureWebResponse) {
        List<GetFigureWebResponseBean.DataBean> data = figureWebResponse.getData();
        List list = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i).getFigureImg());
        }
        if (list.size() <= 0) {
            list.add(R.drawable.null_white);
        }
        banner.update(list);
    }

    private void initDialog() {
        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setInverseBackgroundForced(true);
        builder.setTitle("是否去设置权限？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                getActivity().startActivityForResult(intent, 600);


            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    private void figureWeb() {
        BaseRequestBean bean = new GetFigureWebRequestBean(application.getMyInfoData()
                .getSchoolId());
        HttpAction.getInstance().figureWeb(bean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                WaitUI.Cancel();
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(getActivity(), R.string.connect_error, Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    LogUtils.e("res=  " + response.body());

                    figureWebResponse = JsonMananger.jsonToBean(response.body(),
                            GetFigureWebResponseBean.class);
                    if (StringUtils.isEqual(figureWebResponse.getCode(), "200")) {
                        refreshBanner(figureWebResponse);
                    } else {
//                        Toast.makeText(getActivity(), figureWebResponse.getMsg(), Toast
// .LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
//                    Toast.makeText(getActivity(), R.string.connect_error, Toast.LENGTH_SHORT)
// .show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (banner.getChildCount() <= 1) figureWeb();

        queryMyInfostatus();

        if (mSchoolTextView != null) {
            PccApplication application = (PccApplication) getActivity().getApplication();
            if (!StringUtils.isEmpty(application.getMyInfoData().getSchoolName())) {
                mSchoolTextView.setText(application.getMyInfoData().getSchoolName());
            } else {
                mSchoolTextView.setText("选择学校");
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mSchoolTextView != null) {
                PccApplication application = (PccApplication) getActivity().getApplication();
                if (!StringUtils.isEmpty(application.getMyInfoData().getSchoolName())) {
                    mSchoolTextView.setText(application.getMyInfoData().getSchoolName());
                }
            }
        } else {
        }
    }

    @Override
    public void onAttach(Context context) {
        //  Log.e(TAG, "onAttach");
        super.onAttach(context);
        application = (PccApplication) getActivity().getApplication();
        BroadcastManager.getInstance(getActivity()).addAction(ConstantData.REFRESH_BANNER_ACITON,
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        figureWeb();
                    }
                });
        BroadcastManager.getInstance(getActivity()).addAction(ConstantData
                .REFRESH_UNREAD_MESSAGE_ACITON, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mMessageImageView.setImageResource(R.drawable.ico_message_unread);
            }
        });
    }


    @Override
    public void onDestroy() {
        //  Log.e(TAG, "onDestroy");
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
        if (null != banner) {
            banner.releaseBanner();
        }
        BroadcastManager.getInstance(getActivity()).destroy(ConstantData
                .REFRESH_UNREAD_MESSAGE_ACITON);
        BroadcastManager.getInstance(getActivity()).destroy(ConstantData.REFRESH_BANNER_ACITON);
    }

    OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onCanClick(View v) {

            switch (v.getId()) {
                case R.id.iv_main_release_order:
                    startActivity(new Intent(getActivity(), ReleaseOrderActivity.class));
                    break;
                case R.id.iv_main_make_money:
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                            .ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                            .READ_PHONE_STATE)
                            != PackageManager.PERMISSION_GRANTED
                            ) {
                        //进入到这里代表没有权限.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest
                                        .permission.ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale
                                (getActivity(),
                                        Manifest.permission.READ_PHONE_STATE)) {
                            //已经禁止提示了
                            Toast.makeText(getActivity(), "您已禁止该权限，需要重新开启。", Toast
                                    .LENGTH_SHORT).show();
                            builder.show();
                            return;
                        } else {


                        }
                        return;

                    }

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
                    startActivity(new Intent(getActivity(), RecieveOrderActivity.class));
                    break;
                case R.id.tv_main_school:
                    if (application.getMyInfoData().getUserStatus() != 0) {
                        Toast.makeText(getContext(), "不可修改学校", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startActivity(new Intent(getContext(), ChooseSchoolActivity.class));
                    break;
                case R.id.iv_main_message:
                    startActivity(new Intent(getActivity(), MessageListActivity.class));
                    mMessageImageView.setImageResource(R.drawable.ico_message_readed);
                    break;

            }
        }
    };


    private void queryMyInfostatus() {
        HttpAction.getInstance().queryMyInfostatus(Integer.parseInt(PccApplication.getUserid()),
                new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e("res=  " + response.body());
                        try {
                            QueryMyInfostatusBean vo = JsonMananger.jsonToBean(response.body(),
                                    QueryMyInfostatusBean.class);

                            if (!StringUtils.isEqual(vo.code, "200")) {
                                Toast.makeText(MainFragment.this.getContext(), StringUtils
                                        .isEmpty(vo
                                                .msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            application.getMyInfoData().setUserStatus(Integer.parseInt(vo.getData()
                                    .getUserStatus()));
                            application.getMyInfoData().setIsOrderSwitch(vo.getData()
                                    .getOrderSwitch());
                            application.getMyInfoData().setStarLevel(vo.getData().getStarLevel());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainFragment.this.getContext(), "系统错误", Toast
                                    .LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
}
