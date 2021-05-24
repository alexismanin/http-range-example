package fr.amanin.examples.httprange.clients;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import static fr.amanin.examples.httprange.clients.ByteRange.range;
import static fr.amanin.examples.httprange.clients.ByteRange.singleByte;

/**
 * Simple test app that read default file included in embedded server. Note that we use CompletableFuture API for
 * simpler testing.
 */
public class Main {
    private static final String DEFAULT_URL= "http://localhost:7070/content/ascii.txt";

    public static void main(String[] args) throws ExecutionException, InterruptedException, URISyntaxException {
        // Choose the SPI you want to test
        final var spi = Spis.WEBFLUX;

        final var futureCli = spi.create(new URI(DEFAULT_URL));

        /* Note: To simplify example, we query raw response bytes, and convert it back to string ourself.
         * DO NOT do this in production. Use your client API to get a proper response representation (with proper
         * charset fetched from response headers, etc.)
         */

        // Reading a single chunk is easy, it returns the raw content as response body
        System.out.printf("%n%nSINGLE RANGE READING%n%n");
        final var chunk = futureCli.thenCompose(cli -> cli.read(range(3, 9))).get();
        System.out.println(new String(chunk, StandardCharsets.US_ASCII));

        /* Reading sparse chunks in a single query is more difficult, because a 'multipart/byteranges' is returned, and
         * it would force us to decode manually metadata embedded in response body. The included metadata allows to
         * properly identify individual chunks.
         */
        System.out.printf("%n%nMULTIPLE RANGE READING -> transfer-encoding: Chunked%n%n");
        var bytes = futureCli
                .thenCompose(
                        cli -> cli.read(
                                range(2, 4),
                                singleByte(9),
                                range(22, -1)
                        ))
                .get();
        System.out.println(new String(bytes, StandardCharsets.US_ASCII));
    }
}
