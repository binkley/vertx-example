package hm.binkley.scratch;

import hm.binkley.scratch.health.HealthChecker.HealthCheck;
import hm.binkley.scratch.health.HealthStatus;
import hm.binkley.scratch.health.HealthVerticle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

import static hm.binkley.scratch.health.HealthStatus.ERROR;

/**
 * {@code ExtraHealth} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
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
