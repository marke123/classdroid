package com.primaryt.classdroid.threads;

import java.util.ArrayList;

import org.xmlrpc.android.WebUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.primaryt.classdroid.R;
import com.primaryt.classdroid.bo.Post;
import com.primaryt.classdroid.bo.Pupil;
import com.primaryt.classdroid.bo.PupilServices;
import com.primaryt.classdroid.db.DBAdapter;
import com.primaryt.classdroid.services.ClassdroidService;
import com.primaryt.classdroid.temp.AppUtils;

public class UploaderThread extends Thread {
	public static final int MESSAGE_DONE = 2;

	private long postId;

	private Context context;

	private Handler handler;

	public UploaderThread(long postId, Context context, Handler handler) {
		this.postId = postId;
		this.context = context;
		this.handler = handler;
	}

	@Override
	public void run() {
		DBAdapter dbAdapter = new DBAdapter(context);
		try {
			NotificationManager nManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nManager.cancel((int) (R.string.app_name + postId));
			nManager.cancel(R.string.app_name);

			dbAdapter.open();
			ArrayList<Post> posts = dbAdapter.getPostsById(postId);
			dbAdapter.close();
			long postID = posts.get(0).getId();
			long pupilId = posts.get(0).getPupilId();
			String localImagePath = posts.get(0).getLocalImagePath();
			String grade = posts.get(0).getGrade();
			String note = posts.get(0).getNote();

			ArrayList<Pupil> pupils = new ArrayList<Pupil>();

			for (Post post : posts) {
				dbAdapter.open();
				Pupil pupil = dbAdapter.getPupilById(post.getPupilId());
				dbAdapter.close();
				pupils.add(pupil);
			}
			if (postID != 1) {
				dbAdapter.open();
				PupilServices service = dbAdapter.getPupilServiceByPupilId(
						pupilId, PupilServices.TYPE_PRIMARYBLOGGER);
				dbAdapter.close();
				AppUtils utils = new AppUtils(context);
				if (service.getIsEnabled() == PupilServices.SERVICE_ENABLED) {
					String url = WebUtils.uploadPostToWordpress(pupils,
							localImagePath, "" + grade, utils.getNewURL(),
							context, note, utils.getNewUsername(),
							utils.getNewPassword());
					for (Post post : posts) {
						post.setIsPosted(1);
						post.setReturnedString(url);
						dbAdapter.open();
						dbAdapter.updatePost(post);
						dbAdapter.close();
					}
					sendNotification();
				} else {
					for (Post post : posts) {
						post.setIsPosted(1);
						post.setReturnedString("");
						dbAdapter.open();
						dbAdapter.deletePost(post.getId());
						dbAdapter.close();
					}
				}
				// dbAdapter.open();
				// service =
				// dbAdapter.getPupilServiceByPupilId(post.getPupilId(),
				// PupilServices.TYPE_XPARENA);
				// dbAdapter.close();
				// if (service.getIsEnabled() == PupilServices.SERVICE_ENABLED)
				// {
				// WebUtils.uploadPostToXPArena(service, pupil,
				// post.getReturnedString(), "500");
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
			NotificationManager nManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nManager.cancel(R.string.app_name);
			Intent intent = new Intent(context, ClassdroidService.class);
			intent.putExtra("id", postId);
			PendingIntent pendingIntent = PendingIntent.getService(context,
					(int) postId, intent, 0);
			Notification notification = new Notification(R.drawable.icon,
					context.getString(R.string.lab_classdroid_error),
					System.currentTimeMillis());
			notification.setLatestEventInfo(context,
					context.getString(R.string.lab_upload_failed),
					context.getString(R.string.lab_try_again), pendingIntent);
			nManager.notify((int) (R.string.app_name + postId), notification);
		} finally {
			dbAdapter.close();
			handler.sendEmptyMessage(MESSAGE_DONE);
		}
	}

	private void sendNotification() {
		NotificationManager nManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nManager.cancel(R.string.app_name);

		Intent intent = new Intent(context, ClassdroidService.class);
		intent.putExtra("id", postId);
		PendingIntent pendingIntent = PendingIntent.getService(context,
				(int) postId, intent, 0);
		Notification notification = new Notification(R.drawable.icon,
				context.getString(R.string.lab_post_uploaded),
				System.currentTimeMillis());
		notification.setLatestEventInfo(context,
				context.getString(R.string.lab_post_uploaded),
				context.getString(R.string.lab_assignment_uploaded),
				pendingIntent);

		nManager.notify(R.string.app_name, notification);
	}
}
