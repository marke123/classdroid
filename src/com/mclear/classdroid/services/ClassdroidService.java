
package com.mclear.classdroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.mclear.classdroid.threads.UploaderThread;

public class ClassdroidService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        long postId = intent.getLongExtra("id", 0);
        UploaderThread thread = new UploaderThread(postId, this);
        thread.start();
    }

}
