
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PusherWebSocket implements ConnectionEventListener, ChannelEventListener {

    private final Pusher pusher;
    private final String channelName;
    private final String eventName;
    private final long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(PusherWebSocket.class);
        new PusherWebSocket(args);
    }

    public PusherWebSocket(final String[] args) {
        final String apiKey = args.length > 0 ? args[0] : "3925904600fe97c1a1d8"; //"161717a55e65825bacf1";
        channelName = args.length > 1 ? args[1] : "ride.42.offer";
        eventName = args.length > 2 ? args[2] : "DriverAcceptedEvent";
        final HttpAuthorizer authorization = new HttpAuthorizer(Constants.HOST_NAME);
        final Map<String, String> header = new HashMap<>();
        header.put("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9venppci5pc25hbGJhbmlhLmNvbS9hcGkvdjEvbG9naW4iLCJpYXQiOjE1Nzg5Mzk1OTYsImV4cCI6MTc5NDkzOTU5NiwibmJmIjoxNTc4OTM5NTk2LCJqdGkiOiJtcmJiVG9IWGdyMWVCdkNzIn0.f2wAqvDINs3e_WuNDc4eRPpV2vSMwdtBdlx_DbVU_hg");
        authorization.setHeaders(header);
        final PusherOptions options = new PusherOptions().setEncrypted(true);
        options.setHost(Constants.HOST);
        options.setWsPort(6001);
        options.setAuthorizer(authorization);
        pusher = new Pusher(apiKey, options);
        pusher.connect(this);

        //pusher.subscribe(channelName, this, eventName);
        pusher.subscribePrivate("private-driver.2.ride.42.offer",
                new PrivateChannelEventListener() {
                    @Override
                    public void onEvent(PusherEvent event) {
                        String.format("onEvent onSubscriptionSucceeded [%s]", event.getEventName());
                    }

                    @Override
                    public void onSubscriptionSucceeded(String channelName) {
                        String.format("subscribePrivate onSubscriptionSucceeded [%s]", channelName);

                    }

                    @Override
                    public void onAuthenticationFailure(String message, Exception e) {
                        String.format("Authentication failure due to [%s], exception was [%s]", message, e);
                    }
                });

        // Keep main thread asleep while we watch for events or application will
        // terminate
        while (true) {
            try {
                Thread.sleep(1000);
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* ConnectionEventListener implementation */

    @Override
    public void onConnectionStateChange(final ConnectionStateChange change) {
        System.out.println(String.format("[%d] Connection state changed from [%s] to [%s]", timestamp(), change.getPreviousState(), change.getCurrentState()));
    }

    @Override
    public void onError(final String message, final String code, final Exception e) {
        System.out.println(String.format("[%d] An error was received with message [%s], code [%s], exception [%s]", timestamp(), message, code, e));
    }

    /* ChannelEventListener implementation */

    @Override
    public void onEvent(final PusherEvent event) {
        System.out.println(String.format("[%d] Received event [%s]", timestamp(), event.toString()));
        final Gson gson = new Gson();
        @SuppressWarnings("unchecked")
        final Map<String, String> jsonObject = gson.fromJson(event.getData(), Map.class);
        System.out.println(jsonObject);
    }

    @Override
    public void onSubscriptionSucceeded(final String channelName) {
        System.out.println(String.format("[%d] Subscription to channel [%s] succeeded", timestamp(), channelName));
    }

    private long timestamp() {
        return System.currentTimeMillis() - startTime;
    }
}
