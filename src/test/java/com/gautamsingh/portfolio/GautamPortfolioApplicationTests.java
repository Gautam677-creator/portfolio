package com.gautamsingh.portfolio;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GautamPortfolioApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void profileEndpointReturnsProfile() throws Exception {
        mockMvc.perform(get("/api/v1/gautam/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gautam Kumar Singh"))
                .andExpect(jsonPath("$.college").value("Rungta College of Engineering and Technology"))
                .andExpect(jsonPath("$.hackathons").value(10));
    }

    @Test
    void contactEndpointStoresInquiry() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/gautam/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Recruiter\",\"email\":\"r@example.com\","
                                + "\"subject\":\"Backend Role\",\"message\":\"Hello Gautam\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value(Matchers.containsString("Recruiter")));
    }

    @Test
    void contactEndpointRejectsInvalidPayload() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/gautam/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"not-an-email\","
                                + "\"subject\":\"\",\"message\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"));
    }

    @Test
    void healthEndpointIsUp() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void staticFrontendIsServed() throws Exception {
        // MockMvc does not render the welcome-page forward, so assert the forward
        // target and separately confirm the HTML is packaged on the classpath.
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("index.html"));

        try (var input = getClass().getResourceAsStream("/static/index.html")) {
            assertNotNull(input, "frontend index.html must be packaged on the classpath");
        }
    }
}
