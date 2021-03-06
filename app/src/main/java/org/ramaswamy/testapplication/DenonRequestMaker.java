package org.ramaswamy.testapplication;

import android.webkit.URLUtil;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xml.sax.XMLReader;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by ramaswamy on 11/26/15.
 * http://10.0.0.30/MainZone/index.put.asp?cmd0=PutZone_OnOff/OFF&cmd1=aspMainZone_WebUpdateStatus/&ZoneName=Zone3
 * put packets for managing denon remote
 cmd0 PutZone_InputFunction/ one of: SAT/CBL DVD CD
 cmd1 aspMainZone_WebUpdateStatus/
 ZoneName Main Zone

 cmd0 PutMasterVolumeSet/0 (80 for main, 70 for other two zones)
 cmd1 aspMainZone_WebUpdateStatus/
 ZoneName Main Zone

 cmd0 PutZone_OnOff/OFF
 cmd1 aspMainZone_WebUpdateStatus/
 ZoneName Main Zone
 http://10.0.0.30/goform/formMainZone_MainZoneXml.xml?ZoneName=Main
 */
public class DenonRequestMaker {

    private final String ipAddress = "http://10.0.0.30/";
    private final String statusUrl = ipAddress + "goform/formMainZone_MainZoneXml.xml?ZoneName=";
    private final String updateUrl = ipAddress + "MainZone/index.put.asp?cmd1=aspMainZone_WebUpdateStatus/";

    RequestQueue queue;
    final MyActivity myActivity;

    DenonRequestMaker(final MyActivity myActivity) {
        this.myActivity = myActivity;
    }

    void init() {
        queue = Volley.newRequestQueue(myActivity);
    }

    void updateAllZones() {
        // myActivity.resetTextView();
        for (ZoneEnums zenum : ZoneEnums.values()) {
            updateStateForZone(zenum);
        }
    }

    void updateStateForZone(ZoneEnums zenum) {
        String url = ipAddress + "goform/formMainZone_MainZoneXml.xml?ZoneName=" +
                zenum.getName();
        final ZoneEnums z = zenum;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final TextView tv = (TextView) myActivity.findViewById(R.id.textview);
                        try {
                            myActivity.ampState.parseFromXml(response, z);
                            myActivity.updateFromState(z);

                            tv.append("Read state for zone " + z.getName() + "\n");
                        } catch (Exception e) {
                            tv.append("Could not read state for zone " + z.getName() + "\n");
                            tv.append("stack trace: " + e.toString() + "\n");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final TextView tv = (TextView) myActivity.findViewById(R.id.textview);
                tv.append("Could not read state for zone " + z.getName() + "\n");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    void issueVolleyRequest(String url, final String successMessage, final String failureMessage) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final TextView tv = (TextView)myActivity.findViewById(R.id.textview);
                        tv.append(successMessage);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        final TextView tv = (TextView) myActivity.findViewById(R.id.textview);
                        tv.append(failureMessage);
                    }
                });
        queue.add(stringRequest);
    }

    void updateVolume(ZoneEnums zenum, int value) {
        if (zenum == ZoneEnums.MAINZONE) value = value - 80;
        else value = value - 70;

        String url = updateUrl + "&cmd0=PutMasterVolumeSet/" + value + "&ZoneName=" +
                zenum.getName();
        String msg = value + " for zone " + zenum.getName() + "\n";
        issueVolleyRequest(url, "Updated volume to " + msg,
                "Failed updating volume to " + msg);
    }

    void updateSource(ZoneEnums zenum, SourceEnums senum) {
        if ((senum == SourceEnums.Unknown) && (myActivity.ampState.initDone())) {
            final TextView tv = (TextView) myActivity.findViewById(R.id.textview);
            tv.append("Please set input to valid source");
            updateStateForZone(zenum);
            return;
        }

        String url = updateUrl + "&cmd0=PutZone_InputFunction/" + senum.getSource() + "&ZoneName=" +
                zenum.getName();
        String msg = senum.getDisplaySource() + " for zone " + zenum.getName() + "\n";
        issueVolleyRequest(url, "Updated source to " +  msg,
                "Failure updating source to " + msg);
    }
}
