package com.example.HRMSAvisoft.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class EmployeeControllerTests {

//    HttpClient client;
//    String port;
//
//    @Value("${offset.uploadImage.url}")
//    private String uploadImageUrl;
//
//    @BeforeEach
//    public void setUp() throws SQLException {
//        client = HttpClient.newHttpClient();
//        port = "5555";
//    }

//    @Test
//    @DisplayName("test_image_upload_success")
//    @Transactional
//    void test_imageUploadSuccess() throws IOException, InterruptedException{
//
//        MultipartFile file = Mockito.mock(MultipartFile.class);
//
//        byte[] fileContent = "file content".getBytes();
//        Mockito.when(file.getBytes()).thenReturn(fileContent);
//
//        HttpRequest postRequest = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:5555/api/v1/employee/1L/uploadImage"))
//                .header("Content-Type", "multipart/form-data")
//                .POST(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
//                .build();
//
//        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
//        assertEquals(200, postResponse.statusCode());
//    }


}


