package de.mytfg.jufo.mfakegps;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void play(View view){

    }

    public void rec(View view){
        if(play) {
            // Warn user by toast
            return;
        }
        Intent intent_rec = new Intent(this, FakeGPSRec.class);
        if(rec) {
            stopService(intent_rec);
        }
        else {
            startService(intent_rec);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
