import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class JavaSocket extends Thread {

    public static void main(String[] args) {
        new JavaSocket();
    }

    private JavaSocket() {
        this.start();
    }

    @Override
    public void run() {
        System.out.println("Start");
        try {
            //91.205.172.97
            Socket socket = new Socket("ozzir.isnalbania.com", 6001);
            System.out.println("IsConnected: " + socket.isConnected());
            System.out.println("RemoteSocketAddress: " + socket.getRemoteSocketAddress());
            System.out.println("Channel: " + socket.getChannel());
            // , StandardCharsets.UTF_8
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(reader.readLine());
            while(reader.readLine() != null) {
                System.out.println("Reader: " + reader.readLine());
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }
}
