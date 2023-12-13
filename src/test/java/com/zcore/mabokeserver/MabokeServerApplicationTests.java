package com.zcore.mabokeserver;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MabokeServerApplicationTests {
    /*@Autowired
    TestRestTemplate restTemplate;

	@Test
    void shouldReturnADriveWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate.getForEntity("/drive/28", String.class);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");

        //assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(id).isEqualTo(28);
    }

    @Test
    void shouldNotReturnADriveWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/drive/1000", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }*/
}
