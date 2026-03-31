package com.cravecart;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontendProxyController {

    private static final Set<String> RESTRICTED_HEADERS = Set.of(
            HttpHeaders.HOST,
            HttpHeaders.CONTENT_LENGTH,
            HttpHeaders.TRANSFER_ENCODING,
            HttpHeaders.CONNECTION,
            HttpHeaders.UPGRADE,
            "keep-alive",
            "proxy-authenticate",
            "proxy-authorization",
            "te",
            "trailer",
            "proxy-connection"
    );

    private final HttpClient httpClient;
    private final String frontendBaseUrl;

    public FrontendProxyController(
            @Value("${frontend.proxy.url:http://localhost:3000}") String frontendBaseUrl
    ) {
        this.httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
        this.frontendBaseUrl = frontendBaseUrl;
    }

    @RequestMapping(value = "/**", method = {
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.PATCH,
            RequestMethod.DELETE,
            RequestMethod.OPTIONS,
            RequestMethod.HEAD
    })
    @ResponseBody
    public ResponseEntity<byte[]> proxy(HttpServletRequest request,
                                        @RequestBody(required = false) byte[] body)
            throws IOException, InterruptedException {
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/api/")) {
            return ResponseEntity.notFound().build();
        }

        String queryString = request.getQueryString();
        URI uri = UriComponentsBuilder.fromHttpUrl(frontendBaseUrl)
                .path(requestUri)
                .query(queryString)
                .build(true)
                .toUri();

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri)
                .method(request.getMethod(), body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofByteArray(body));

        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            if (RESTRICTED_HEADERS.contains(headerName.toLowerCase())) {
                return;
            }
            Collections.list(request.getHeaders(headerName)).forEach(headerValue -> requestBuilder.header(headerName, headerValue));
        });

        HttpResponse<byte[]> proxyResponse = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());
        HttpHeaders responseHeaders = new HttpHeaders();
        proxyResponse.headers().map().forEach((name, values) -> {
            if (!name.equalsIgnoreCase(HttpHeaders.TRANSFER_ENCODING)
                    && !name.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)) {
                responseHeaders.put(name, values);
            }
        });

        return ResponseEntity.status(proxyResponse.statusCode())
                .headers(responseHeaders)
                .body(proxyResponse.body());
    }
}
