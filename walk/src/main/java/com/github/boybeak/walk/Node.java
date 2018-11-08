package com.github.boybeak.walk;

public class Node {

    private long timestamp;
    private double latitude, longitude;
    private double altitude;

    public Node(long timestamp, double latitude, double longitude, double altitude) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public Node(double latitude, double longitude, double altitude) {
        this(System.currentTimeMillis(), latitude, longitude, altitude);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }
}