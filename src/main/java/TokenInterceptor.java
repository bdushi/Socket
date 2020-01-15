import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class TokenInterceptor implements Interceptor {
    private final String token;
    public TokenInterceptor(String token) {
        this.token = token;
    }

    @NotNull
    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if(token != null) {
            builder.addHeader("Authorization", token);
        }
        return chain.proceed(builder.build());
    }
}
