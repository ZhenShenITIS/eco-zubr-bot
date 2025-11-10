package itis.ecozubrbot.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import itis.ecozubrbot.services.GeoLocationService;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class YandexGeoLocationService implements GeoLocationService {

    @Value("${YANDEX_GEOCODER_TOKEN}")
    private String YANDEX_GEOCODER_TOKEN;

    private final HttpClient http =
            HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String getCity(double lat, double lon) {
        try {
            String geocodeRaw = String.format(Locale.US, "%.8f,%.8f", lon, lat);
            String query = "apikey=" + URLEncoder.encode(YANDEX_GEOCODER_TOKEN, StandardCharsets.UTF_8)
                    + "&geocode=" + geocodeRaw
                    + "&lang=ru_RU"
                    + "&kind=locality"
                    + "&sco=longlat"
                    + "&format=json"
                    + "&results=1";

            URI uri = new URI("https", "geocode-maps.yandex.ru", "/v1", query, null);

            HttpRequest req = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(10))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            String body = resp.body();

            if (resp.statusCode() != 200) return "";

            JsonNode root = MAPPER.readTree(body);
            JsonNode members = root.path("response").path("GeoObjectCollection").path("featureMember");
            if (!members.isArray() || members.size() == 0) return "";

            JsonNode geoObject = members.get(0).path("GeoObject");
            JsonNode comps = geoObject
                    .path("metaDataProperty")
                    .path("GeocoderMetaData")
                    .path("Address")
                    .path("Components");
            String city = findComponentByKinds(comps, "locality", "area", "province", "region");

            if ((city == null || city.isBlank())
                    && "locality"
                            .equalsIgnoreCase(geoObject
                                    .path("metaDataProperty")
                                    .path("GeocoderMetaData")
                                    .path("kind")
                                    .asText(""))) {
                city = geoObject.path("name").asText("");
            }
            return city == null ? "" : city;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return "";
        }
    }

    private String findComponentByKinds(JsonNode comps, String... kinds) {
        if (comps != null && comps.isArray()) {
            for (String k : kinds) {
                for (JsonNode c : comps) {
                    String kind = c.path("kind").asText("");
                    String name = c.path("name").asText("");
                    if (k.equalsIgnoreCase(kind) && !name.isBlank()) {
                        return name;
                    }
                }
            }
        }
        return null;
    }

    private static String head(String s, int n) {
        if (s == null) return "null";
        return s.length() <= n ? s : s.substring(0, n) + "...";
    }

    private static String maskApiKey(String url) {
        if (url == null) return null;
        return url.replaceAll("apikey=[^&]+", "apikey=***");
    }
}
