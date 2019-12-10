package tw.org.iii.android_audio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    public void playSound(View view) {
        audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
    }
}
