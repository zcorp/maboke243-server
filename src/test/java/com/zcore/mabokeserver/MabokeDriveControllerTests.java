package com.zcore.mabokeserver;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MabokeDriveControllerTests {
   /* @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void shouldReturnCreatedCode() { 
        Drive drive = new Drive("Hello world !");
        ResponseEntity<String> response = restTemplate.postForEntity("/drive", drive, String.class);
        assertThat(201).isEqualTo(response.getStatusCode().value());
    }

    @Test
    public void shoulReturnBadRequestCode() {
        Drive drive = new Drive();
        ResponseEntity<String> response = restTemplate.postForEntity("/drive", drive, String.class);
        assertThat(400).isEqualTo(response.getStatusCode().value());
        //assertThat(true).isEqualTo(response.getBody().contains("Missing request header"));
    }

    @Test
    public void shouldReturnDriveList() { 
        ResponseEntity<Drive[]> response = restTemplate.getForEntity("/drive", Drive[].class);
        Drive[] drives = response.getBody();
        assertThat(2).isEqualTo(drives.length);
    }

    @Test
    public void shouldReturnEmptyDriveList() { 
        ResponseEntity<Drive[]> response = restTemplate.getForEntity("/drive", Drive[].class);
        Drive[] drives = response.getBody();
        assertThat(2).isEqualTo(drives.length);
    }

    @Test
    public void shouldReturnADrive() {
        ResponseEntity<String> response = restTemplate.getForEntity("/drive/28", String.class);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");

        assertThat(id).isEqualTo(28);
    }

    @Test
    public void shouldNotReturnADriveWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/drive/1000", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    public void updateShouldReturnNoContent() {
        HttpEntity<Drive> requestEntity; 
        ResponseEntity<Drive> response = restTemplate.getForEntity("/drive/28", Drive.class);
        Drive drive = response.getBody();
        drive.setName("test1234");

        requestEntity = new HttpEntity<Drive>(drive);
        response = restTemplate.exchange("/drive", HttpMethod.PUT, requestEntity, Drive.class);
        assertThat(204).isEqualTo(response.getStatusCode().value());
    }

    @Test
    public void updateShouldReturnNotFound() {
        HttpEntity<Drive> requestEntity; 
        ResponseEntity<Drive> response = restTemplate.getForEntity("/drive/28", Drive.class);
        Drive drive = response.getBody();
        drive.setId(0L);

        requestEntity = new HttpEntity<Drive>(drive);
        response = restTemplate.exchange("/drive", HttpMethod.PUT, requestEntity, Drive.class);
        assertThat(404).isEqualTo(response.getStatusCode().value());
    }*/
    
    /*@Test
    public void deleteShouldReturnNoContent() {
        HttpEntity<Drive> requestEntity; 
        ResponseEntity<Drive> response = restTemplate.getForEntity("/drive/28", Drive.class);
        Drive drive = response.getBody();
        requestEntity = new HttpEntity<Drive>(drive);
        //response = restTemplate.exchange("/drive/" + drive.getId(), HttpMethod.DELETE, Drive.class);
        response = restTemplate.exchange("/drive/28", HttpMethod.DELETE, requestEntity, Drive.class, drive.getId());
        //restTemplate.delete("/drive/28" + drive.getId(), Void.class);
        //assertThat(204).isEqualTo(response.getStatusCode().value());
    }*/

    /*@Test
    public void deleteShouldReturnNotFound() {
        HttpEntity<Drive> requestEntity; 
        ResponseEntity<Drive> response = restTemplate.getForEntity("/drive/28", Drive.class);
        Drive drive = response.getBody();
        //drive.setId(0L);
        requestEntity = new HttpEntity<Drive>(drive);
        response = restTemplate.exchange("/drive", HttpMethod.DELETE, requestEntity, Drive.class);
        //assertThat(404).isEqualTo(response.getStatusCode().value());
    }*/
}