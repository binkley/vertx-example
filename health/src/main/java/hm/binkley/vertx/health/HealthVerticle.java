package hm.binkley.vertx.health;

import hm.binkley.vertx.health.HealthChecker.HealthCheck;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Verticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.kohsuke.MetaInfServices;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static hm.binkley.vertx.health.HealthStatus.ALIVE;
import static io.vertx.core.http.HttpHeaders.CACHE_CONTROL;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static io.vertx.core.http.HttpMethod.GET;
import static java.lang.String.format;
import static java.net.InetAddress.getLocalHost;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * {@code HealthVerticle} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Duplication in text/json health methods; avoid overdoing it
 */
@MetaInfServices(Verticle.class)
@Singleton
public final class HealthVerticle
        extends AbstractVerticle {
    /** Package scope for testing. */
    @Inject
    Router router;

    private final List<HealthChecker> checks = new CopyOnWriteArrayList<>();

    public void add(final HealthChecker check) {
        checks.add(check);
    }

    @Override
    public void start()
            throws Exception {
        router.route(GET, "/health").
                produces("text/plain").
                handler(this::healthBrowser);
        router.route(GET, "/health").
                produces("application/json").
                handler(this::healthRest);
    }

    private void healthBrowser(final RoutingContext context) {
        final Map<String, Object> headers = headers(context);

        final HealthStatus[] alive = {ALIVE};
        final String body = checks.stream().
                map(HealthChecker::get).
                peek(c -> alive[0] = alive[0].with(c.getStatus())).
                map(c -> format("%c %s %s - %s - %s", c.getStatus().mark(),
                        c.getStatus(), c.getDisplay(), c.getResource(),
                        c.getMessage())).
                collect(joining("\n"));
        headers.put("status", alive[0]);

        // Build headers AFTER body so dep health checks can change status
        final String header = headers.entrySet().stream().
                map(e -> format("%s: %s", e.getKey().toUpperCase(),
                        e.getValue())).
                collect(joining("\n"));

        context.request().response().
                putHeader(CONTENT_TYPE, context.getAcceptableContentType()).
                putHeader(CACHE_CONTROL, "no-cache").
                end(format("%s\n\n%s\n", header, body));
    }

    private void healthRest(final RoutingContext context) {
        final HealthStatus[] alive = {ALIVE};
        final Map<String, Object> body = checks.stream().
                map(HealthChecker::get).
                peek(c -> alive[0] = alive[0].with(c.getStatus())).
                collect(toMap(HealthCheck::getDisplay,
                        c -> mapOf("name", c.getDisplay(), "status",
                                c.getStatus(), "resource", c.getResource(),
                                "message", c.getMessage()), throwingMerger(),
                        () -> headers(context)));
        body.put("status", alive[0]);

        context.request().response().
                putHeader(CONTENT_TYPE, context.getAcceptableContentType()).
                putHeader(CACHE_CONTROL, "no-cache").
                end(new JsonObject(body).
                        encode());
    }

    private static Map<String, Object> headers(final RoutingContext context) {
        return mapOf("name", "???", "status", ALIVE, "resource",
                context.request().absoluteURI(), "hostname", hostname());
    }

    /** @todo Utility code - drops null-valued keys */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    private static <R, K extends R, V extends R> Map<K, V> mapOf(
            final R... pairs) {
        final List<R> x = asList(pairs);
        final Iterator<R> it = x.iterator();
        final Map<K, V> map = new LinkedHashMap<>();
        while (it.hasNext()) {
            final K key = (K) it.next();
            if (!it.hasNext())
                throw new IllegalArgumentException("Key with no value");
            final V value = (V) it.next();
            if (null != value)
                map.put(key, value);
        }
        return map;
    }

    /** Stolen from {@link Collectors#throwingMerger}. */
    private static <T> BinaryOperator<T> throwingMerger() {
        return (u, v) -> {
            throw new IllegalStateException(format("Duplicate key %s", u));
        };
    }

    private static String hostname() {
        try {
            return getLocalHost().getHostName();
        } catch (final UnknownHostException e) {
            throw new AssertionError(e);
        }
    }
}
