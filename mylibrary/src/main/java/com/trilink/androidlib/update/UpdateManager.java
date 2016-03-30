package com.trilink.androidlib.update;


import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.trilink.androidlib.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class UpdateManager {
	/* 下载*/
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 保存服务器返回*/
	String serviceStr = "";
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条*/
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度*/
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}
	/**
	 * 软件更新
	 */
	public void checkUpdate(int type) {
		if (isUpdate()) {
			// 显示提示对话�?
			showNoticeDialog();
		} else {
			if(type==1){
//			    BaseActivity ac2 = (BaseActivity)mContext;
//                ac2.showMsg(R.string.soft_update_no);
				Toast.makeText(mContext, R.string.soft_update_no,
					     Toast.LENGTH_SHORT).show();
			}
		}
	}
	/**
	 * 软件是否有更新版
	 * 
	 * @return
	 */
	private boolean isUpdate() {
		// 获取当前软件版本
		String serviceVersion = "";
		try {
//			serviceStr = UrlConnection.getUrl(mContext, null, "/AutoUpdateServlet", "checkTheVersion");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!serviceStr.equals("")){
			serviceVersion = serviceStr.split("<#>")[0];
			double versionCode = getVersionCode(mContext);
			System.out.println("服务器版"+serviceVersion+"  客户端版"+versionCode);
			if (Double.parseDouble(serviceVersion) > versionCode) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取软件版本
	 * 
	 * @param context
	 * @return
	 */
	private double getVersionCode(Context context) {
		double versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = Double.parseDouble(context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 显示软件更新对话
	 */
	private void showNoticeDialog() {
		// 构对话
		Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(R.string.soft_update_info);
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 显示下载对话
						showDownloadDialog();
					}
				});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话�?
	 */
	private void showDownloadDialog() {
		// 构�?软件下载对话�?
		Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度�?
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.soft_update_cancel,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 设置取消状�?
						cancelUpdate = true;
					}
				});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软�?
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					System.out.println(serviceStr.split("<#>")[2]);
					URL url = new URL(serviceStr.split("<#>")[2]);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, serviceStr.split("<#>")[1]);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度�?
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下�?
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显�?
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, serviceStr.split("<#>")[1]);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

}
