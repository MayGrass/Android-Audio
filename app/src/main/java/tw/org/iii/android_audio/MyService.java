package tw.org.iii.android_audio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private File sdroot;
    private MediaPlayer mediaPlayer;
    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    //獲得時間進度值
    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int pos = mediaPlayer.getCurrentPosition();
                sendBroadcast(new Intent("PLAY_NOW").putExtra("now", pos));
            }
        }
    }

    //第一次按才執行，第二次後都是onStart
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("DCH", "OnCreate");

        timer = new Timer();
        timer.schedule(new MyTask(), 0, 100);
        try {
            sdroot = Environment.getExternalStorageDirectory();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(sdroot.getAbsolutePath() + "/Ringtones/theme/Wild Horses.mp3");
            mediaPlayer.prepare();
        } catch (Exception e) {
            Log.v("DCH", e.toString());
            //Log.v("DCH", sdroot.getAbsolutePath());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.v("DCH", "onStartCommand");
        String cmd = intent.getStringExtra("cmd");
        if (mediaPlayer.isPlaying() && cmd.equals("pause")) {
            mediaPlayer.pause();
        }
        else if (!mediaPlayer.isPlaying() && cmd.equals("play")) {
            mediaPlayer.start();
        }
        else if (cmd.equals("seekto")) {
            int nowpos = intent.getIntExtra("nowpos", -1);
            if (nowpos >= 0) mediaPlayer.seekTo(nowpos);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        //Log.v("DCH", "onDestroy");
    }
}
