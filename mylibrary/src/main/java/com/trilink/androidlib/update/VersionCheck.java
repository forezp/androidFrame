package com.trilink.androidlib.update;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.trilink.androidlib.Constant;
import com.trilink.androidlib.R;
import com.trilink.androidlib.widget.IphoneDialog;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * vESIONcHECK
 * HOW  to use it?
 *   VersionCheck vc = new VersionCheck(AboutUsActivity.this);
 *    vc.startCheckVersion(2);
 *
 *  UPDATE_PATH=http://txyjk120.com/update/updateinfo.xml
 *	content:
 *  see the drawable : update_info.png
 *
 *
 *
 *
 */
public class VersionCheck {
	private final String TAG = this.getClass().getName();

	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 5;

	private final int UPDATA_NONEED = 0;

	private final int UPDATA_CLIENT = 1;

	private final int GET_UNDATAINFO_ERROR = 2;

	private final int SDCARD_NOMOUNTED = 3;

	private final int DOWN_ERROR = 4;

	private Activity mContext;

	private int tiShi = 0;

	private File file;

	private static UpdateInfo info;

	private String localVersion;

	/* 是否取消更新 */
	private boolean cancelUpdate = false;
	/* 是否取消更新 */
	private boolean cancelByUser = false;

	public MyVersionDialog dlg;

	public VersionCheck(Activity context)
	{
		this.mContext = context;
	}

	/*
	 * 监测版本方法调用
	 */
	public void startCheckVersion(int isTishi)
	{
		if (isTishi != 0)
		{
			try
			{
				localVersion = getVersionName();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			tiShi = isTishi;
			CheckVersionTask cv = new CheckVersionTask();
			new Thread(cv).start();
		}
	}

	/*
	 * 获取当前程序的版本号
	 */
	private String getVersionName()
			throws Exception
			{
		// 获取packagemanager的实例
		PackageManager packageManager = mContext.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
		return packInfo.versionName;
			}

	/*
	 * 从服务器获取xml解析并进行比对版本号
	 */
	public class CheckVersionTask implements Runnable
	{
		public void run()
		{
			try
			{
				// 从资源文件获取服务器 地址
				//              String path = mContext.getResources().getString(
				//                      R.string.url_server);
				String path = Constant.UPDATE_PATH;
				// 包装成url的对象
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setConnectTimeout(5000);
				InputStream is = conn.getInputStream();
				info = getUpdataInfo(is);
				System.out.println("info = " + info);
				if (info.getVersion().equals(localVersion))
				{
					Log.i(TAG, "版本号相同无需升级");
					Message msg = new Message();
					msg.what = UPDATA_NONEED;
					handler.sendMessage(msg);

				}
				else
				{
					Log.i(TAG, "版本号不同 ,提示用户升级 ");
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			}
			catch (Exception e)
			{
				// 待处理
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}

	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
//			super.handleMessage(msg);
//			//Utils.dismissProgressDialog();
			switch (msg.what)
			{
			case UPDATA_NONEED:
				if (tiShi > 1)
				{
					Toast.makeText(mContext, "已经是最新版本！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case UPDATA_CLIENT:
				// 对话框通知用户升级程序
				// Toast.makeText(getApplicationContext(), "可以升级程序啦~",
				// 1).show();
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				// 服务器超时
				Toast.makeText(mContext, "获取服务器更新信息失败",
						Toast.LENGTH_SHORT).show();
				break;
			case SDCARD_NOMOUNTED:
				// sdcard不可用
				Toast.makeText(mContext, "SD卡不可用",
						Toast.LENGTH_SHORT).show();
				break;
			case DOWN_ERROR:
				// 下载apk失败
				Toast.makeText(mContext, "下载新版本失败",
						Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk(file);
				break;
			}
		}
	};

	/*
	 * 
	 * 弹出对话框通知用户更新程序
	 * 
	 * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	protected void showUpdataDialog()
	{

		final Dialog dialog= IphoneDialog.getTwoBtnDialog(mContext, "升级提醒", "检测到新版本，立即更新吗?");
		Button btnOk=(Button)dialog.findViewById(R.id.ok);
		Button btnCancel=(Button)dialog.findViewById(R.id.cancel);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "下载apk,更新");
				downLoadApk();
				dialog.dismiss();

			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelByUser = true;
				dialog.dismiss();
			}
		});
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					if(dlg!=null&&dlg.isShowing()){
						VersionCheck.this.setCancel(true);
						dlg.dismiss();
						return true;
					}
				}
				return false;
			}
		});
		dialog.show();
		
//		dlg = new MyVersionDialog(mContext, R.style.dialogNeed, new MyVersionDialog.PriorityListener()
//		{
//			public void refreshPriorityUI()
//			{
//				Log.i(TAG, "下载apk,更新");
//				downLoadApk();
//			}
//
//			@Override
//			public void cancelPriority() {
//				// TODO Auto-generated method stub
//				cancelByUser = true;
//			}
//		});
//		dlg.setOnKeyListener(new OnKeyListener() {
//
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//				// TODO Auto-generated method stub
//				if (keyCode == KeyEvent.KEYCODE_BACK
//						&& event.getAction() == KeyEvent.ACTION_DOWN) {
//					if(dlg!=null&&dlg.isShowing()){
//						VersionCheck.this.setCancel(true);
//						dlg.dismiss();
//						return true;
//					}
//				}
//				return false;
//			}
//		});
//		dlg.show();
	}

	/*
	 * 从服务器中下载APK
	 */
	protected void downLoadApk()
	{
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(mContext);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.setCancelable(false);
		pd.setButton("取消", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				cancelUpdate = true;
				cancelByUser = true;
				dialog.dismiss();
			}
		});
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			Message msg = new Message();
			msg.what = SDCARD_NOMOUNTED;
			handler.sendMessage(msg);
		}
		else
		{
			pd.show();
			new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						file = getFileFromServer(info.getUrl(), pd);
						pd.dismiss(); // 结束掉进度条对话框
					}
					catch (Exception e)
					{
						Message msg = new Message();
						msg.what = DOWN_ERROR;
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	// 安装apk
	protected void installApk(File file)
	{
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		mContext.startActivity(intent);
		System.exit(0);
	}

	/**
	 * 从服务器下载apk
	 * 
	 * @param path
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	private File getFileFromServer(String path, ProgressDialog pd)
			throws Exception
			{
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			cancelUpdate = false;
			cancelByUser = false;
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(5000);
			// 获取到文件的大小
			pd.setMax(conn.getContentLength()/1024);
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(), info.getApkName());
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			int count = 0;
			byte[] buffer = new byte[1024];
			do
			{
				int numread = is.read(buffer);
				count += numread/1024;
				pd.setProgress(count);
				// 更新进度
				if (numread <= 0)
				{
					//下载完成，安装APK
					handler.sendEmptyMessage(DOWNLOAD_FINISH);
					break;
				}
				// 写入文件
				fos.write(buffer, 0, numread);
			} while (!cancelUpdate);
			fos.close();
			bis.close();
			is.close();
			return file;
		}
		else
		{
			return null;
		}
			}

	/*
	 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
	 */
	private UpdateInfo getUpdataInfo(InputStream is)
			throws Exception
			{
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");// 设置解析的数据源
		int type = parser.getEventType();
		UpdateInfo info = new UpdateInfo();// 实体
		while (type != XmlPullParser.END_DOCUMENT)
		{
			switch (type)
			{
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName()))
				{
					info.setVersion(parser.nextText()); // 获取版本号
				}
				else if ("url".equals(parser.getName()))
				{
					info.setUrl(parser.nextText()); // 获取要升级的APK文件
				}
				else if ("description".equals(parser.getName()))
				{
					info.setDescription(parser.nextText()); // 获取该文件的信息
				}
				else if ("apkName".equals(parser.getName()))
				{
					info.setApkName(parser.nextText()); // 获取该文件的信息
				}
				break;
			}
			type = parser.next();
		}
		return info;
			}

	public boolean getCancel(){
		return this.cancelByUser;
	}
	public void setCancel(boolean b){
		this.cancelByUser = b;
	}

}