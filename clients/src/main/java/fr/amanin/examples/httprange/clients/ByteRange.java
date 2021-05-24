package fr.amanin.examples.httprange.clients;

import java.util.function.Function;

public class ByteRange implements Function<RangeUnit, String> {

    private final long inclusiveStart;
    private final long inclusiveEnd;
    private final boolean untilEndOfStream;

    private ByteRange(long position) {
        this(position, position);
    }

    private ByteRange(long inclusiveStart, long inclusiveEnd) {
        untilEndOfStream = inclusiveEnd < 0 || inclusiveEnd >= Long.MAX_VALUE;
        if (inclusiveStart < 0) {
            throw new IllegalArgumentException("Start byte must be positive");
        } else if (!untilEndOfStream && inclusiveEnd < inclusiveStart) {
            throw new IllegalArgumentException("Malformed HTTP range: end byte cannot be greater than start byte.");
        }
        this.inclusiveStart = inclusiveStart;
        this.inclusiveEnd = inclusiveEnd;
    }

    @Override
    public String apply(RangeUnit unit) {
        return "%s-%s".formatted(Long.toString(inclusiveStart), untilEndOfStream ? "" : inclusiveEnd);
    }

    public static ByteRange singleByte(long position) { return new ByteRange(position); }
    public static ByteRange range(long inclusiveStartByte, long inclusiveEndByte) {
        return new ByteRange(inclusiveStartByte, inclusiveEndByte);
    }
}
