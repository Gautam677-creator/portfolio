package com.gautamsingh.portfolio.controller;

import com.gautamsingh.portfolio.model.ApiResponse;
import com.gautamsingh.portfolio.model.ContactRequest;
import com.gautamsingh.portfolio.model.EngineerProfile;
import com.gautamsingh.portfolio.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/gautam")
public class GautamPortfolioController {

    private final ContactService contactService;

    public GautamPortfolioController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/profile")
    public ResponseEntity<EngineerProfile> getProfile() {
        return ResponseEntity.ok(new EngineerProfile(
                "Gautam Kumar Singh",
                "Rungta College of Engineering and Technology",
                List.of("Java Spring Boot", "Data Structures & Algorithms",
                        "DevOps (Docker/K8s)", "Polyglot Programming"),
                10
        ));
    }

    @PostMapping("/contact")
    public ResponseEntity<ApiResponse> submitInquiry(@Valid @RequestBody ContactRequest request) {
        contactService.save(request);
        return ResponseEntity.ok(ApiResponse.success(
                "Thank you " + request.name()
                        + "! Your inquiry has been logged and Gautam will get back to you soon."));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "gautam-portfolio-api",
                "timestamp", Instant.now().toString()
        ));
    }
}
