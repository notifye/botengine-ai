/**
 * 
 */
package io.notifye.botengine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import io.notifye.botengine.interceptor.LoggingRequestInterceptor;
import io.notifye.botengine.interceptor.RewritePropertiesRequestInterceptor;
import io.notifye.botengine.model.parsers.ParserUtil;
import io.notifye.botengine.security.Token;

/**
 * @author santosadriano
 *
 */
public class HttpClient {
	
	private static RestTemplate client;
	
	public static ResponseEntity<String> put(String url, HttpEntity<?> entity) {
		return getClient().exchange(url, HttpMethod.PUT, entity, String.class);
	}
	
	public static ResponseEntity<String> post(String url, HttpEntity<?> entity) {
		return getClient().exchange(url, HttpMethod.POST, entity, String.class);
	}
	
	public static boolean isSuccessful(ResponseEntity<String> response) {
		return response.getStatusCode().is2xxSuccessful();
	}
	
	public static HttpHeaders getDevHeaders(Token token){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "text/plain");
		headers.add("authorization", String.format("Bearer %s", token.getToken()));
		return headers;
	}
	
	public static RestTemplate getClient(){
		if(Objects.isNull(client)){
			client = new RestTemplate();
	
			client.setMessageConverters(createConverters());
			client.setInterceptors(getRequestInterceptors());
			return client;
		}
		return client;
	}

	public static List<ClientHttpRequestInterceptor> getRequestInterceptors(){
		RewritePropertiesRequestInterceptor rewriterRequest = new RewritePropertiesRequestInterceptor();
		LoggingRequestInterceptor loggingRequestInterceptor = new LoggingRequestInterceptor();
		List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
		ris.add(loggingRequestInterceptor);
		ris.add(rewriterRequest);
		return ris;
	}
	
	private static List<HttpMessageConverter<?>> createConverters() {
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converters.add(new StringHttpMessageConverter());
		converters.add(converter);
		converter.setObjectMapper(ParserUtil.getJsonParser());
		return converters;
	}

}
