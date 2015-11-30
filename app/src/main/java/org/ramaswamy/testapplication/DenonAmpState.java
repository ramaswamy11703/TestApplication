package org.ramaswamy.testapplication;

import android.renderscript.ScriptGroup;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.transform.Source;

/**
 * Created by ramaswamy on 11/29/15.
 * Represent the state of the zones, their volumes and their inputs
 */
public class DenonAmpState {
    final String INPUTSELECT = "InputFuncSelect";
    final String VOLUME = "MasterVolume";
    final String ZONEPOWER = "ZonePower";
    final String VALUE = "value";
    final String NS = "";

    public static class ZoneState {
        ZoneEnums zone;
        int volume;
        SourceEnums source;
        boolean onOff;

        ZoneState(ZoneEnums z) {
            zone = z;
        }
        ZoneState(ZoneEnums z, int v, SourceEnums s, boolean current) {
            zone = z;
            volume = v;
            source = s;
            onOff = current;
        }
    }




    ZoneState[] zoneStates;
    DenonAmpState() {
        zoneStates = new ZoneState[ZoneEnums.values().length];
        for (ZoneEnums zoneEnum : ZoneEnums.values()) {
            zoneStates[zoneEnum.ordinal()] = null;
        }
    }

    boolean initDone() {
        boolean alldone = true;
        for (ZoneEnums zenum : ZoneEnums.values()) {
            alldone = alldone && (zoneStates[zenum.ordinal()] != null);
        }
        return alldone;
    }

    ZoneState getZoneState(ZoneEnums zenum) {
        return zoneStates[zenum.ordinal()];
    }

    void updateInput(ZoneEnums zone, SourceEnums source) {
        zoneStates[zone.ordinal()].source = source;
    }

    void updateVolume(ZoneEnums zone, int vol) {
        zoneStates[zone.ordinal()].volume = vol;
    }

    void parseFromXml(String xmlInput, ZoneEnums zone) throws XmlPullParserException, IOException {
        String source = "";
        String volume = "0";
        String power = "";

        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(new StringReader(xmlInput));

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String tag = parser.getName();
            int eventType = parser.getEventType();
            if (eventType == XmlPullParser.TEXT) {
                continue;
            } else if (tag.equals(INPUTSELECT)) {
                source = getField(INPUTSELECT, parser);
            } else if (tag.equals(VOLUME)) {
                volume = getField(VOLUME, parser);
            } else if (tag.equals(ZONEPOWER)) {
                power = getField(ZONEPOWER, parser);
            } // else don't care
        }
        SourceEnums sEnum = SourceEnums.getSource(source);
        // this is 80 based
        int v = (int)Double.parseDouble(volume) + 80;
        boolean p = true;
        if (power.equalsIgnoreCase("Off")) {
            p = false;
        }
        zoneStates[zone.ordinal()] = new ZoneState(zone, v, sEnum, p);
    }

    String getField(String tagName, XmlPullParser parser)
            throws XmlPullParserException, IOException {
        String field = "";
        parser.require(XmlPullParser.START_TAG, NS, tagName);
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, NS, VALUE);
        if (parser.next() == XmlPullParser.TEXT) {
            field = parser.getText();
        }
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, NS, VALUE);
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, NS, tagName);
        return field;
    }

    static void testMethod() {
        try {
            DenonAmpState das = new DenonAmpState();
            String xmlString = "<item><RenameZone><value>living</value></RenameZone>\n" +
                    "    <InputFuncSelect>\n" +
                    "    <value>chromecast</value></InputFuncSelect>\n" +
                    "    <VolumeDisplay>\n" +
                    "    <value>Absolute</value>\n" +
                    "    </VolumeDisplay>\n" +
                    "    <MasterVolume><value>-10</value></MasterVolume>\n" +
                    "    <Mute><value>off</value></Mute></item>";
            das.parseFromXml(xmlString, ZoneEnums.MAINZONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
