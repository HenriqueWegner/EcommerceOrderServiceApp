package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.apis;


import io.github.henriquewegner.EcommerceOrderServiceApi.web.common.exceptions.ExternalApiException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class OAuth2ApiClient {

    private final OAuth2AuthorizedClientManager clientManager;
    private final WebClient webClient;

    public OAuth2ApiClient(ClientRegistrationRepository clients,
                           OAuth2AuthorizedClientRepository authRepo){

        OAuth2AuthorizedClientProvider provider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        DefaultOAuth2AuthorizedClientManager manager =
                new DefaultOAuth2AuthorizedClientManager(clients, authRepo);

        manager.setAuthorizedClientProvider(provider);

        this.clientManager = manager;
        this.webClient = WebClient.builder().build();
    }

    public <T> T callApiGet(
            String clientRegistrationId,
            String url,
            Function<WebClient.ResponseSpec, Mono<T>> responseExtractor
    ) {
        OAuth2AuthorizedClient client = authorizeRequest(clientRegistrationId);

        WebClient.ResponseSpec responseSpec = webClient.get()
                .uri(url)
                .headers(h -> h.setBearerAuth(client.getAccessToken().getTokenValue()))
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("Error 5xx in External Api")))
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.empty());

        return responseExtractor.apply(responseSpec).block();
    }

    public <T> T callApiGet(String clientRegistrationId, String url, Class<T> responseType) {
        return callApiGet(clientRegistrationId, url, req -> req.bodyToMono(responseType));
    }

    public <T> T callApiGet(String clientRegistrationId, String url, ParameterizedTypeReference<T> responseType) {
        return callApiGet(clientRegistrationId, url, req -> req.bodyToMono(responseType));
    }

    public void callApiPut(
            String clientRegistrationId,
            String url,
            Object body
    ) {
        OAuth2AuthorizedClient client = authorizeRequest(clientRegistrationId);

        webClient.put()
                .uri(url)
                .headers(h -> h.setBearerAuth(client.getAccessToken().getTokenValue()))
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("Error 5xx in External Api.")))
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new ExternalApiException("Error 4xx in External Api.")))
                .bodyToMono(Void.class)
                .block();
    }


    private OAuth2AuthorizedClient authorizeRequest(String clientRegistrationId){
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistrationId)
                .principal("order-api")
                .build();

        OAuth2AuthorizedClient client = clientManager.authorize(authorizeRequest);

        return client;
    }

}
