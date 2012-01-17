package com.primaryt.classdroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.primaryt.classdroid.threads.UploaderThread;

public class ClassdroidService extends Service {
	private int threadCount = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		long postId = intent.getLongExtra("id", 0);
		UploaderThread thread = new UploaderThread(postId, this, handler);
		thread.start();
		threadCount++;
	}

	private void checkIfWorkPendingAndStopService() {
		if (threadCount == 0) {
			stopSelf();
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == UploaderThread.MESSAGE_DONE) {
				threadCount--;
				checkIfWorkPendingAndStopService();
			}
		}

	};

}
