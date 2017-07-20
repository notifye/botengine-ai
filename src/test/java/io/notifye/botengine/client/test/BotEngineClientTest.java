package io.notifye.botengine.client.test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class BotEngineClientTest {
	private static String clientAccessToken = "e5b129af8161ed369e27804f9a2735fa81a43d2e911e661195c6c7160b70b27b";
	private static String devAccessToken = "Bearer 9bc76810e6767d68970095e8db817a79134245b096eddcd645d34f19996751a1";
	private static RestTemplate client = new RestTemplate();
	private static String baseUrl = "https://api.botengine.ai";	

	public static void main(String[] args){
		String resource = baseUrl + "/stories";
	
		HttpHeaders headers = new HttpHeaders();
		headers.add("authorization", devAccessToken);
		
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> json = client.exchange(resource, HttpMethod.GET, entity, String.class);
		System.out.println("Body -> " + json.getBody());		
	}
	

}
