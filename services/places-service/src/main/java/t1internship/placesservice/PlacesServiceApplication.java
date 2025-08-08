package t1internship.placesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PlacesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlacesServiceApplication.class, args);
    }

}
