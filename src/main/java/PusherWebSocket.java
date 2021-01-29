
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.*;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PusherWebSocket implements ConnectionEventListener, ChannelEventListener, PrivateChannelEventListener {

    private final Pusher pusher;
    private final String channelName;
    private final String eventName;

    // Passanger
    private final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjMsImlzcyI6Imh0dHA6Ly9venppci5pc25hbGJhbmlhLmNvbS9hcGkvdjEvbG9naW4iLCJpYXQiOjE1ODAxOTc0MDEsImV4cCI6MTc5NjE5NzQwMSwibmJmIjoxNTgwMTk3NDAxLCJqdGkiOiJya2NOanc4YXd3anRLczRVIn0.P6GvktHWjxAFMo2RfLhBMJN5IwrV5gM9dCcpQLZZe6Y";
    // Driver
    // private final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIsImlzcyI6Imh0dHA6Ly9venppci5pc25hbGJhbmlhLmNvbS9hcGkvdjEvbG9naW4iLCJpYXQiOjE1ODAxOTcxOTUsImV4cCI6MTc5NjE5NzE5NSwibmJmIjoxNTgwMTk3MTk1LCJqdGkiOiJxSDFVWlowOHpScTF5Z01XIn0.MNgARRBAkzICYs0FMPQlblnC5WMp0Do5UHGKYlL0VKU";
    private final long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(PusherWebSocket.class);
        new PusherWebSocket(args);
    }

    public PusherWebSocket(final String[] args) {
        final String apiKey = args.length > 0 ? args[0] : "SDFSDF"; //"161717a55e65825bacf1";
        channelName = args.length > 1 ? args[1] : "my-channel";
        eventName = args.length > 2 ? args[2] : "my-event";
        final HttpAuthorizer authorization = new HttpAuthorizer(Constants.AUTH_URL);
        final Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + token);
        authorization.setHeaders(header);
        final PusherOptions options = new PusherOptions();
        options.setHost(Constants.HOST_NAME);
        options.setWsPort(Constants.HOST_PORT);
        options.setAuthorizer(authorization);
        options.setEncrypted(false);
        pusher = new Pusher(apiKey, options);
        // pusher.connect(this);
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting! " +
                        "\n code: " + code +
                        "\n message: " + message +
                        "\n Exception: " + e
                );
            }
        }, ConnectionState.ALL);
        pusher.subscribe("home", new ChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {
                System.out.println("onSubscriptionSucceeded: " + channelName);
            }

            @Override
            public void onEvent(PusherEvent event) {
                System.out.println("onEvent: " + event);
            }
        });

        // pusher.subscribePrivate("private-ride.59.offer", this, "Modules\\OzzirApi\\Events\\DriverInqueryEvent");
        // pusher.subscribePrivate("private-ride.59.decline", this, "Modules\\OzzirApi\\Events\\DriverDeclinedEvent");
        // pusher.subscribePrivate("private-ride.70.accept", this, "Modules\\OzzirApi\\Events\\PassangerAcceptedEvent");
        // pusher.subscribePrivate("private-driver.2.ride.70.accept", this, "Modules\\OzzirApi\\Events\\DriverAcceptedEvent");
        // pusher.subscribePrivate("private-driver.2.ride.70.accept", this, "Modules\\OzzirApi\\Events\\PassangerAcceptedEvent");
        System.out.println("NewRideEvent isSubscribed: " +
                pusher.subscribePrivate("private-driver.2.new-ride", this, "Modules\\OzzirApi\\Events\\NewRideEvent").isSubscribed());
        System.out.println("DriverInqueryEvent isSubscribed: " +
                pusher.subscribePrivate("private-ride.2.offer", this, "Modules\\OzzirApi\\Events\\DriverInqueryEvent").isSubscribed());
        System.out.println("DriverAcceptedEvent isSubscribed: " +
                pusher.subscribePrivate("private-ride.2.accept", this, "Modules\\OzzirApi\\Events\\DriverAcceptedEvent").isSubscribed());
        System.out.println("DriverDeclinedEvent isSubscribed: " +
                pusher.subscribePrivate("private-ride.2.decline", this, "Modules\\OzzirApi\\Events\\DriverDeclinedEvent").isSubscribed());

        /*pusher.subscribePrivate("private-driver.2.ride.2.accept", this, ".Modules\\OzzirApi\\Events\\DriverAcceptEvent");
        pusher.subscribePrivate("private-driver.2.ride.2.decline", this, ".Modules\\OzzirApi\\Events\\DriverDeclinedEvent");
        pusher.subscribePrivate("private-driver.2.ride.2.offer", this, ".Modules\\OzzirApi\\Events\\DriverAcceptEvent");*/

        // Keep main thread asleep while we watch for events or application will
        // terminate
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
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

    @Override
    public void onAuthenticationFailure(String message, Exception e) {
        String.format("[%s] Authentication failure due to [%s], exception was [%s]", timestamp(), message, e);
    }

    /* ChannelEventListener implementation */

    @Override
    public void onEvent(final PusherEvent event) {
        System.out.println(String.format("[%d] Received event [%s]", timestamp(), event.toString()));
        final Gson gson = new Gson();
        @SuppressWarnings("unchecked") final Map<String, String> jsonObject = gson.fromJson(event.getData(), Map.class);
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
