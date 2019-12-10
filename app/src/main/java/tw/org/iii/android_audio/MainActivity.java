package tw.org.iii.android_audio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private AudioManager audioManager;
    private SoundPool soundPool;
    int h1,h2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        h1 = soundPool.load(this, R.raw.h1, 1);
        h2 = soundPool.load(this, R.raw.h2, 1);
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
}
