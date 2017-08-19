package io.notifye.botengine.interceptor;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RewritePropertiesRequestInterceptor implements ClientHttpRequestInterceptor {
	private static final String QUICKREPLY_ROOT_PATTERN = "\"quickReply\":{";
	private static final String QUICKREPLY_FINAL_PATTERN = "\"]}}]";

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		String requestBody = new String(body, "UTF-8");
		
		requestBody = rewriteIfRootQuickReplyFound(requestBody);

        return execution.execute(request, requestBody.getBytes());
	}

	private String rewriteIfRootQuickReplyFound(String requestBody) {
		String request = requestBody;
		if(request.contains(QUICKREPLY_ROOT_PATTERN)) {
			log.debug("Request Body before rewrite -> {}", requestBody);
			log.debug("Match string to  rewrite");
			request = request.replace(QUICKREPLY_ROOT_PATTERN, "");
			request = request.replace(QUICKREPLY_FINAL_PATTERN, "\"]}]");
			log.debug("Request Body after rewrite -> {}", request);
		}
		return request;
	}
	

}
