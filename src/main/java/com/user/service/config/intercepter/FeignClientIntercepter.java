package com.user.service.config.intercepter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
@Component
public class FeignClientIntercepter implements RequestInterceptor{
	
	@Autowired
	private OAuth2AuthorizedClientManager manager;

	@Override
	public void apply(RequestTemplate template) {
		
	String token = 	manager.authorize(OAuth2AuthorizeRequest.withClientRegistrationId("my-internal-client")
				.principal("internal").build()).getAccessToken().getTokenValue();
		
		template.header("Authorization", "Bearer "+token);
		
	}

}
