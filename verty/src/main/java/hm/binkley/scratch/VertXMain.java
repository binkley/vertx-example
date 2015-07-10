package hm.binkley.scratch;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import javax.inject.Inject;
import java.util.ServiceLoader;

import static com.google.inject.Guice.createInjector;

/**
 * {@code VertXMain} runs the demo verticle REST service.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
public final class VertXMain
        implements Runnable {
    private final Vertx vertx;
    private final Router router;
    private final ServiceLoader<Verticle> verticles;

    @Inject
    public VertXMain(final Vertx vertx, final Router router,
            final ServiceLoader<Verticle> verticles) {
        this.vertx = vertx;
        this.router = router;
        this.verticles = verticles;
    }

    @Override
    public void run() {
        verticles.forEach(vertx::deployVerticle);
        vertx.createHttpServer().
                requestHandler(router::accept).
                listen(8080);
    }

    public static void main(final String... args) {
        createInjector(new VertXModule()).
                getInstance(VertXMain.class).
                run();
    }
}
