package io.github.henriquewegner.EcommerceOrderServiceApi.infrastructure.integration.apis;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
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

        var manager = new DefaultOAuth2AuthorizedClientManager(clients, authRepo);
        manager.setAuthorizedClientProvider(provider);

        this.clientManager = manager;
        this.webClient = WebClient.builder().build();
    }

    public <T> T callApi(
            String clientRegistrationId,
            String url,
            Function<WebClient.ResponseSpec, Mono<T>> responseExtractor
    ) {
        var authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistrationId)
                .principal("order-api")
                .build();

        var client = clientManager.authorize(authorizeRequest);

        var responseSpec = webClient.get()
                .uri(url)
                .headers(h -> h.setBearerAuth(client.getAccessToken().getTokenValue()))
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("Error 5xx in External Api"))
                )
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.empty()
                );


        return responseExtractor.apply(responseSpec).block();
    }

    public <T> T callApi(String clientRegistrationId, String url, Class<T> responseType) {
        return callApi(clientRegistrationId, url, req -> req.bodyToMono(responseType));
    }

    public <T> T callApi(String clientRegistrationId, String url, ParameterizedTypeReference<T> responseType) {
        return callApi(clientRegistrationId, url, req -> req.bodyToMono(responseType));
    }

}
