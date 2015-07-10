package hm.binkley.scratch.hello;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static io.vertx.core.http.HttpMethod.GET;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code HelloVerticleTest} tests {@link HelloVerticle}.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Needs documentation
 */
@RunWith(MockitoJUnitRunner.class)
public class HelloVerticleTest {
    @Mock
    private Router router;
    @Mock
    private Route route;

    private HelloVerticle hello;

    @Before
    public void setUp() {
        hello = new HelloVerticle();
        hello.router = router;
    }

    /** @todo Not a good test - tests how rather than what */
    @Test
    public void shouldRoutePath() {
        final ArgumentCaptor<HttpMethod> method = forClass(HttpMethod.class);
        final ArgumentCaptor<String> path = forClass(String.class);
        when(router.route(method.capture(), path.capture())).
                thenReturn(route);

        hello.start();

        assertThat(method.getAllValues(), is(equalTo(asList(GET, GET))));
        assertThat(path.getAllValues(),
                is(equalTo(asList("/hello/:name", "/hello/:name"))));

        verify(route, times(2)).handler(any());
    }
}
