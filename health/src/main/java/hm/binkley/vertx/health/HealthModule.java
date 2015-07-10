package hm.binkley.vertx.health;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import io.vertx.core.Verticle;
import org.kohsuke.MetaInfServices;

import javax.inject.Singleton;
import java.util.ServiceLoader;

import static java.util.stream.StreamSupport.stream;

/**
 * {@code HealthModule} wires {@link HealthVerticle}.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Better pattern for extending {@link HealthVerticle}.
 */
@MetaInfServices(Module.class)
public class HealthModule
        extends AbstractModule {
    /**
     * Carefully provides <strong>same</strong> instance of {@code
     * HealthVerticle} created by {@link ServiceLoader}.  Else uses of {@link
     * HealthVerticle#add(HealthChecker)} would update the wrong instance.
     *
     * @param verticles the verticles to search
     *
     * @return the health vertile, if found
     *
     * @throws IllegalStateException if no health verticle found
     */
    @Provides
    @Singleton
    public static HealthVerticle provideHealthVerticle(
            final ServiceLoader<Verticle> verticles) {
        return stream(verticles.spliterator(), true).
                filter(v -> v instanceof HealthVerticle).
                findFirst().
                map(HealthVerticle.class::cast).
                orElseThrow(() -> new IllegalStateException(
                        "No HealthVerticle in META-INF/services"));
    }

    @Override
    protected void configure() {
    }
}
