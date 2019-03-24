package ro.pub.cs.systems.eim.practicaltest01;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.service.autofill.Dataset;

import java.util.Date;

public class ProcessingThread extends Thread {


    private double avg;
    private double geo;
    private Context context;

    public ProcessingThread(Context context, int a, int b) {

        this.avg = (a + b) / 2.0;
        this.geo = Math.sqrt(a * b);
        this.context = context;
    }

    @Override
    public void run() {

        while (true)
        {

            Intent intent = new Intent();
            intent.putExtra("message", new Date(System.currentTimeMillis()) + " " + this.avg + " " + this.geo);
            intent.setAction(Constants.actionTypes[0]);

            this.context.sendBroadcast(intent);

            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }

    }
}
