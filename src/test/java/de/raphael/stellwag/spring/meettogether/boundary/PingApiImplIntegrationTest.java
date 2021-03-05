package de.raphael.stellwag.spring.meettogether.boundary;

import de.raphael.stellwag.spring.meettogether.SpringMeetTogetherApiApplication;
import org.junit.jupiter.api.Test;
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

    @Test
    void testBasicAuthCredentials_RaphiChromePrivate() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();

        //Raphi Chrome Private
        //603bd2ca9d844f464e7d792c:QpbMjjGxCsCtNh87alwI

        httpHeaders.add("Authorization", "Basic NjAzYmQyY2E5ZDg0NGY0NjRlN2Q3OTJjOlFwYk1qakd4Q3NDdE5oODdhbHdJ");

        mockMvc.perform(get("/api/v1/ping").headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    void testBasicAuthCredentials_RaphiFirefox() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        //Raphi Firefox
        //Basic NjAzYmQwZjI5ZDg0NGY0NjRlN2Q3OTEwOkN6MHlFV1k2QTVPSHZXRWs0enlZ
        //Basic
        httpHeaders.add("Authorization", "Basic NjAzYmQwZjI5ZDg0NGY0NjRlN2Q3OTEwOkN6MHlFV1k2QTVPSHZXRWs0enlZ");

        mockMvc.perform(get("/api/v1/ping").headers(httpHeaders))
                .andExpect(status().isOk());
    }


    @Test
    void testBasicAuthCredentials_RaphiChrome() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();

        //Raphi Chrome
        //Basic NjAzYmQxNzg5ZDg0NGY0NjRlN2Q3OTFhOmtTS1FsRnBVYlpKbWxwQTVRdWxt
        //Basic
        httpHeaders.add("Authorization", "Basic NjAzYmQxNzg5ZDg0NGY0NjRlN2Q3OTFhOmtTS1FsRnBVYlpKbWxwQTVRdWxt");

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