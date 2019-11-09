package com.yjzfirst.util;


import android.content.Context;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *@ author MailiYjz
 *@ description 退出接口 本项目未使用
 *@ time 2018/12/11 17:56
*/
public class UserLogout extends AsyncTask<String, Void, Void> {
	Context context;

	public String ip_key = "ip";
	public String port_key = "port";
	public UserLogout(Context context) {
		this.context=context;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			System.out.println("user logout");
			String httpPost = "http://" +
					PreferencesUtils.getString(context, ip_key, "120.27.2.177")
					+ ":" + PreferencesUtils.getString(context, port_key, "8069") +
					IndexConstants.LOGOUTURL+"?";
			URL logoutUrl = new URL(httpPost);
			HttpURLConnection conn = (HttpURLConnection) logoutUrl
					.openConnection();

			conn.setConnectTimeout(10000);
			InputStream inStream = conn.getInputStream();
			inStream.close();
		} catch (Exception e) {
			// TODO: handle exception
			// System.out.println("未能获取网络数据");
			e.printStackTrace();
		}

		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);


	}
}
