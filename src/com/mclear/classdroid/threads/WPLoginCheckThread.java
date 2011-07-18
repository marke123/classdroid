
package com.mclear.classdroid.threads;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mclear.classdroid.utils.Parsers;

public class WPLoginCheckThread extends Thread {

    private String url;

    private String username;

    private String password;

    private boolean isOK;

    private Handler handler;

    public WPLoginCheckThread(String url, String username, String password, Handler handler) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.handler = handler;
    }

    @Override
    public void run() {
        String adjustedURL = url;
        if (!adjustedURL.contains("http://")) {
            adjustedURL = "http://" + adjustedURL;
        }
        if (adjustedURL.lastIndexOf("/") < adjustedURL.length() - 1) {
            adjustedURL = adjustedURL + "/";
        }
        if (!adjustedURL.endsWith("xmlrpc.php")) {
            adjustedURL = adjustedURL + "xmlrpc.php";
        }

        StringBuffer body = new StringBuffer();
        body.append("<?xml version='1.0' ?>");
        body.append("<methodCall>");
        body.append("<methodName>wp.getUsersBlogs</methodName>");
        body.append("<params><param><value><string>");
        body.append(username);
        body.append("</string></value></param><param><value><string>");
        body.append(password);
        body.append("</string></value></param></params></methodCall>");

        HttpClient client = new DefaultHttpClient();

        HttpPost postRequest = new HttpPost(adjustedURL);

        try {
            HttpEntity entity = new StringEntity(body.toString());
            postRequest.setEntity(entity);
            postRequest.setHeader(HTTP.CONTENT_TYPE, "text/xml");
            client.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
            HttpResponse response = client.execute(postRequest);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream stream = response.getEntity().getContent();
                isOK = Parsers.parseAndCheckLoginWP(stream);
            }
            handler.sendEmptyMessage(1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            packAndSendException(e);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            packAndSendException(e);
        } catch (IOException e) {
            e.printStackTrace();
            packAndSendException(e);
        }
    }

    private void packAndSendException(Exception e) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("ex", e.getMessage());
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public boolean isOK() {
        return isOK;
    }

}
