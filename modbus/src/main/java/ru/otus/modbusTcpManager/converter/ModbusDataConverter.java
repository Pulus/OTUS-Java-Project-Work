package ru.otus.modbusTcpManager.converter;

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
}
