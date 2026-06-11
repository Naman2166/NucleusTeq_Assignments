package com.naman.capstone.service.impl;

import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.naman.capstone.constant.AppConstants.*;

/**
 * implementation for image upload related operations
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {


    private static final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    /**
     * uploads the file in backend folder and returns its path
     * @param type image type
     * @param file image file to upload
     * @return path of uploaded image
     */
    @Override
    public String uploadFile(String type, MultipartFile file) {

        try {

            log.info("Starting file upload for type: {}", type);

            //Validate file
            if (file.isEmpty()) {
                log.warn("Upload failed: file is empty");
                throw new ResourceNotFoundException("File is empty");
            }

            // Build folder path
            String folder = IMAGE_PATH + "/" + type + "/";
            File dir = new File(folder);
            if (!dir.exists()) dir.mkdirs();

            // Unique filename
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(folder + fileName);
            Files.copy(file.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            log.info("File uploaded successfully: {}", fileName);
            log.info("Saving file to: {}", new File(folder).getAbsolutePath());

            // Return public URL
            return "/" + type + "/" + fileName;


        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }
}