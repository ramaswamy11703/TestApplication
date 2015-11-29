package org.ramaswamy.testapplication;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/*


 */
public class MyActivity extends AppCompatActivity {
    protected int callValue = 0;
    protected DenonRequestMaker drm = new DenonRequestMaker(this);
    protected DenonAmpState ampState = new DenonAmpState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressListener(view);
            }
        });

        SeekBar seekBar = (SeekBar)findViewById(R.id.sliderMain);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drm.updateVolume(ZoneEnums.MAINZONE, progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        drm.init();
        // wire widgets to event handlers
        drm.updateStatus(this);

        DenonAmpState.testMethod();
    }

    protected void sliderListener(View view, int zone) {}
    protected void radioListener(View view, int source) {}
    protected void paintScreen() {}

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();
        ZoneEnums zenum = ZoneEnums.MAINZONE;
        SourceEnums senum = SourceEnums.Comcast;
        switch (view.getId()) {
            case R.id.mainCable:
                break;
            case R.id.mainCC:
                senum = SourceEnums.Chromecast;
                break;
            case R.id.mainSonos:
                senum = SourceEnums.Sonos;
                break;
        }
        drm.updateSource(zenum, senum);
    }

    protected void pressListener(View view) {
        String outputStr = "call value is " + callValue++;
        Snackbar.make(view, outputStr, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        // TextView tv = (TextView) findViewById(R.id.textWidget);
        // tv.setText(outputStr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
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
