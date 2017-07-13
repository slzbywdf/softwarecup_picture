package com.picture.picture_manager.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public static void sendHttpRequest(final String address, final String params, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    //connection.setRequestProperty("Content-type", "text/html");
                    connection.setRequestProperty("Accept-Charset", "utf-8");
                    //connection.setRequestProperty("contentType", "utf-8");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //登录后每次发送请求加上session id
                    if (GlobalFlags.isLoggedIn()) {
                        if (GlobalFlags.getSessionId() != null && GlobalFlags.getSessionId().length() != 0)
                            connection.setRequestProperty("cookie", GlobalFlags.getSessionId());
                    }

                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
                    out.write(params);//格式为param1=value1&param2=value2...
                    out.flush();
                    out.close();
                    //PrintWriter printWriter=new PrintWriter(connection.getOutputStream());
                    //printWriter.write(params);//格式为param1=value1&param2=value2...
                    //printWriter.flush();

                    InputStream in = connection.getInputStream();
                    //第一次登录时记下seesion id
                    if (GlobalFlags.isLoggedIn() == false) {
                        String cookieValue = connection.getHeaderField("set-cookie");
                        if (cookieValue != null) {
                            GlobalFlags.setSessionId(cookieValue.substring(0, cookieValue.indexOf(";")));
                            //获得到sessionId之后，可以将sessionId保存
                        }
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    if ((line = reader.readLine()) != null)
                        response.append(line);
                    while ((line = reader.readLine()) != null) {
                        response.append("\n" + line);
                    }
                    in.close();
                    //Log.d("Get from server", "message: " + response.toString());
                    if (listener != null) {// 回调onFinish()方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {// 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
