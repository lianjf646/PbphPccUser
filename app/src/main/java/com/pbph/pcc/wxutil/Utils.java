package com.pbph.pcc.wxutil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.utils.LogUtils;
import com.pbph.pcc.tools.ConstantData;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String TAG = "Utils";


    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "1.0";
        }
    }


    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    public static void startAPP(Context context, String appPackageName) {
        try {
            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(appPackageName);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(intent);
        } catch (Exception ignored) {

        }
    }

    public static void startAppNew(Context context, String appPackageName) {
        try {
            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(appPackageName);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (Exception ignored) {

        }
    }


//	public static void toAPP(Context context, String pageName) {
//
//		Intent intent = new Intent(context, FloatWindowService.class);
//
//		ActivityManager manager = (ActivityManager) context
//				.getSystemService(Context.ACTIVITY_SERVICE);
//		@SuppressWarnings("deprecation")
//		List<RunningTaskInfo> task_info = manager.getRunningTasks(20);
//
//		for (int i = 0; i < task_info.size(); i++) {
//			if (pageName.equals(task_info.get(i).topActivity.getPackageName())) {
//				System.out.println("��̨  "
//						+ task_info.get(i).topActivity.getClassName());
//
//				pageName = task_info.get(i).topActivity.getClassName();
//				// ������ָ�Ӻ�̨���ص�ǰ̨ ǰ�������ǹؼ�
//				intent.setAction(Intent.ACTION_MAIN);
//				intent.addCategory(Intent.CATEGORY_LAUNCHER);
//				try {
//					intent.setComponent(new ComponentName(context, Class
//							.forName(pageName)));
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}//
//					// intent.setClass(context,
//					// Class.forName(className));
//				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//						| Intent.FLAG_ACTIVITY_NEW_TASK
//						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//				context.startActivity(intent);
//
//			}
//
//		}
//
//	}


//	public static void getRoot(Context context, String fileName) {
//		String string = "/system/bin/screencap -p " + fileName + "\n\n";
//		try {
//			// ProgressDialog progressDialog = ProgressDialog.show(this, "ROOT",
//			// "���ڻ�ȡROOTȨ��",true,false);
//			// progressDialog.show();
//
//			Runtime.getRuntime().exec(string);
//
//		} catch (IOException e) {
//			FloatWindowManager.alertTextError("��ȡROOTȨ�޳���!", context);
//
//			System.out.println("��ȡROOTȨ�޳���!");
//			e.printStackTrace();
//		}
//	}

    public static boolean isTopActivity(Context context, String packageName) {

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        @SuppressWarnings("deprecation")
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);

        if (tasksInfo.size() > 0) {

            // Ӧ�ó���λ�ڶ�ջ�Ķ���

            if (packageName.equals(tasksInfo.get(0).topActivity
                    .getPackageName())) {

                return true;

            }

        }

        return false;

    }

    private static boolean mHaveRoot = false;

    public static boolean haveRoot() {

        if (!mHaveRoot) {
            int ret = execRootCmdSilent("echo test"); // ͨ��ִ�в������������
            if (ret != -1) {
                LogUtils.i("have root!");
                mHaveRoot = true;
            } else {
                LogUtils.i("not root!");
            }
        } else {
            LogUtils.i("mHaveRoot = true, have root!");
        }
        return mHaveRoot;
    }

    public static int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());

            LogUtils.i(cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    @SuppressWarnings("deprecation")
    public static String execRootCmd(String cmd) {
        StringBuilder result = new StringBuilder(new StringBuffer());
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            Process p = Runtime.getRuntime().exec("su");// ����Root�����androidϵͳ����su����
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());

            LogUtils.i(cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line;
            while ((line = dis.readLine()) != null) {
                LogUtils.d("result" + line);
                result.append(line);
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }


    public static boolean isWorked(Context context, String text) {

        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(30);

        for (int i = 0; i < runningService.size(); i++)

        {
//			System.out.println("�Ѿ������ķ���"
//					+ runningService.get(i).service.getClassName().toString());

            if (runningService.get(i).service.getClassName()
                    .equals(text))

            {

                return true;

            }

        }

        return false;

    }

    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); // �л���root�ʺ�
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception ignored) {
            }
        }
        return true;
    }

    public static void doSU() {
        try {

            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            // os.writeBytes("ifconfig eth0 192.168.18.122\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Bitmap getBitmapFromUrl(String imgUrl) {

        URL url;
        Bitmap bitmap = null;

        try {
            url = new URL(imgUrl);
            InputStream is = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public void saveBitmapToFile(Bitmap obmp, String picName, Context context) {
        String SD_PATH = ConstantData.DOWN_LOAD_PATH;
        File dir = new File(SD_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File(SD_PATH, picName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            obmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
