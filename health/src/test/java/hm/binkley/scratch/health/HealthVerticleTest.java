package hm.binkley.scratch.health;

import io.vertx.ext.web.Router;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * {@code HealthVerticleTest} tests {@link HealthVerticle}.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@RunWith(MockitoJUnitRunner.class)
public class HealthVerticleTest {
    @Test
    public void should() {
    }

    @Mock
    private Router router;

    private HealthVerticle health;

    @Before
    public void setUp() {
        health = new HealthVerticle();
        health.router = router;
    }
}
