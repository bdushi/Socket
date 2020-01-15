import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClientExample {
    public static void main(String[] args) {
        try {
            new SocketClientExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SocketClientExample() throws IOException, InterruptedException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8090);
        SocketChannel client = SocketChannel.open(hostAddress);

        System.out.println("Client... started");

        String threadName = Thread.currentThread().getName();

        // Send messages to server
        String [] messages = new String [] {threadName + ": test1",threadName + ": test2",threadName + ": test3"};

        for (int i = 0; i < messages.length; i++) {
            byte [] message = new String(messages [i]).getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(message);
            client.write(buffer);
            System.out.println(messages [i]);
            buffer.clear();
            Thread.sleep(5000);
        }
        client.close();
    }
}
