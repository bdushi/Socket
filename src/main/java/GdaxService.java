import com.tinder.scarlet.WebSocket;
import com.tinder.scarlet.ws.Receive;
import com.tinder.scarlet.ws.Send;
import io.reactivex.Flowable;

public interface GdaxService {
    @Receive
    Flowable<WebSocket.Event> observeWebSocketEvent();
    @Send
    void sendSubscribe(Subscriber subscribe);
    @Receive
    Flowable<Ticker> observeTicker();
}
