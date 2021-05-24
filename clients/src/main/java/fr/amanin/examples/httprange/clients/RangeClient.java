package fr.amanin.examples.httprange.clients;

import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class RangeClient {

    static final String RANGE_HEADER = "Range";
    protected static final String TRANSFER_ENCODING = "transfer-encoding";

    protected final URI uri;
    protected final RangeUnit unit;

    public RangeClient(URI uri, RangeUnit unit) {
        this.uri = uri;
        this.unit = unit;
    }

    abstract CompletableFuture<byte[]> read(final ByteRange... chunk);

    String buildRangeValue(ByteRange... parts) {
        return Arrays.stream(parts)
                // TODO: configure unit type
                .map(range -> range.apply(unit))
                .collect(Collectors.joining(",", unit.name() + "=", ""));
    }
}
