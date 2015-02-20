package de.mytfg.jufo.mfakegps;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
    // Log TAG
    protected static final String TAG = "MainActivity-class";

    private Button button_rec;
    private Button button_play;

    private boolean rec = false;
    private boolean play = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_rec = (Button) findViewById(R.id.button_record);
        button_play = (Button) findViewById(R.id.button_play);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void play(View view){
        Log.i(TAG, "play()");
        if(rec) {
            // Warn user by toast
            return;
        }
        Intent intent_play = new Intent(this, FakeGPSRec.class);
        if(play) {
            stopService(intent_play);
            button_play.setText(R.string.button_play_track);
            button_rec.setEnabled(true);
            play = false;
        }
        else {
            startService(intent_play);
            button_play.setText(R.string.button_play_track_stop);
            button_rec.setEnabled(false);
            play = true;
        }
    }

    public void rec(View view){
        Log.i(TAG, "rec()");
        if(play) {
            // Warn user by toast
            return;
        }
        Intent intent_rec = new Intent(this, FakeGPSRec.class);
        if(rec) {
            stopService(intent_rec);
            button_rec.setText(R.string.button_record_track);
            button_play.setEnabled(true);
            rec = false;
        }
        else {
            startService(intent_rec);
            button_rec.setText(R.string.button_record_track_stop);
            button_play.setEnabled(false);
            rec = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
