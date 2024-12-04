package ru.otus.modbusTcpManager.converter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ModbusDataConverter {
    public static ArrayList<Integer> byteToInteger(byte[] b) {
        ArrayList<Integer> arrayList = new ArrayList<>();

        for (int i = 0; i < b.length; i += 2) {
            byte b1 = b[i];
            byte b2 = b[i + 1];
            arrayList.add(((b1 & 0xFF) << 8) | (b2 & 0xFF));
        }
        return arrayList;
    }

    public static byte[] integerToByte(ArrayList<Integer> al) {
        byte[] b = new byte[al.size() * 2];
        int j = 0;
        for (int i = 0; i < al.size(); i++) {
            b[j] = (byte) ((al.get(i) >> 8) & 0xFF);
            b[j + 1] = (byte) (al.get(i) & 0xFF);
            j += 2;
        }
        return b;
    }

    public static ArrayList<Float> byteToFloat(byte[] b) {
        ArrayList<Float> arrayList = new ArrayList<>();
        byte[] tempByte = new byte[4];
        for (int i = 0; i < b.length; i += 4) {
            tempByte[0] = b[i];
            tempByte[1] = b[i + 1];
            tempByte[2] = b[i + 2];
            tempByte[3] = b[i + 3];
            arrayList.add(ByteBuffer.wrap(tempByte).getFloat());
        }
        return arrayList;
    }
}
