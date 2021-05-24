package fr.amanin.examples.httprange.clients;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface RangeClientSpi {
    CompletableFuture<? extends RangeClient> create(final URI uri) throws IllegalArgumentException;
}
