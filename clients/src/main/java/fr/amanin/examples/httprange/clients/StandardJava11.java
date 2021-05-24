package fr.amanin.examples.httprange.clients;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

class StandardJava11 extends RangeClient {
    final HttpClient cli;

    public StandardJava11(HttpClient cli, URI uri, RangeUnit unit) {
        super(uri, unit);
        this.cli = cli;
    }

    @Override
    public CompletableFuture<byte[]> read(ByteRange... chunks) {
        return readRanges(HttpResponse.BodyHandlers.ofByteArray(), chunks);
    }

    private <T> CompletableFuture<T> readRanges(HttpResponse.BodyHandler<T> bodyHandler, ByteRange... ranges) {
        final var request = HttpRequest.newBuilder(uri)
                .header(RANGE_HEADER, buildRangeValue(ranges))
                .GET()
                .build();
        return cli.sendAsync(request, bodyHandler)
                .thenApply(response -> {
                    if (response.statusCode() == 206) return response.body();
                    else throw new RuntimeException("request failed. Status code: "+response.statusCode());
                });
    }
}
