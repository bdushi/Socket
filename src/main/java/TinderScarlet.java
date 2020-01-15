import com.tinder.scarlet.Scarlet;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class TinderScarlet {

    public static void main(String[] args) {

    }

    private TinderScarlet() {
        Scarlet scarlet = new Scarlet.Builder()
                //.webSocketFactory(okHttpClient.newWebSocketFactory("wss://ws-feed.gdax.com"))
                //.addMessageAdapterFactory(MoshiMessageAdapter.Factory())
                //.addStreamAdapterFactory(RxJava2StreamAdapterFactory())
                .build();
    }

    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();
}
