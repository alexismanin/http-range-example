/**
 * Shows how to perform HTTP range requests (reading part of a file through HTTP) with various clients:
 * <ul>
 *     <li>{@link fr.amanin.examples.httprange.clients.StandardJava8 Java 8 standard library}: using URL connection.</li>
 *     <li>{@link fr.amanin.examples.httprange.clients.StandardJava11 Java 16 standard library}: using Http client API.</li>
 *     <li>{@link fr.amanin.examples.httprange.clients.Webflux} because it's a very powerful tool ;-)</li>
 * </ul>
 *
 * HTTP-Range allows to download only part of a server resource. You should check if target resource support it using
 * <em>HEAD</em> request. To get an example, see {@link fr.amanin.examples.httprange.clients.Spis}, more specifically
 * {@link fr.amanin.examples.httprange.clients.RangeClientSpi#create(java.net.URI) client creation methods}. they eagerly
 * check HTTP range support.
 *
 * For details about HTTP range requests, see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Range_requests">Mozilla developper guide</a>.
 *
 * @see fr.amanin.examples.httprange.clients.Main Main testing app
 */
package fr.amanin.examples.httprange.clients;