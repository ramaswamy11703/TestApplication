package org.ramaswamy.testapplication;

/**
 * Created by ramaswamy on 11/28/15.
 * simple enum for representing the three zones in the amp we care about
 */
public enum ZoneEnums {
    MAINZONE(0), ZONE2(1), ZONE3(2);

    private final int zoneNumber;
    ZoneEnums(int zNumber) {
        zoneNumber = zNumber;
    }

    final String[] zoneNames = {"Main Zone", "Zone2", "Zone3"};
    String getName() {
        return zoneNames[zoneNumber];
    }
}
