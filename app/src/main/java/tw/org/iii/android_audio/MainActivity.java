package tw.org.iii.android_audio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private AudioManager audioManager;
    private SoundPool soundPool;
    int h1,h2;
    private File musicDir, sdroot;
    private MediaRecorder mediaRecorder;
    private MyReceiver receiver;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        else {
            init();
        }

    }

    private void init() {

        //儲存路徑
        musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        sdroot = Environment.getExternalStorageDirectory();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        h1 = soundPool.load(this, R.raw.h1, 1);
        h2 = soundPool.load(this, R.raw.h2, 1);
        seekBar = findViewById(R.id.seekBar);
        receiver = new MyReceiver();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Intent intent = new Intent(MainActivity.this, MyService.class);
                    intent.putExtra("cmd", "seekto");
                    intent.putExtra("nowpos", "seekto");
                    startService(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter("PLAY_NOW"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    public void playSystemSound(View view) {
        audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
    }

    public void PlaySound1(View view) {
        soundPool.play(h1, 1f, 1f, 1, 0, 1);
    }

    public void PlaySound2(View view) {
        soundPool.play(h2, 1f, 1f, 1, 0, 1);
    }

    public void StartRecord(View view) {
//        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(musicDir, "DCH.amr")));
//        startActivityForResult(intent, 777);
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //設定輸出檔案格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //解碼方式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //輸出路徑
            mediaRecorder.setOutputFile(new File(sdroot, "DCH.3gp").getAbsolutePath());

            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {

        }
    }

    public void StopRecord(View view) {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("DCH", "OK");
    }


    public void playMusic(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("cmd", "play");
        startService(intent);
    }

    public void stopMusic(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("cmd", "pause");
        startService(intent);
    }

    public void resetMusic(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    //廣播接收器
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("now", -1);
            int len = intent.getIntExtra("len", -1);
            if (len>=0) {
                seekBar.setMax(len);
            }
            else if (pos >= 0) {
                seekBar.setProgress(pos);
            }
        }
    }
}
