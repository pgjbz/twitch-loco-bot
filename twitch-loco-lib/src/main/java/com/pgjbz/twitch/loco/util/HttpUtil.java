package com.pgjbz.twitch.loco.util;

import com.pgjbz.twitch.loco.enums.HttpMethod;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Log4j2
public class HttpUtil {

    private HttpUtil() {
    }

    /**
     * @return String body
     */
    public static String execute(HttpMethod httpMethod,
                                 String url,
                                 String body,
                                 @NonNull Map<String, String> headers,
                                 @NonNull Map<String, String> params) throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newBuilder()
                .proxy(ProxySelector.getDefault())
                .version(HttpClient.Version.HTTP_2)
                .build();


        String formattedUrl = formatUrl(url, params);

        headers.put("Content-Type", "application/json");

        HttpRequest httpRequest = createBuilderWithMethod(httpMethod, body)
                .uri(URI.create(formattedUrl))
                .headers(extractMapToArray(headers))
                .build();

        log.info("Realizing request {}", httpRequest);

        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return send.body();
    }

    public static String execute(HttpMethod httpMethod, String url) throws IOException, InterruptedException {
        return execute(httpMethod, url, "", new HashMap<>(), new HashMap<>());
    }

    private static String formatUrl(String url, Map<String, String> params) {
        if (params.isEmpty()) return url;
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        for (String key : params.keySet()) {
            if (sb.length() > url.length()) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    key,
                    params.get(key)));
        }
        return sb.toString();
    }

    /**
     * @return Return a pair of sequence [key, value]
     */
    private static String[] extractMapToArray(Map<String, String> headers) {
        List<String> header = new ArrayList<>();
        for (String key : headers.keySet()) {
            header.add(key);
            header.add(headers.get(key));
        }
        return header.toArray(String[]::new);
    }

    private static HttpRequest.Builder createBuilderWithMethod(HttpMethod httpMethod, String body) {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        switch (httpMethod) {
            case GET:
                builder.GET();
                break;
            case POST:
                builder.POST(HttpRequest.BodyPublishers.ofString(body));
                break;
        }
        return builder;
    }

}
