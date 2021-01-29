import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class IoSocket {
    public static void main(String[] args) {
        System.out.println("Start");
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor(String.format("%s %s", "Bearer", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIsImlzcyI6Imh0dHA6Ly9venppci5pc25hbGJhbmlhLmNvbS9hcGkvdjEvbG9naW4iLCJpYXQiOjE1Nzk2MTE1NjAsImV4cCI6MTc5NTYxMTU2MCwibmJmIjoxNTc5NjExNTYwLCJqdGkiOiJDY0VJcnNXS1R2ZVhZdWdOIn0.M9di3r16ljHGTH_iRo2e8NX-0THr5pXoJqe-n3exyUI")))
                .addInterceptor(logging)
                //.hostnameVerifier(myHostnameVerifier)
                //.sslSocketFactory(mySSLContext.getSocketFactory(), myX509TrustManager)
                .build();
        // default settings for all sockets
        IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
        IO.setDefaultOkHttpCallFactory(okHttpClient);
        // set as an option
        IO.Options opts = new IO.Options();
        // opts.query = "Authorization=" + String.format("%s %s", "Bearer", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIsImlzcyI6Imh0dHA6Ly9venppci5pc25hbGJhbmlhLmNvbS9hcGkvdjEvbG9naW4iLCJpYXQiOjE1Nzk2MTE1NjAsImV4cCI6MTc5NTYxMTU2MCwibmJmIjoxNTc5NjExNTYwLCJqdGkiOiJDY0VJcnNXS1R2ZVhZdWdOIn0.M9di3r16ljHGTH_iRo2e8NX-0THr5pXoJqe-n3exyUI");
        opts.port = 6001;
        opts.hostname = Constants.HOST_NAME;
        opts.host = Constants.HOST_URL;
        opts.secure = false;
        // opts.callFactory = okHttpClient;
        // opts.webSocketFactory = okHttpClient;
        try {
            Socket socket = IO.socket(Constants.HOST_URL, opts);
            socket.on("trump", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject) args[0];
                    System.out.println("Trump length: " + args.length);
                    System.out.println("Trump: " + data);
                }

            }).on("new message", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject) args[0];
                    System.out.println("New message length: " + args.length);
                    System.out.println("New message: " + data);
                }

            }).on("private-driver.2.ride.42.offer", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject) args[0];
                    System.out.println("private-driver.2.ride.42.offer length: " + args.length);
                    System.out.println("private-driver.2.ride.42.offer: " + data);
                }

            }).on("driver.2.new-ride", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject) args[0];
                    System.out.println("driver.2.new-ride: " + args.length);
                    System.out.println("driver.2.new-ride: " + data);
                }

            }).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject) args[0];
                    System.out.println("EVENT_MESSAGE: " + args.length);
                    System.out.println("EVENT_MESSAGE: " + data);
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("EVENT_DISCONNECT: " + args);
                }

            }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Exception err = (Exception)args[0];
                    System.out.println("EVENT_ERROR: " + err);
                }
            }).on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("EVENT_CONNECT: " + args);
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("EVENT_DISCONNECT: " + args);
                }
            }).on(Socket.EVENT_PING, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("EVENT_PING: " + args);
                }
            });
            socket.connect();
            System.out.println("Connected: " + socket.connected());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
