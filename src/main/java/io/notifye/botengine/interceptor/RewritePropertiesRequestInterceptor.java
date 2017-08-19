package io.notifye.botengine.interceptor;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RewritePropertiesRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		String requestBody = new String(body, "UTF-8");
		
		requestBody = rewriteIfRootQuickReplyFound(requestBody);

        return execution.execute(request, requestBody.getBytes());
	}

	private String rewriteIfRootQuickReplyFound(String requestBody) {
		String request = requestBody;
		if(request.contains("\"quickReply\":{\"")) {
			log.debug("Request Body before rewrite -> {}", requestBody);
			log.debug("Match string to  rewrite");
			request = request.replace("\"quickReply\":{", "");
			request = request.replace("\"]}}]", "\"]}]");
			log.debug("Request Body after rewrite -> {}", request);
		}
		return request;
	}
	

}