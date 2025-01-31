package com.alenedaj.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class MapboxUtil {
    @Value("${mapbox.api-key}")
    private String mapboxApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON Parser

    // Convert place name to latitude and longitude using Mapbox API
    public double[] getCoordinates(String placeName) {
        if (placeName == null || placeName.isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }

        // ✅ Encode location to avoid errors
        String encodedPlaceName = UriUtils.encodePath(placeName, StandardCharsets.UTF_8);

        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/"
                + encodedPlaceName + ".json?access_token=" + mapboxApiKey;

        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (!jsonNode.has("features") || jsonNode.get("features").isEmpty()) {
                throw new RuntimeException("Location not found for: " + placeName);
            }

            JsonNode location = jsonNode.get("features").get(0);
            double lon = location.get("center").get(0).asDouble();
            double lat = location.get("center").get(1).asDouble();

            return new double[]{lat, lon};
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Mapbox response: " + e.getMessage());
        }
    }

    public String getDirections(String place1, String place2) {
        double[] coords1 = getCoordinates(place1);
        double[] coords2 = getCoordinates(place2);

        String url = "https://api.mapbox.com/directions/v5/mapbox/driving/"
                + coords1[1] + "," + coords1[0] + ";" + coords2[1] + "," + coords2[0]
                + "?access_token=" + mapboxApiKey;

        return restTemplate.getForObject(url, String.class);
    }

    // Calculate distance between two locations using Haversine formula
    public double calculateDistance(double[] coords1, double[] coords2) {

        final int EARTH_RADIUS = 6371; // Earth radius in km

        double latDistance = Math.toRadians(coords2[0] - coords1[0]);
        double lonDistance = Math.toRadians(coords2[1] - coords1[1]);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(coords1[0])) * Math.cos(Math.toRadians(coords2[0]))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // Distance in km
    }
}
