package t1internship.visualizationservice.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;

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
            return checkExistence("floors/{id}/exists", floorId);
        } catch (ServiceUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkPlaceExists(Long placeId) {
        try {
            return checkExistence("places/{id}/exists", placeId);
        } catch (ServiceUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkExistence(String endpoint, Long id) throws ServiceUnavailableException {
        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(
                    baseUrl + endpoint,
                    Void.class,
                    id
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (RestClientException e) {
            throw new ServiceUnavailableException(
                    "Failed to check existence. Service unavailable: " + e.getMessage()
            );
        }
    }
}

