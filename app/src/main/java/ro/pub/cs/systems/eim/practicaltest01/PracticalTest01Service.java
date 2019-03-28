package ro.pub.cs.systems.eim.practicaltest01;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PracticalTest01Service extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int a = Integer.parseInt(intent.getStringExtra("press_me_count"));
        int b = Integer.parseInt(intent.getStringExtra("press_me_too_count"));

        ProcessingThread processingThread = new ProcessingThread(getApplicationContext(), a,b);
        processingThread.start();

        return Service.START_REDELIVER_INTENT;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
