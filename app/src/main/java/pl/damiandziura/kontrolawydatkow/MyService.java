package pl.damiandziura.kontrolawydatkow;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    private Timer timer;
    private TimerTask timerTask;
    private Database BazaDanych;

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            BazaDanych.CheckCyclicalExpenses();
            BazaDanych.CheckCyclicalIncome();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        BazaDanych = new Database(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        clearTimerSchedule();
        initTask();
        timer.scheduleAtFixedRate(timerTask, 4 * 1000, 60 * 60 * 1000);
        return super.onStartCommand(intent, flags, startId);

    }

    private void clearTimerSchedule() {
        if(timerTask != null) {
            timerTask.cancel();
            timer.purge();
        }
    }

    private void initTask() {
        timerTask = new MyTimerTask();
    }

    @Override
    public void onDestroy() {
        clearTimerSchedule();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}