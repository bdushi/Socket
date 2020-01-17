import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Map;

public class IoSocket {
    public static void main(String[] args) {
        System.out.println("Start");
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor(String.format("%s %s", "Bearer", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9venppci5pc25hbGJhbmlhLmNvbS9hcGkvdjEvbG9naW4iLCJpYXQiOjE1Nzg5Mzk1OTYsImV4cCI6MTc5NDkzOTU5NiwibmJmIjoxNTc4OTM5NTk2LCJqdGkiOiJtcmJiVG9IWGdyMWVCdkNzIn0.f2wAqvDINs3e_WuNDc4eRPpV2vSMwdtBdlx_DbVU_hg")))
                .addInterceptor(logging)
                //.hostnameVerifier(myHostnameVerifier)
                //.sslSocketFactory(mySSLContext.getSocketFactory(), myX509TrustManager)
                .build();
        // default settings for all sockets
        // IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
        // IO.setDefaultOkHttpCallFactory(okHttpClient);
        // set as an option
        IO.Options opts = new IO.Options();
        opts.query = "Authorization=" + String.format("%s %s", "Bearer", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9venppci5pc25hbGJhbmlhLmNvbS9hcGkvdjEvbG9naW4iLCJpYXQiOjE1Nzg5Mzk1OTYsImV4cCI6MTc5NDkzOTU5NiwibmJmIjoxNTc4OTM5NTk2LCJqdGkiOiJtcmJiVG9IWGdyMWVCdkNzIn0.f2wAqvDINs3e_WuNDc4eRPpV2vSMwdtBdlx_DbVU_hg");
        // opts.port = 6001;
        // opts.callFactory = okHttpClient;
        // opts.webSocketFactory = okHttpClient;
        try {
            Socket socket = IO.socket(Constants.HOST_NAME, opts);

            socket.on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Transport transport = (Transport)args[0];
                    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            @SuppressWarnings("unchecked")
                            Map<String, String> headers = (Map<String, String>) args[0];
                            // set header
                            headers.put("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9venppci5pc25hbGJhbmlhLmNvbS9hcGkvdjEvbG9naW4iLCJpYXQiOjE1Nzg5Mzk1OTYsImV4cCI6MTc5NDkzOTU5NiwibmJmIjoxNTc4OTM5NTk2LCJqdGkiOiJtcmJiVG9IWGdyMWVCdkNzIn0.f2wAqvDINs3e_WuNDc4eRPpV2vSMwdtBdlx_DbVU_hg");
                        }
                    });
                }
                // trump
            }).on("trump", new Emitter.Listener() {
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

            }).on("ride.42.offer", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject) args[0];
                    System.out.println("ride.42.offer length: " + args.length);
                    System.out.println("ride.42.offer: " + data);
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
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
