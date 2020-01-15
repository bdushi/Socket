import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main (String[] args) throws IOException {
        try {
            // here I set my java keystore, note that these settings are GLOBAL
            System.setProperty("javax.net.ssl.keyStore", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9venppci5pc25hbGJhbmlhLmNvbS9hcGkvdjEvbG9naW4iLCJpYXQiOjE1Nzg5Mzk1OTYsImV4cCI6MTc5NDkzOTU5NiwibmJmIjoxNTc4OTM5NTk2LCJqdGkiOiJtcmJiVG9IWGdyMWVCdkNzIn0.f2wAqvDINs3e_WuNDc4eRPpV2vSMwdtBdlx_DbVU_hg");
            System.setProperty("javax.net.ssl.keyStorePassword", "12345678");
            // The trust store represents the certificates I trust, If I used a certificate signed by a certificate authority(CA),
            // then I would use the default java trust store. The default trust store supports most CAs. However, in this intsance, I
            // am using a self signed localhost certificate, so I will supply my own trust store, identical to my keystore.
            //System.setProperty("javax.net.ssl.trustStore", "testKeyStore.keystore");
            System.setProperty("javax.net.ssl.trustStore", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9venppci5pc25hbGJhbmlhLmNvbS9hcGkvdjEvbG9naW4iLCJpYXQiOjE1Nzg5Mzk1OTYsImV4cCI6MTc5NDkzOTU5NiwibmJmIjoxNTc4OTM5NTk2LCJqdGkiOiJtcmJiVG9IWGdyMWVCdkNzIn0.f2wAqvDINs3e_WuNDc4eRPpV2vSMwdtBdlx_DbVU_hg");
            System.setProperty("javax.net.ssl.trustStorePassword", "12345678");
            SocketFactory factory =  SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket("ozzir.isnalbania.com", 6001);
            // this should be true by default
            socket.setUseClientMode(true);
            // enable all cipher suites
            String[] supported = socket.getSupportedCipherSuites();
            socket.setEnabledCipherSuites(supported);
            try (OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)) {
                writer.write("Hello World");
                writer.flush();
            }
        }
        catch (IOException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }
}
