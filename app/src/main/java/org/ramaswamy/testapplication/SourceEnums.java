package org.ramaswamy.testapplication;

/**
 * Created by ramaswamy on 11/28/15.
 * simple enum for setting amp sources
 */
public enum SourceEnums {
    Comcast(0), Chromecast(1), Sonos(2);
    private final int source;
    final String[] sourceStrings = {"SAT/CBL", "DVD", "CD"};
    SourceEnums(int s) {
        source = s;
    }
    String getSource(){
        return sourceStrings[source];
    }

    static SourceEnums getSource(String sourceName) {
        if (sourceName.equalsIgnoreCase("comcast")) {
            return Comcast;
        } else if (sourceName.equalsIgnoreCase("chromecast")) {
            return Chromecast;
        } else return Sonos;
    }

}
