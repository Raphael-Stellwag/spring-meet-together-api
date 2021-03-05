package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.spring.meettogether.SpringMeetTogetherApiApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = SpringMeetTogetherApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PingApiImplIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    //
    //Raphi Chrome Private
    //Basic NjAzYmQyY2E5ZDg0NGY0NjRlN2Q3OTJjOlFwYk1qakd4Q3NDdE5oODdhbHdJ
    //603bd2ca9d844f464e7d792c:QpbMjjGxCsCtNh87alwI
    //
    //Raphi Firefox
    //Basic NjAzYmQwZjI5ZDg0NGY0NjRlN2Q3OTEwOkN6MHlFV1k2QTVPSHZXRWs0enlZ
    //Basic
    //
    //Raphi Chrome
    //Basic NjAzYmQxNzg5ZDg0NGY0NjRlN2Q3OTFhOmtTS1FsRnBVYlpKbWxwQTVRdWxt
    //Basic
    @ParameterizedTest
    @ValueSource(strings = {"Basic NjAzYmQyY2E5ZDg0NGY0NjRlN2Q3OTJjOlFwYk1qakd4Q3NDdE5oODdhbHdJ",
            "Basic NjAzYmQwZjI5ZDg0NGY0NjRlN2Q3OTEwOkN6MHlFV1k2QTVPSHZXRWs0enlZ",
            "Basic NjAzYmQxNzg5ZDg0NGY0NjRlN2Q3OTFhOmtTS1FsRnBVYlpKbWxwQTVRdWxt"})
    void testBasicAuthCredentials(String headerValue) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(headerValue);

        mockMvc.perform(get("/api/v1/ping").headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    void testBasicAuthCredentials_invalidCredentials() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Basic NjAzYmQxgfhg5ZDg0NGY0NjRlN2Q3OTFhOmtTS1FsRnBVYlpKbWxwQTVRdWxt");

        mockMvc.perform(get("/api/v1/ping").headers(httpHeaders))
                .andExpect(status().isUnauthorized());
    }
}