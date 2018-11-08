package com.github.boybeak.walk;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TimeZone;

public class WalkWriter {

    private static final int MODE_CREATE = 1, MODE_APPEND = 2;

    private int mode = MODE_CREATE;

    private Node lastNode = null;
    private Queue<Node> nodeQueue = new LinkedList<>();

    private File file;
    private Header header;

    public static WalkWriter newInstance() {
        return new WalkWriter();
    }

    private WalkWriter() {
    }

    public WalkWriter start(File file) {
        this.file = file;
        if (file.exists() && file.length() > 0) {
            if (file.length() < 14) {
                throw new IllegalStateException("Not a real .walk file");
            }
            mode = MODE_APPEND;
        } else {
            mode = MODE_CREATE;
        }
        header = readHeader(file);
        if (!header.version.startsWith("walk")) {
            throw new IllegalStateException("Not a real .walk file");
        }
        lastNode = readLastNode();
        System.out.println(header);
        return this;
    }

    public WalkWriter add(double latitude, double longitude, double altitude) {
        add(new Node(latitude, longitude, altitude));
        return this;
    }

    public WalkWriter add(Node node) {
        if (lastNode != null) {
            if (lastNode.getTimestamp() > node.getTimestamp()) {
                throw new IllegalStateException("Illegal node, because of timestamp");
            }
        }
        nodeQueue.offer(node);
        lastNode = node;
        return this;
    }

    public WalkWriter flush() {
        final int queueSize = nodeQueue.size();
        header.size += queueSize;
        switch (mode) {
            case MODE_CREATE:
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    FileOutputStream fos = new FileOutputStream(file, true);
                    fos.write(header.versionBytes());
                    fos.write(header.sizeBytes());
                    fos.write(header.timezoneIdByteSizeBytes());
                    fos.write(header.timezoneIdBytes());
                    while (!nodeQueue.isEmpty()) {
                        Node node = nodeQueue.poll();
                        fos.write(Utils.nodeToBytes(node));
                    }
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case MODE_APPEND:

                try {
                    FileOutputStream fos = new FileOutputStream(file, true);
                    while (!nodeQueue.isEmpty()) {
                        Node node = nodeQueue.poll();
                        fos.write(Utils.nodeToBytes(node));
                    }
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    RandomAccessFile raf = new RandomAccessFile(file, "rw");
                    raf.seek(6);
                    raf.write(header.sizeBytes());
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return this;
    }

    public void stop() {

    }

    private Header readHeader(File file) {
        Header header = new Header();
        switch (mode) {
            case MODE_APPEND:
                try {
                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                    byte[] version = new byte[6];
                    raf.read(version);
                    header.version = Utils.bytesToString(version);

                    byte[] size = new byte[4];
                    raf.read(size);
                    header.size = Utils.bytesToInt(size);

                    byte[] timezoneBytesSize = new byte[4];
                    raf.read(timezoneBytesSize);
                    header.timezoneIdByteSize = Utils.bytesToInt(timezoneBytesSize);

                    byte[] timezoneId = new byte[header.timezoneIdByteSize];
                    raf.read(timezoneId);
                    header.timezoneId = Utils.bytesToString(timezoneId);

                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case MODE_CREATE:
                header.version = "walk18";
                header.size = 0;
                String timezoneId = TimeZone.getDefault().getID();
                byte[] timezoneBytes = Utils.stringToBytes(timezoneId);
                header.timezoneIdByteSize = timezoneBytes.length;
                header.timezoneId = timezoneId;
                break;
        }
        return header;
    }

    private Node readLastNode() {
        Node node = null;
        if (hasNodes()) {
            try {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(file.length() - 32);
                byte[] last32Bytes = new byte[32];
                raf.read(last32Bytes);
                node = parseNode(last32Bytes);
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return node;
    }

    private Node parseNode(byte[] bytes) {
        return Utils.bytesToNode(bytes);
    }

    private boolean hasNodes() {
        return header.size > 0;
    }

    private class Header {
        public int size;
        public String version;
        public int timezoneIdByteSize;
        public String timezoneId;

        public int getHeaderBytesSize() {
            return 14 + timezoneIdByteSize;
        }

        @Override
        public String toString() {
            return "{" + '\n' +
                    '\t' + "version" + '=' + version + ',' + '\n' +
                    '\t' + "size" + '=' + size + ',' + '\n' +
                    '\t' + "timezoneIdByteSize" + '=' + timezoneIdByteSize + ',' + '\n' +
                    '\t' + "timezoneId" + '=' + timezoneId + '\n' +
                    "}";
        }

        byte[] versionBytes() {
            return Utils.stringToBytes(version);
        }

        byte[] sizeBytes() {
            return Utils.intToBytes(size);
        }

        byte[] timezoneIdBytes() {
            return Utils.stringToBytes(timezoneId);
        }

        byte[] timezoneIdByteSizeBytes() {
            return Utils.intToBytes(timezoneIdByteSize);
        }

    }

}
