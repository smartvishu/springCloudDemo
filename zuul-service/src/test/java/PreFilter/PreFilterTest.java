package PreFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.netflix.util.Pair;
import com.netflix.zuul.context.RequestContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.pre.PreDecorationFilter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORWARD_TO_KEY;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.REQUEST_URI_KEY;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

/**
 * @author Dave Syer
 */
public class PreFilterTest {

	private PreDecorationFilter filter;

	@Mock
	private DiscoveryClient discovery;

	private ZuulProperties properties = new ZuulProperties();

	private DiscoveryClientRouteLocator routeLocator;

	private MockHttpServletRequest request = new MockHttpServletRequest();

	private ProxyRequestHelper proxyRequestHelper = new ProxyRequestHelper();

	@Before
	public void init() {
		initMocks(this);
		this.properties = new ZuulProperties();
		this.routeLocator = new DiscoveryClientRouteLocator("/", this.discovery, this.properties);
		this.filter = new PreDecorationFilter(this.routeLocator, "/", this.properties, this.proxyRequestHelper);
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.clear();
		ctx.setRequest(this.request);
	}

	@After
	public void clear() {
		RequestContext.getCurrentContext().clear();
	}

	@Test
	public void basicProperties() throws Exception {
		assertEquals(5, this.filter.filterOrder());
		assertEquals(true, this.filter.shouldFilter());
		assertEquals(PRE_TYPE, this.filter.filterType());
	}

	@Test
	public void skippedIfServiceIdSet() throws Exception {
		RequestContext.getCurrentContext().set(SERVICE_ID_KEY, "myservice");
		assertEquals(false, this.filter.shouldFilter());
	}

	@Test
	public void skippedIfForwardToSet() throws Exception {
		RequestContext.getCurrentContext().set(FORWARD_TO_KEY, "myconteext");
		assertEquals(false, this.filter.shouldFilter());
	}

	@Test
	public void xForwardedHostHasPort() throws Exception {
		this.properties.setPrefix("/api");
		this.request.setRequestURI("/api/foo/1");
		this.request.setRemoteAddr("5.6.7.8");
		this.request.setServerPort(8080);
		this.routeLocator.addRoute(new ZuulRoute("foo", "/foo/**", "foo", null, false, null, null));
		this.filter.run();
		RequestContext ctx = RequestContext.getCurrentContext();
		assertEquals("localhost:8080", ctx.getZuulRequestHeaders().get("x-forwarded-host"));
	}

}
