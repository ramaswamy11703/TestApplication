package org.ramaswamy.testapplication;

import javax.xml.transform.Source;

/**
 * Created by ramaswamy on 11/28/15.
 * simple enum for setting amp sources
 */
public enum SourceEnums {
    Comcast(0), Chromecast(1), Sonos(2), Unknown(3);
    private final int source;
    static final String[] sourceStrings = {"SAT/CBL", "DVD", "CD", ""};
    static final String[] displayStrings = {"comcast", "chromecast", "sonos", "unknown"};
    SourceEnums(int s) {
        source = s;
    }
    String getSource(){
        return sourceStrings[source];
    }
    String getDisplaySource() {
        return displayStrings[source];
    }

    static SourceEnums getSource(String sourceName) {
        if (sourceName.equalsIgnoreCase(Comcast.getDisplaySource())) {
            return Comcast;
        } else if (sourceName.equalsIgnoreCase(Chromecast.getDisplaySource())) {
            return Chromecast;
        } else if (sourceName.equalsIgnoreCase(Sonos.getDisplaySource())) {
            return Sonos;
        } else return Unknown;
    }

    static SourceEnums getSource(int index) {
        return SourceEnums.values()[index];
    }

}
