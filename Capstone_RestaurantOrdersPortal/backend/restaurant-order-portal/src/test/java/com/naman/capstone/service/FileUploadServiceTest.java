package com.naman.capstone.service;

import com.naman.capstone.service.impl.FileUploadServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test cases for file upload
 */
class FileUploadServiceTest {

    private final FileUploadServiceImpl fileUploadService = new FileUploadServiceImpl();


    /**
     * testing successful file upload
     */
    @Test
    void upload_file_success() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "naman_test.png",
                "image/png",
                "dummy image".getBytes()
        );

        String result = fileUploadService.uploadFile("restaurant", file);

        assertNotNull(result);
        assertTrue(result.contains("/restaurant/"));
    }


    /**
     * testing upload with empty file
     */
    @Test
    void upload_file_empty() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "naman_test.png",
                "image/png",
                new byte[0]
        );

        assertThrows(RuntimeException.class, () -> fileUploadService.uploadFile("restaurant", file));
    }


    /**
     * testing upload when filename is null
     */
    @Test
    void upload_file_with_null_filename() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                null,
                "image/png",
                "dummy".getBytes()
        );

        String result = fileUploadService.uploadFile("restaurant", file);

        assertNotNull(result);
    }


    /**
     * testing upload with different type folder
     */
    @Test
    void upload_file_different_type() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "naman_profile.jpg",
                "image/jpeg",
                "image".getBytes()
        );

        String result = fileUploadService.uploadFile("user", file);

        assertTrue(result.contains("/user/"));
    }
}