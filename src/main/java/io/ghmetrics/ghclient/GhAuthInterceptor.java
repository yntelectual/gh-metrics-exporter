package io.ghmetrics.ghclient;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Priority(Priorities.USER)
public class GhAuthInterceptor implements ClientRequestFilter {
    @ConfigProperty(name = "github.api.token")
    Optional<String> token;

    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException {
        if (token.isEmpty()) {
            throw new IllegalArgumentException("Github token empty. Provide `github.api.token` value.");
        }
        String authHeaderValue = "Bearer " + token.get();
        clientRequestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeaderValue);
        clientRequestContext.getHeaders().add("X-GitHub-Api-Version", "2022-11-28");
    }
}
