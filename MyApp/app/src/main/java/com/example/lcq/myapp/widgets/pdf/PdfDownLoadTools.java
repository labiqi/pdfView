package com.example.lcq.myapp.widgets.pdf;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.lcq.myapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 文件pdf下载帮助类
 * 注意点
 * 1，必须在UI线程中创建AsyncTask的实例.
 * 2，只能在UI线程中调用AsyncTask的execute方法.
 * 3，AsyncTask被重写的四个方法是系统自动调用的,不应手动调用.
 * 4，每个AsyncTask只能被执行(execute方法)一次,多次执行将会引发异常.
 */
public class PdfDownLoadTools extends AsyncTask<String, Integer, Integer> {
	private static final String TAG = PdfDownLoadTools.class.getName();
	public final static  String SDPATH = Environment.getExternalStorageDirectory() + "/";
	private File file;
	private long fileSize;
	private int flag;
	// 网络连接错误
	private final int NET_WORK_ERROR = 1;
	private OnLoadListener onLoadlistener;

	//运行在ui线程
	@Override
	protected void onPreExecute() {
		if (!file.exists()) {
			onLoadlistener.showLoading();
		}
		super.onPreExecute();
	}

	//运行在其他线程
	@Override
	protected Integer doInBackground(String... params) {
		String fileDir = params[1];
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		HttpURLConnection urlConn = null;
		try {
			File dir = new File(SDPATH + fileDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			URL url = new URL(encoderUrl(params[0]));
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(5000);
			urlConn.setRequestMethod("GET");
			urlConn.connect();
			if(Double.compare(fileSize, 0) <= 0) {
				fileSize = urlConn.getContentLength();
			}
			if (urlConn.getResponseCode() == 200) {
				if (!file.exists()||fileSize!=file.length()) {
					try{
						file.createNewFile();
					} catch (Exception e) {
						e.printStackTrace();
					}
//					 获得文件的长度
					inputStream = urlConn.getInputStream();
					outputStream = new FileOutputStream(file);
					int len = 0;
					byte[] date = new byte[4 * 1024];
					int totalLen = 0;
					while ((len = inputStream.read(date)) != -1) {
						totalLen += len;
						int value = (int) (totalLen * 100 / fileSize);
						publishProgress(value);
						outputStream.write(date, 0, len);
					}
				}
			} else {
				flag = NET_WORK_ERROR;
			}
		} catch (Exception e) {
			flag = NET_WORK_ERROR;
			e.printStackTrace();
		} finally {
			if (outputStream != null){
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (urlConn != null){
				urlConn.disconnect();
			}
			if (flag == NET_WORK_ERROR &&file.exists()) {
				file.delete();
			}
		}
		return flag;
	}

	private String encoderUrl(String urlStr) {
		String url = "";
		try {
			url = URLEncoder.encode(urlStr, "utf-8").replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage() == null ? "" : e.getMessage(), e);
		}
		url = url.replaceAll("%3A", ":").replaceAll("%2F", "/");
		return url;
	}

	//运行在ui线程
	@Override
	protected void onProgressUpdate(Integer... values) {
		//此方法通过在doInBackground 中调用publishProgress();用来实时更新进度显示。
		super.onProgressUpdate(values);
	}

	//运行在ui线程
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (flag == NET_WORK_ERROR) {
			onLoadlistener.loadFail(R.string.com_load_fail);
		}else {
			onLoadlistener.loadSuccess(Uri.fromFile(file));
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public OnLoadListener getOnLoadlistener() {
		return onLoadlistener;
	}

	public void setOnLoadListener(OnLoadListener onLoadlistener) {
		this.onLoadlistener = onLoadlistener;
	}

	public interface OnLoadListener {
		void showLoading();
		void loadSuccess(Uri uri);
		void loadFail(int resId);
	}
}
