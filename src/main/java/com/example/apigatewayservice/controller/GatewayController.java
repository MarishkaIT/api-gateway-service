package com.example.apigatewayservice.controller;

import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GatewayController {


    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/payments")
    public String getPayments() {
        InstanceInfo paymentServiceInstance = getPaymentServiceInstance();
        if (paymentServiceInstance != null) {
            String paymentServiceUrl = paymentServiceInstance.getHomePageUrl();
            return restTemplate.getForObject(paymentServiceUrl + "/payments", String.class);
        }else {
            return "Payment service is not available";
        }
    }

    private InstanceInfo getPaymentServiceInstance() {
        List<ServiceInstance> infoList = discoveryClient.getInstances("payment-service");
        if (!infoList.isEmpty()) {
            return (InstanceInfo) infoList.get(0);
        }else {
            return null;
        }
    }

}
