import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

public class DebugPusherWebSocket {
    public static void main(String[] args) {
        new DebugPusherWebSocket();
    }
    public DebugPusherWebSocket() {
        PusherOptions options = new PusherOptions();
        // options.setCluster("rsocket-demo.herokuapp.com/ws");
        options.setHost("rsocket-demo.herokuapp.com/ws");
        //options.setHost("socket-io-chat.now.sh");
        options.setWssPort(80);
        Pusher pusher = new Pusher("3925904600fe97c1a1d8", options);
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
        pusher.subscribe("new message", new ChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {
                System.out.println("onSubscriptionSucceeded: " + channelName);
            }

            @Override
            public void onEvent(PusherEvent event) {
                System.out.println("onEvent: " + event);
            }
        });

        pusher.subscribe("trump", new ChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {
                System.out.println("onSubscriptionSucceeded: " + channelName);
            }

            @Override
            public void onEvent(PusherEvent event) {
                System.out.println("onEvent: " + event);
            }
        });
//        Channel channel = pusher.subscribe("my-channel");
//
//        channel.bind("my-event", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(PusherEvent event) {
//                System.out.println("Received event with data: " + event.toString());
//            }
//        });

        // Keep application from terminating while we watch for events
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
