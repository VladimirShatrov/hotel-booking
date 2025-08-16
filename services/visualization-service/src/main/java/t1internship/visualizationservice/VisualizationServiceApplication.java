package t1internship.visualizationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VisualizationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VisualizationServiceApplication.class, args);
    }

}
