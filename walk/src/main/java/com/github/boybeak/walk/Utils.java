package com.github.boybeak.walk;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Utils {
    public static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public static int bytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static byte[] longToBytes(long value) {
        return ByteBuffer.allocate(8).putLong(value).array();
    }

    public static byte[] floatToBytes(float value) {
        return ByteBuffer.allocate(8).putFloat(value).array();
    }

    public static byte[] doubleToBytes(double value) {
        return ByteBuffer.allocate(8).putDouble(value).array();
    }

    public static byte[] stringToBytes(String value) {
        return value.getBytes(Charset.forName("ASCII"));
    }

    public static String bytesToString(byte[] bytes) {
        return new String(bytes, Charset.forName("ASCII"));
    }

    public static byte[] nodeToBytes(Node node) {
        return ByteBuffer.allocate(32)
                .putLong(node.getTimestamp())
                .putDouble(node.getLatitude())
                .putDouble(node.getLongitude())
                .putDouble(node.getAltitude())
                .array();
    }

    public static Node bytesToNode(byte[] bytes) {
        if (bytes.length != 32) {
            throw new IllegalStateException("Illegal bytes");
        }

        long timestamp = ByteBuffer.wrap(bytes, 0, 8).getLong();
        double latitude = ByteBuffer.wrap(bytes, 8, 8).getDouble();
        double longitude = ByteBuffer.wrap(bytes, 16, 8).getDouble();
        double altitude = ByteBuffer.wrap(bytes, 24, 8).getDouble();
        return new Node(timestamp, latitude, longitude, altitude);
    }

}
