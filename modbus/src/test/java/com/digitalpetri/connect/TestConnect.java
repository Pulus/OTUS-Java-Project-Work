package com.digitalpetri.connect;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.digitalpetri.modbus.ModbusPduSerializer.DefaultRequestSerializer;
import com.digitalpetri.modbus.client.ModbusClient;
import com.digitalpetri.modbus.client.ModbusTcpClient;
import com.digitalpetri.modbus.internal.util.Hex;
import com.digitalpetri.modbus.pdu.ReadHoldingRegistersRequest;
import com.digitalpetri.modbus.server.ProcessImage;
import com.digitalpetri.modbus.server.ReadWriteModbusServices;
import com.digitalpetri.modbus.tcp.Netty;
import com.digitalpetri.modbus.tcp.client.NettyTcpClientTransport;
import com.digitalpetri.modbus.tcp.client.NettyTcpClientTransport.ConnectionListener;
import com.digitalpetri.modbus.tcp.client.NettyTimeoutScheduler;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestConnect extends ClientServerIT {

    ModbusTcpClient client;

    NettyTcpClientTransport clientTransport;

    @BeforeEach
    void setup() throws Exception {
        var processImage = new ProcessImage();
        var modbusServices = new ReadWriteModbusServices() {
            @Override
            protected Optional<ProcessImage> getProcessImage(int unitId) {
                return Optional.of(processImage);
            }
        };

        clientTransport = NettyTcpClientTransport.create(cfg -> {
            cfg.hostname = "localhost";
            cfg.port = 502;
            cfg.connectPersistent = false;
        });

        client = ModbusTcpClient.create(
                clientTransport, cfg -> cfg.timeoutScheduler = new NettyTimeoutScheduler(Netty.sharedWheelTimer()));
        client.connect();
    }

    @AfterEach
    void teardown() throws Exception {
        if (client != null) {
            client.disconnect();
        }
    }

    @Override
    ModbusClient getClient() {
        return client;
    }

    @Test
    void sendRaw() throws Exception {
        var request = new ReadHoldingRegistersRequest(1, 1);
        ByteBuffer buffer = ByteBuffer.allocate(256);
        DefaultRequestSerializer.INSTANCE.encode(request, buffer);

        byte[] requestedPduBytes = new byte[buffer.position()];
        buffer.flip();
        buffer.get(requestedPduBytes);

        System.out.println("requestedPduBytes: " + Hex.format(requestedPduBytes));

        byte[] responsePduBytes = client.sendRaw(1, requestedPduBytes);

        System.out.println("responsePduBytes: " + Hex.format(responsePduBytes));
    }

    @Test
    void connectionListener() throws Exception {
        var onConnection = new CountDownLatch(1);
        var onConnectionLost = new CountDownLatch(1);

        clientTransport.addConnectionListener(new ConnectionListener() {
            @Override
            public void onConnection() {
                onConnection.countDown();
            }

            @Override
            public void onConnectionLost() {
                onConnectionLost.countDown();
            }
        });

        assertTrue(client.isConnected());

        client.disconnect();
        assertTrue(onConnectionLost.await(1, TimeUnit.SECONDS));

        client.connect();
        assertTrue(onConnection.await(1, TimeUnit.SECONDS));
    }
}
