package hm.binkley.vertx;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import hm.binkley.vertx.health.HealthVerticle;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import java.util.ServiceLoader;

import static io.vertx.core.Vertx.vertx;
import static io.vertx.ext.web.Router.router;
import static java.util.ServiceLoader.load;

/**
 * {@code VertXModule} wires {@link VertXMain}.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Better pattern for extending {@link HealthVerticle}.
 */
public class VertXModule
        extends AbstractModule {
    @Override
    protected void configure() {
        // Composes extending modules from the classpath
        final ServiceLoader<Module> modules = load(Module.class);
        modules.forEach(this::requestInjection);
        modules.forEach(this::install);

        // Alternative to static singleton provider methods
        final Vertx vertx = vertx();
        bind(Vertx.class).toInstance(vertx);
        bind(Router.class).toInstance(router(vertx));

        // Verticles need injection - service loader requires no-arg ctor
        final ServiceLoader<Verticle> verticles = load(Verticle.class);
        verticles.forEach(this::requestInjection);
        bind(new TypeLiteral<ServiceLoader<Verticle>>() {
        }).toInstance(verticles);

        // TODO: A more automated way for health extensions
        bind(ExtraHealth.class).asEagerSingleton();
    }
}
