package hm.binkley.scratch.health;

import hm.binkley.scratch.health.HealthChecker.HealthCheck;
import lombok.Data;

import java.util.function.Supplier;

/**
 * {@code HealthChecker} checks for component health for {@link
 * HealthVerticle}.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
public interface HealthChecker
        extends Supplier<HealthCheck> {
    @Data
    final class HealthCheck {
        private final String display;
        private final HealthStatus status;
        private final Object resource;
        private final String message;
    }
}
