package fr.amanin.examples.httprange.clients;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.reactive.function.client.WebClient;

public class Webflux extends RangeClient {

    final WebClient cli;

    public Webflux(URI uri, RangeUnit unit, WebClient cli) {
        super(uri, unit);
        this.cli = cli;
    }

    @Override
    CompletableFuture<byte[]> read(ByteRange... chunks) {
        return cli.get().header(RANGE_HEADER, buildRangeValue(chunks))
            .retrieve()
            .bodyToMono(byte[].class)
            .toFuture();
    }
}
