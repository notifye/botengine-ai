package io.notifye.botengine.client.interceptor;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		ClientHttpResponse response = execution.execute(request, body);

		traceRequest(request, body);
        return response;
	}
	
	private void traceRequest(HttpRequest request, byte[] body) throws IOException {
		log.debug("===========================request begin================================================");
	    log.debug("URI         : {}", request.getURI());
	    log.debug("Method      : {}", request.getMethod());
	    log.debug("Headers     : {}", request.getHeaders() );
	    log.debug("Request body: {}", new String(body, "UTF-8"));
	    log.debug("==========================request end================================================");
	}

}
