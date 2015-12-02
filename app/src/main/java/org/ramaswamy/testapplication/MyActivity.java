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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import javax.xml.transform.Source;

/*


 */
public class MyActivity extends AppCompatActivity {
    protected int callValue = 0;
    protected DenonRequestMaker drm = new DenonRequestMaker(this);
    protected DenonAmpState ampState;
    // set to true when internally taking care of things so that we don't trigger event handlers
    boolean updatingState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ampState = new DenonAmpState();
        setContentView(R.layout.activity_my);

        drm.init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int[] rids = {R.id.sliderMain, R.id.sliderZone2, R.id.sliderZone3};
        for (int id : rids) {
            SeekBar seekBar = (SeekBar) findViewById(id);
            seekBar.setTag(0);
            setupSliderEventHandler(seekBar);
        }


        int[] rids2 = {R.id.spinnerMain, R.id.spinnerZone2, R.id.spinnerZone3};
        for (int id : rids2) {
            Spinner spinner = (Spinner) findViewById(id);
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                    R.layout.support_simple_spinner_dropdown_item);
            adapter.addAll(SourceEnums.displayStrings);
            spinner.setAdapter(adapter);
            setupSpinnerEventHandler(spinner);
            spinner.setTag(SourceEnums.Unknown.ordinal());
            spinner.setSelection(SourceEnums.Unknown.ordinal());
        }

        drm.updateAllZones();

        DenonAmpState.testMethod();
    }

    void setupSliderEventHandler(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getTag().equals(progress)) {
                    return;
                }
                seekBar.setTag(progress);
                drm.updateVolume(getZoneFromSlider(seekBar), progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    void setupSpinnerEventHandler(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SourceEnums senum = SourceEnums.getSource(position);
                if (parent.getTag().equals(senum.ordinal())) return;
                parent.setTag(senum.ordinal());
                drm.updateSource(getZoneFromSpinner(parent), SourceEnums.getSource(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                drm.updateSource(getZoneFromSpinner(parent), SourceEnums.Unknown);
            }
        });
    }


    void resetTextView() {
        TextView tv = (TextView) findViewById(R.id.textview);
        tv.setText("");
    }

    void updateFromState(ZoneEnums z) {
        DenonAmpState.ZoneState zs = this.ampState.getZoneState(z);
        Spinner spinner = (Spinner) findViewById(getSpinnerIdFromZone(z));
        spinner.setTag(zs.source.ordinal());
        spinner.setSelection(zs.source.ordinal());

        SeekBar seekbar = (SeekBar) findViewById(getSliderIdFromZone(z));
        seekbar.setTag(zs.volume);
        seekbar.setProgress(zs.volume);

    }

    int getSpinnerIdFromZone(ZoneEnums z) {
        switch (z) {
            case MAINZONE: return R.id.spinnerMain;
            case ZONE2: return R.id.spinnerZone2;
            case ZONE3: return R.id.spinnerZone3;
        }
        return R.id.spinnerMain;
    }

    int getSliderIdFromZone(ZoneEnums z) {
        switch (z) {
            case MAINZONE: return R.id.sliderMain;
            case ZONE2: return R.id.sliderZone2;
            case ZONE3: return R.id.sliderZone3;
        }
        return R.id.sliderMain;
    }

    ZoneEnums getZoneFromSpinner(View parent) {
        switch (parent.getId()) {
            case R.id.spinnerMain:
                return ZoneEnums.MAINZONE;
            case R.id.spinnerZone2:
                return ZoneEnums.ZONE2;
            case R.id.spinnerZone3:
                return ZoneEnums.ZONE3;
        }
        return ZoneEnums.MAINZONE;
    }

    ZoneEnums getZoneFromSlider(View parent) {
        switch (parent.getId()) {
            case R.id.sliderMain:
                return ZoneEnums.MAINZONE;
            case R.id.sliderZone2:
                return ZoneEnums.ZONE2;
            case R.id.sliderZone3:
                return ZoneEnums.ZONE3;
        }
        return ZoneEnums.MAINZONE;
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
