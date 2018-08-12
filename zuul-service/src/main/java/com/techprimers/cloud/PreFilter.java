package com.techprimers.cloud;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class PreFilter extends ZuulFilter {

	private static final String ACCESS_TOKEN_HEADER_KEY = "Authorization";
	private static final Integer FILTER_ORDER = 1;
	private static final String FILTER_TYPE = "pre";
	private static final boolean SHLOUD_FILTER = true;

	@Autowired
	DiscoveryClient discoveryClient;

	@Override
	public String filterType() {
		return FILTER_TYPE;
	}

	@Override
	public int filterOrder() {
		return FILTER_ORDER;
	}

	@Override
	public boolean shouldFilter() {
		return SHLOUD_FILTER;
	}

	@Override
	public Object run() {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();

		RequestContext context = getCurrentContext();
		String token = context.getRequest().getHeader(ACCESS_TOKEN_HEADER_KEY);

		String url = discoveryClient.getInstances("auth-server").get(0).getUri().toString();

		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

	
		ResponseEntity<String> result = restTemplate.exchange(url + "/oauth/check_token/right", HttpMethod.GET, entity,String.class);
		return null;

	}
}
