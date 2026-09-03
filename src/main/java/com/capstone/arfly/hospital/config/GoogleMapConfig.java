package com.capstone.arfly.hospital.config;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.maps.GeoApiContext;
import com.google.maps.places.v1.PlacesClient;
import com.google.maps.places.v1.PlacesSettings;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class GoogleMapConfig {
    @Value("${GOOGLE_MAP_KEY}")
    private String apiKey;

    @Bean(destroyMethod = "close")
    public PlacesClient placesClient() throws IOException {

        PlacesSettings settings = PlacesSettings.newBuilder()
                .setCredentialsProvider(NoCredentialsProvider.create())
                .setHeaderProvider(() -> {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("X-Goog-Api-Key", apiKey);
                    return headers;
                })
                .build();

        log.info("구글 맵 api 초기 설정 완료!");

        return PlacesClient.create(settings);
    }

    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder().apiKey(apiKey).build();
    }
}
