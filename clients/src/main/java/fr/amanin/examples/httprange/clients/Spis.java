package fr.amanin.examples.httprange.clients;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import static java.net.http.HttpRequest.BodyPublishers.noBody;
import static java.net.http.HttpResponse.BodyHandlers.*;

public class Spis {

    private static final String ACCEPT_RANGES = "Accept-Ranges";

    public static final RangeClientSpi JAVA_11_CLIENT = uri -> {
        HttpClient cli = HttpClient.newBuilder().build();
        final var request = HttpRequest.newBuilder(uri).method("HEAD", noBody()).build();
        return cli.sendAsync(request, discarding())
           .thenApply(response -> {
               RangeUnit unit = response.headers().firstValue(ACCEPT_RANGES)
                       .map(RangeUnit::parse)
                       .orElseThrow(() -> new IllegalArgumentException("HTTP range is not supported by given service; or defined range unit is unknown to us"));

               return new StandardJava11(cli, uri, unit);
           });
    };

    public static final RangeClientSpi JAVA_8_CLIENT = uri -> CompletableFuture.supplyAsync(() -> {
            try {
                final var conn = uri.toURL().openConnection();
                if (conn instanceof HttpURLConnection) {
                    ((HttpURLConnection) conn).setRequestMethod("HEAD");
                    conn.connect();
                    final var unit = RangeUnit.parse(conn.getHeaderField(ACCEPT_RANGES));
                    if (unit != RangeUnit.none && unit != RangeUnit.unsupported) {
                        return new StandardJava8(uri, unit);
                    }
                }
                throw new IllegalArgumentException("HTTP range is not supported, or we do not support defined unit");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

    public static final RangeClientSpi WEBFLUX = uri -> {
        final var wc = WebClient.builder().baseUrl(uri.toString()).build();
        return wc.head()
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getHeaders().getFirst(HttpHeaders.ACCEPT_RANGES))
                .map(acceptRange -> RangeUnit.parse(acceptRange))
                .filter(acceptRange -> acceptRange != RangeUnit.unsupported && acceptRange != RangeUnit.none)
                .map(rangeValue -> new Webflux(uri, rangeValue, wc))
                .toFuture();
    };
}
