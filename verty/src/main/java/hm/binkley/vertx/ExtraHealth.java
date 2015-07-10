package hm.binkley.vertx;

import hm.binkley.vertx.health.HealthChecker.HealthCheck;
import hm.binkley.vertx.health.HealthStatus;
import hm.binkley.vertx.health.HealthVerticle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

import static hm.binkley.vertx.health.HealthStatus.ERROR;

/**
 * {@code ExtraHealth} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Needs documentation
 */
@Singleton
public final class ExtraHealth {
    private static final Random random = new Random();

    @Inject
    public ExtraHealth(final HealthVerticle base) {
        base.add(() -> {
            final HealthStatus state = randomHealthStatus();
            return new HealthCheck("bob", state, "https://bob/yer/unkel",
                    ERROR == state ? "Threw something or other" : null);
        });
        base.add(() -> {
            final HealthStatus state = randomHealthStatus();
            return new HealthCheck("fred", state, "https://fred/and/wilma",
                    ERROR == state ? "Threw something or other" : null);
        });
    }

    /** Privileges first value. */
    private static HealthStatus randomHealthStatus() {
        final int n = random.nextInt(HealthStatus.values().length + 1);
        switch (n) {
        case 0:
            return HealthStatus.values()[0];
        default:
            return HealthStatus.values()[n - 1];
        }
    }
}
