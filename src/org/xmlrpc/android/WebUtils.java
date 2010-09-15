
package org.xmlrpc.android;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.wordpress.android.MediaFile;

import android.content.Context;

import com.mclear.classdroid.bo.Pupil;
import com.mclear.classdroid.bo.PupilServices;

public class WebUtils {
    @SuppressWarnings("unchecked")
    public static String uploadPostToWordpress(Pupil pupil, String image, String grade,
            PupilServices service, Context context) throws XMLRPCException {

        String adjustedURL = service.getUrl();
        if (!adjustedURL.contains("http://")) {
            adjustedURL = "http://" + adjustedURL;
        }
        if (adjustedURL.lastIndexOf("/") < adjustedURL.length() - 1) {
            adjustedURL = adjustedURL + "/";

        }

        service.setUrl(adjustedURL);

        String imageURL = uploadImage(image, service, context);

        XMLRPCClient client = new XMLRPCClient(service.getUrl() + "xmlrpc.php");
        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("post_type", "post");
        content.put("title", pupil.getName() + "'s work");
        // content.put("title", "Assignment for " + pupil.getName());
        content.put("description", prepareBodyOfPost(grade, imageURL));
        String username = "";
        String password = "";
        if (service.getUseDefault() == PupilServices.USE_DEFAULT) {
            username = IConstants.USER1;
            password = IConstants.PASS1;
        } else {
            username = service.getUsername();
            password = service.getPassword();
        }
        Object[] params = {
                1, username, password, content, true
        };

        Object result = null;
        try {
            result = (Object) client.call("metaWeblog.newPost", params);
        } catch (XMLRPCException e) {
            throw e;
        }
        return getPostUrl(result.toString(), service.getUrl());
    }

    private static String uploadImage(String image, PupilServices service, Context context)
            throws XMLRPCException {
        String resultUrl = "";
        XMLRPCClient client = new XMLRPCClient(service.getUrl() + "xmlrpc.php");
        String sXmlRpcMethod = "wp.uploadFile";

        MediaFile mf = new MediaFile();
        String orientation = "1";
        String mimeType = "image/jpeg";
        File jpeg = new File(image);
        mf.setFilePath(jpeg.getPath());
        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put("name", jpeg.getName());
        m.put("type", mimeType);
        m.put("bits", mf);
        m.put("overwrite", true);
        String username = "";
        String password = "";
        if (service.getUseDefault() == PupilServices.USE_DEFAULT) {
            username = IConstants.USER1;
            password = IConstants.PASS1;
        } else {
            username = service.getUsername();
            password = service.getPassword();
        }
        Object[] params = {
                1, username, password, m
        };

        Object result = null;

        try {
            result = (Object) client.call("wp.uploadFile", params);
            jpeg.delete();
        } catch (XMLRPCException e) {
            throw e;
        }
        HashMap<String, Object> contentHash = new HashMap<String, Object>();
        contentHash = (HashMap<String, Object>) result;
        resultUrl = contentHash.get("url").toString();
        return resultUrl;
    }

    private static String prepareBodyOfPost(String grade, String imageURL) {
        StringBuffer body = new StringBuffer();

        body.append("<img style=\"display:block;margin-right:auto;margin-left:auto;\" src=\""
                + imageURL + "\" alt=\"image\" />");
        body.append("\nGrade for assignment : " + grade);
        System.out.println(body.toString());
        String formattedString = body.toString();
        formattedString = formattedString.replace("/\n\n/g", "</p><p>");
        formattedString = formattedString.replace("/\n/g", "<br />");
        return formattedString;
    }

    public static boolean uploadPostToXPArena(PupilServices service, Pupil pupil, String url,
            String score) {
        boolean value = false;
        HttpPost post = new HttpPost(service.getUrl());
        DefaultHttpClient client = new DefaultHttpClient();
        String username = "";
        String password = "";
        if (service.getUseDefault() == PupilServices.USE_DEFAULT) {
            username = IConstants.USER1;
            password = IConstants.PASS1;
        } else {
            username = service.getUsername();
            password = service.getPassword();
        }

        StringBuffer body = new StringBuffer();
        body.append("<?xml version=\"1.0\"?>");
        body.append("<note>");
        body.append("<username>" + username + "</username>");
        body.append("<password>" + password + "</password>");
        body.append("<url>" + url + "</url>");
        body.append("<score>" + score + "</score>");
        body.append("<nick>" + service.getNickname() + "</nick>");
        body.append("</note>");
        try {
            StringEntity entity = new StringEntity(body.toString());
            post.setEntity(entity);
            post.setHeader(HTTP.CONTENT_TYPE, "text/xml");
            // post.setHeader(HTTP.CONTENT_LEN, ""+entity.getContentLength());
            HttpResponse response = client.execute(post);
            value = true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getPostUrl(String postID, String url) {
        return url + "?p=" + postID;
    }
}
