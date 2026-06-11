package com.naman.capstone.controller;

import com.naman.capstone.service.FileUploadService;
import com.naman.capstone.service.impl.FileUploadServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.naman.capstone.constant.AppConstants.*;

/**
 * handles API for image upload
 */
@RestController
@RequestMapping(IMAGE_UPLOAD_BASE_URL)
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    /**
     * handles image upload requests
     * @param type image type (restaurant or menu)
     * @param file image file to upload
     * @return path of uploaded image
     */
    @PostMapping(IMAGE_TYPE)
    public ResponseEntity<String> uploadFile(@PathVariable String type, @RequestParam("file") MultipartFile file) {
        logger.info("Received upload request for type: {}", type);
        String fileUrl = fileUploadService.uploadFile(type, file);
        logger.info("URl sent successfully for file type: {}", type);
        return ResponseEntity.ok(fileUrl);
    }
}