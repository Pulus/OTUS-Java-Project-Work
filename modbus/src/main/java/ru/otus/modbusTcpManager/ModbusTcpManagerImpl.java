package ru.otus.modbusTcpManager;

import com.digitalpetri.modbus.client.ModbusTcpClient;
import com.digitalpetri.modbus.pdu.ReadHoldingRegistersRequest;
import com.digitalpetri.modbus.pdu.ReadHoldingRegistersResponse;
import com.digitalpetri.modbus.pdu.WriteMultipleRegistersRequest;
import com.digitalpetri.modbus.tcp.client.NettyTcpClientTransport;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.modbusTcpManager.converter.ModbusDataConverter;

public class ModbusTcpManagerImpl implements ModbusTcpManager {
    private static final Logger logger = LoggerFactory.getLogger(ModbusTcpManagerImpl.class);

    private ModbusTcpClient client;
    private ArrayList<Integer> integerArrayList = new ArrayList<>();

    @Override
    public void setup() {
        try {
            Param param = getParam();
            NettyTcpClientTransport transport = NettyTcpClientTransport.create(cfg -> {
                cfg.hostname = param.host();
                cfg.port = param.port();
            });

            client = ModbusTcpClient.create(transport);
            client.connect();
            logger.info("connect: {}:{} - ok", param.host(), param.port());
        } catch (Exception a) {
            logger.warn(a.toString());
        }
    }

    @Override
    public void stop() {
        try {
            if (client != null) {
                client.disconnect();
            }
            logger.info("connect - stop");
        } catch (Exception a) {
            logger.warn(a.toString());
        }
    }

    public void readHolding(int unitId, int address, int quantity) {
        // Чение
        try {
            ReadHoldingRegistersResponse response =
                    client.readHoldingRegisters(unitId, new ReadHoldingRegistersRequest(address, quantity));
            integerArrayList = ModbusDataConverter.byteToInteger(response.registers());
            logger.info("registers: {}", integerArrayList);

            logger.info("read - ok");
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    public void writeHolding(int unitId, int address, int quantity, ArrayList wrireArrayList) {
        // Запись
        try {
            byte[] bytes = ModbusDataConverter.integerToByte(wrireArrayList);

            client.writeMultipleRegisters(unitId, new WriteMultipleRegistersRequest(address, quantity, bytes));

            logger.info("write - ok");
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    private Param getParam() {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("config.properties");
            Properties props = new Properties();
            props.load(is);
            String host = props.getProperty("host");
            Integer port = Integer.valueOf(props.getProperty("port"));
            Param param = new Param(host, port);
            logger.info("The settings file has been read");
            return param;
        } catch (FileNotFoundException ex) {
            logger.warn("The file does not exist");
        } catch (IOException ex) {
            logger.warn("I/O error");
        }
        return null;
    }
}
