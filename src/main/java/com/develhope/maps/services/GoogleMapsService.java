package com.develhope.maps.services;

import com.develhope.maps.models.GoogleMapsInputMatrix;
import com.develhope.maps.models.GoogleMapsRoute;
import com.develhope.maps.models.components.Destination;
import com.develhope.maps.models.components.Origin;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class GoogleMapsService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    public GoogleMapsRoute calculateRoute(
            Double startLatitude,
            Double startLongitude,
            Double endLatitude,
            Double endLongitude) throws Exception {

        if (startLatitude == null || startLongitude == null || endLatitude == null || endLongitude == null) {
            throw new Exception("No coordinates insert for this experience");
        }

        // Create Origin and Destination
        Origin origin = new Origin(startLatitude, startLongitude);
        Destination destination = new Destination(endLatitude, endLongitude);

        // Create Input Matrix
        GoogleMapsInputMatrix mapsInput = new GoogleMapsInputMatrix(List.of(origin), List.of(destination));

        // Convert mapsInput in JSON format string
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(mapsInput);

        // Open connection with Google Maps Server
        HttpURLConnection connection = getHttpURLConnection(jsonInput);

        // Read response
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // Remove "[" and "]" from Google response (because it usually returns a list of routes)
            response.deleteCharAt(0);
            response.deleteCharAt(response.length() - 1);

            // Convert response from JSON format string to GoogleMapsRoute list
            return mapper.readValue(response.toString(), new TypeReference<>() {});
        }
    }

    private HttpURLConnection getHttpURLConnection(String jsonInput) throws IOException {
        URL url = new URL("https://routes.googleapis.com/distanceMatrix/v2:computeRouteMatrix");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set request properties
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-Goog-FieldMask", "distanceMeters");
        connection.setRequestProperty("X-Goog-Api-Key", apiKey);
        connection.setDoOutput(true);

        // Send request
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }
}
