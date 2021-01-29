import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

public class OkHttpSocket {

    public static void main(String[] args) {
        new OkHttpSocket();
    }

    private OkHttpSocket() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient clientCoinPrice =
                new OkHttpClient
                        .Builder()
                        .addInterceptor(new TokenInterceptor(String.format("%s %s", "Bearer", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9venppci5pc25hbGJhbmlhLmNvbS9hcGkvdjEvbG9naW4iLCJpYXQiOjE1Nzg5Mzk1OTYsImV4cCI6MTc5NDkzOTU5NiwibmJmIjoxNTc4OTM5NTk2LCJqdGkiOiJtcmJiVG9IWGdyMWVCdkNzIn0.f2wAqvDINs3e_WuNDc4eRPpV2vSMwdtBdlx_DbVU_hg")))
                        .addInterceptor(logging)
                        .build();
        Request requestCoinPrice = new Request.Builder().url(Constants.CHAT_SERVER_URL).build();
        clientCoinPrice.newWebSocket(requestCoinPrice, webSocketListenerCoinPrice);
        clientCoinPrice.dispatcher().executorService().shutdown();
    }

    private WebSocketListener webSocketListenerCoinPrice = new WebSocketListener() {
        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            //super.onOpen(webSocket, response);
            System.out.println("onOpen: " + response.message());
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
            System.out.println("onMessage: " + bytes);
        }

        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
            System.out.println("onMessage: " + code + " " + reason);
        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosing(webSocket, code, reason);
            // webSocket.close(1000, "Finished");
            // webSocket.cancel();
            System.out.println("onMessage: " + code + " " + reason);
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            System.out.println("onMessage: " + t.getMessage() + " " + response.message());
        }
    };

}
