package fr.amanin.examples.httprange.clients;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;

public class StandardJava8 extends RangeClient {
    public StandardJava8(URI uri, RangeUnit unit) {
        super(uri, unit);
    }

    @Override
    CompletableFuture<byte[]> read(ByteRange... parts) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return readFully(parts);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    private byte[] readFully(final ByteRange... chunks) throws IOException {
        final var conn = uri.toURL().openConnection();
        conn.setRequestProperty(RANGE_HEADER, buildRangeValue(chunks));
        try (var is = conn.getInputStream()) {
            return is.readAllBytes();
        }
    }
}
