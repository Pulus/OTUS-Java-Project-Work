package com.digitalpetri.connect;

import static org.junit.jupiter.api.Assertions.*;

import com.digitalpetri.modbus.client.ModbusClient;
import com.digitalpetri.modbus.exceptions.ModbusExecutionException;
import com.digitalpetri.modbus.pdu.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public abstract class ClientServerIT {

    abstract ModbusClient getClient();

    @Test
    void readCoils() throws Exception {
        ReadCoilsResponse response = getClient().readCoils(1, new ReadCoilsRequest(0, 2000));

        assertEquals(250, response.coils().length);
    }

    @Test
    void readDiscreteInputs() throws Exception {
        ReadDiscreteInputsResponse response = getClient().readDiscreteInputs(1, new ReadDiscreteInputsRequest(0, 2000));

        assertEquals(250, response.inputs().length);
    }

    @Test
    void readHoldingRegisters() throws Exception {
        ReadHoldingRegistersResponse response =
                getClient().readHoldingRegisters(1, new ReadHoldingRegistersRequest(0, 125));

        assertEquals(250, response.registers().length);
    }

    @Test
    void readInputRegisters() throws Exception {
        ReadInputRegistersResponse response = getClient().readInputRegisters(1, new ReadInputRegistersRequest(0, 125));

        assertEquals(250, response.registers().length);
    }

    @Test
    void writeSingleCoil() throws Exception {
        WriteSingleCoilResponse response = getClient().writeSingleCoil(1, new WriteSingleCoilRequest(0, true));

        assertEquals(0, response.address());
        assertEquals(0xFF00, response.value());
    }

    @Test
    void writeSingleRegister() throws Exception {
        WriteSingleRegisterResponse response =
                getClient().writeSingleRegister(1, new WriteSingleRegisterRequest(0, 0x1234));

        assertEquals(0, response.address());
        assertEquals(0x1234, response.value());
    }

    @Test
    void writeMultipleCoils() throws Exception {
        WriteMultipleCoilsResponse response =
                getClient().writeMultipleCoils(1, new WriteMultipleCoilsRequest(0, 8, new byte[] {(byte) 0xFF}));

        assertEquals(0, response.address());
        assertEquals(8, response.quantity());
    }

    @Test
    void writeMultipleRegisters() throws Exception {
        WriteMultipleRegistersResponse response =
                getClient().writeMultipleRegisters(1, new WriteMultipleRegistersRequest(0, 1, new byte[] {0x12, 0x34}));

        assertEquals(0, response.address());
        assertEquals(1, response.quantity());
    }

    @Test
    void maskWriteRegister() throws Exception {
        MaskWriteRegisterResponse response =
                getClient().maskWriteRegister(1, new MaskWriteRegisterRequest(0, 0xFF00, 0x00FF));

        assertEquals(0, response.address());
        assertEquals(0xFF00, response.andMask());
        assertEquals(0x00FF, response.orMask());
    }

    @Disabled
    @Test
    void readWriteMultipleRegisters() throws Exception {
        ReadWriteMultipleRegistersResponse response = getClient()
                .readWriteMultipleRegisters(
                        1, new ReadWriteMultipleRegistersRequest(0, 1, 0, 1, new byte[] {0x12, 0x34}));

        byte[] registers = response.registers();
        assertEquals(0x12, registers[0]);
        assertEquals(0x34, registers[1]);
    }

    @Test
    void isConnected() throws ModbusExecutionException {
        assertTrue(getClient().isConnected());

        getClient().disconnect();

        assertFalse(getClient().isConnected());
    }
}