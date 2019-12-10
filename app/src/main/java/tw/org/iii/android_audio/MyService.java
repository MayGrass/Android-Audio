package tw.org.iii.android_audio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

public class MyService extends Service {
    private File sdroot;
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    //第一次按才執行，第二次後都是onStart
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("DCH", "OnCreate");

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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
        }
        Log.v("DCH", "onDestroy");
    }
}
