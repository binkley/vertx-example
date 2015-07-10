package hm.binkley.scratch.hello;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Verticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.kohsuke.MetaInfServices;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static io.vertx.core.http.HttpMethod.GET;
import static java.lang.String.format;

/**
 * {@code HelloVerticle} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@MetaInfServices(Verticle.class)
@Singleton
public final class HelloVerticle
        extends AbstractVerticle {
    /** Package scope for testing. */
    @Inject
    Router router;

    @Override
    public void start() {
        router.route(GET, "/hello/:name").
                handler(this::meet);
        router.route(GET, "/hello/:name").
                handler(this::greet);
    }

    /** Package scope for testing. */
    void meet(final RoutingContext context) {
        context.request().response().
                setChunked(true).
                putHeader(CONTENT_TYPE, "text/plain").
                write("Hello,\n"); // Newline needed to send chunk to client

        context.vertx().setTimer(1000, tid -> context.next());
    }

    /** Package scope for testing. */
    void greet(final RoutingContext context) {
        context.request().response().
                end(format("%s\n", context.request().getParam("name")));
    }
}
