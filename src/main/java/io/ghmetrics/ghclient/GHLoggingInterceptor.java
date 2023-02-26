package io.ghmetrics.ghclient;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Priority(Priorities.USER)
public class GHLoggingInterceptor implements ClientRequestFilter, ClientResponseFilter {
    private final int maxEntitySize = 1024 * 10;


    @Override
    public void filter(ClientRequestContext req, ClientResponseContext res) throws IOException {
        log.info("Received {} {} {} ", req.getUri(), res.getStatus(), res.getHeaders());
        dumpResponseBody(res);
    }

    @Override
    public void filter(ClientRequestContext req) throws IOException {
        log.info("Sending {} {} {} ", req.getMethod(), req.getUri(), req.getStringHeaders());
    }

    private void dumpResponseBody(ClientResponseContext responseContext) {
        try {
            InputStream stream = responseContext.getEntityStream();
            if (!stream.markSupported()) {
                stream = new BufferedInputStream(stream);
            }
            stream.mark(maxEntitySize + 1);
            StringBuilder b = new StringBuilder();
            stream.mark(maxEntitySize + 1);
            final byte[] entity = new byte[maxEntitySize + 1];
            final int entitySize = stream.read(entity);
            if (entitySize <= 0) {
                b.append("<EMPTY BODY>");
            } else {
                b.append(new String(entity, 0, Math.min(entitySize, maxEntitySize), StandardCharsets.UTF_8));
                if (entitySize > maxEntitySize) {
                    b.append("...more...");
                }
            }
            log.info("Response: " + b);
            stream.reset();
            responseContext.setEntityStream(stream);
        } catch (Exception e) {
            log.error("Failed to reset entity stream", e);
        }
    }

}
