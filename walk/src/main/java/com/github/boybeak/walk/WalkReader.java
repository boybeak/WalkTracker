package com.github.boybeak.walk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class WalkReader {

    public static WalkReader newInstance() {
        return new WalkReader();
    }

    private WalkReader() {

    }

    public Walk read(File file) {

        try {
            Walk walk = new Walk();

            FileInputStream inputStream = new FileInputStream(file);
            byte[] versionBytes = new byte[6];
            inputStream.read(versionBytes);
            walk.setVersion(Utils.bytesToString(versionBytes));

            byte[] sizeBytes = new byte[4];
            inputStream.read(sizeBytes);
            int size = Utils.bytesToInt(sizeBytes);
            walk.setSize(size);

            byte[] timezoneIdSizeBytes = new byte[4];
            inputStream.read(timezoneIdSizeBytes);
            int timezoneSize = Utils.bytesToInt(timezoneIdSizeBytes);

            byte[] timeZoneIdBytes = new byte[timezoneSize];
            inputStream.read(timeZoneIdBytes);
            String timeZoneId = Utils.bytesToString(timeZoneIdBytes);
            walk.setTimeZone(TimeZone.getTimeZone(timeZoneId));

            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                byte[] nodeBytes = new byte[32];
                if (inputStream.read(nodeBytes) != -1) {
                    nodes.add(Utils.bytesToNode(nodeBytes));
                }
            }
            walk.setNodes(nodes);

            inputStream.close();

            return walk;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
