import java.util.ArrayList;
import ru.otus.modbusTcpManager.ModbusTcpManagerImpl;

public class Mast {
    public static void main(String[] args) {
        ModbusTcpManagerImpl tcpManager = new ModbusTcpManagerImpl();
        tcpManager.setup();
        tcpManager.readHolding(1, 0, 5);
        tcpManager.stop();

        tcpManager.setup();
        var wrireArrayList = new ArrayList<Integer>();
        wrireArrayList.add(1111);
        wrireArrayList.add(2222);
        wrireArrayList.add(3333);
        tcpManager.writeHolding(1, 0, wrireArrayList.size(), wrireArrayList);
        tcpManager.stop();
    }
}
