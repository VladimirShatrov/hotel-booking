package t1internship.visualizationservice.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PlacesServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public PlacesServiceClient(@Value("${places-service.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();

    }

    public boolean checkFloorExists(Long floorId) {
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(
                    baseUrl + "/floors/{floorId}/exists", Boolean.class, floorId);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Boolean.TRUE.equals(response.getBody());
            }
            return false;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            throw e;
        }
    }

    public boolean checkPlaceExists(Long placeId) {
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(
                    baseUrl + "/places/{placeId}/exists", Boolean.class, placeId);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Boolean.TRUE.equals(response.getBody());
            }
            return false;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            throw e;
        }
    }
}

