package com.digitalpetri.presentation;

import org.junit.jupiter.api.*;
import ru.otus.modbusTcpManager.ModbusTcpManagerImpl;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPresentation {

    ModbusTcpManagerImpl tcpManager = new ModbusTcpManagerImpl();;

    @BeforeEach
    void setup() throws Exception {
        tcpManager.setup();
    }

    @AfterEach
    void stop() throws Exception {
        tcpManager.stop();
    }

    @Disabled
    @Test
    @DisplayName("Чтение Int из ПЛК")
    void readInt() throws Exception {
        var holding = tcpManager.readHolding(1, 4042, 1);
        ArrayList<Integer> freeArray = new ArrayList<>();
        freeArray.add(10);
        assertEquals(holding, freeArray);
    }

    @Disabled
    @Test
    @DisplayName("Запись Int в ПЛК")
    void wrInt() throws Exception {
        int address = 4041; //адрес куда будем записывать и считывать для проверки
        int val = 11;       //значение для записи

        var wrireArrayList = new ArrayList<Integer>();
        wrireArrayList.add(val);
        tcpManager.writeHolding(1, address, wrireArrayList.size(), wrireArrayList);

        var holding = tcpManager.readHolding(1, address, 1);
        assertEquals(wrireArrayList, holding);
    }

    //@Disabled
    @Test
    @DisplayName("Чтение Float из ПЛК")
    void read() throws Exception {
        var floats = tcpManager.readFloat(1, 4033, 2);
        ArrayList<Float> freeArray = new ArrayList<>();
        freeArray.add(100.0f);
        assertEquals(floats, freeArray);
    }
}
