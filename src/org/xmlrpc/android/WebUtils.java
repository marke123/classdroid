package org.xmlrpc.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.primaryt.classdroid.bo.Pupil;
import com.primaryt.classdroid.bo.PupilServices;

public class WebUtils {
	@SuppressWarnings("unchecked")
	public static String uploadPostToWordpress(Pupil pupil, String image,
			String grade, PupilServices service, Context context, String note)
			throws XMLRPCException {

		// String categories = "";

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
		content.put("description", prepareBodyOfPost(grade, imageURL, image, note));
		/*
		 * Vector<String> cats = new Vector<String>(); cats.add("classdroid");
		 * cats.add("stuff"); cats.add("Kumar"); content.put("categories",
		 * cats);
		 */
		String username = "";
		String password = "";
		if (service.getUseDefault() == PupilServices.USE_DEFAULT) {
			username = IConstants.USER1;
			password = IConstants.PASS1;
		} else {
			username = service.getUsername();
			password = service.getPassword();
		}
		Object[] params = { 1, username, password, content, true };

		Object result = null;
		try {
			result = (Object) client.call("metaWeblog.newPost", params);
			File file = new File(image);
			file.delete();
		} catch (XMLRPCException e) {
			throw e;
		}
		return getPostUrl(result.toString(), service.getUrl());
	}

	private static String uploadImage(String image, PupilServices service,
			Context context) throws XMLRPCException {
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
		Object[] params = { 1, username, password, m };

		Object result = null;

		try {
			result = (Object) client.call("wp.uploadFile", params);
			// Don't delete the image now. The image should be deleted only
			// after the post upload is successful
			// jpeg.delete();
		} catch (XMLRPCException e) {
			throw e;
		}
		HashMap<String, Object> contentHash = new HashMap<String, Object>();
		contentHash = (HashMap<String, Object>) result;
		resultUrl = contentHash.get("url").toString();
		return resultUrl;
	}

	private static String prepareBodyOfPost(String grade, String imageURL,
			String image, String note) {
		StringBuffer body = new StringBuffer();

		String imageTag = getImageTag(image, imageURL, note);

		body.append(imageTag);
		body.append("\nGrade for assignment : " + grade);
		System.out.println(body.toString());
		String formattedString = body.toString();
		formattedString = formattedString.replace("/\n\n/g", "</p><p>");
		formattedString = formattedString.replace("/\n/g", "<br />");
		return formattedString;
	}

	private static String getImageTag(String image, String imageURL, String note) {
		Bitmap bitmap = BitmapFactory.decodeFile(image);
		int bmpHeight = bitmap.getHeight();
		int bmpWidth = bitmap.getWidth();
		if (bmpHeight == bmpWidth) {
			// Square image
			if (bmpHeight > 400) {
				bmpHeight = 400;
				bmpWidth = 400;
			}
		} else if (bmpHeight > bmpWidth) {
			// Portrait image
			if (bmpHeight > 400) {
				int bmpNewHeight = 400;
				int bmpNewWidth = bmpNewHeight * bmpWidth / bmpHeight;

				bmpHeight = bmpNewHeight;
				bmpWidth = bmpNewWidth;
			}
		} else if (bmpWidth > bmpHeight) {
			// Landscape image
			if (bmpWidth > 400) {
				int bmpNewWidth = 400;
				int bmpNewHeight = bmpNewWidth * bmpHeight / bmpWidth;

				bmpHeight = bmpNewHeight;
				bmpWidth = bmpNewWidth;
			}
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("[caption id=\"attachment_200\" ");
		builder.append("align=\"aligncenter\" width=\""+bmpWidth+"\" ");
		builder.append("caption=\""+note+"\"]");
		builder.append("<a href=\""+imageURL+"\">");
		builder.append("<img src=\""+imageURL+"\" ");
		builder.append("class=\"size-full wp-image-200\" title=\"Uploaded from Classdroid\" ");
		builder.append("style=\"width:"+bmpWidth+"px;height:"+bmpHeight+"px\"></a>");
		builder.append("[/caption]");
		
		return builder.toString();
	}

	public static boolean uploadPostToXPArena(PupilServices service,
			Pupil pupil, String url, String score) {
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
