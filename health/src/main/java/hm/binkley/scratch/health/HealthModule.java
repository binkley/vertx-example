package hm.binkley.scratch.health;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import io.vertx.core.Verticle;
import org.kohsuke.MetaInfServices;

import javax.inject.Singleton;
import java.util.ServiceLoader;

/**
 * {@code HealthModule} wires {@link HealthVerticle}.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
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
        for (final Verticle v : verticles)
            if (v instanceof HealthVerticle)
                return (HealthVerticle) v;
        throw new IllegalStateException(
                "No HealthVerticle in META-INF/services");
    }

    @Override
    protected void configure() {
    }
}
