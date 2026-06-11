package com.naman.capstone.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Defines methods for image upload operations
 */
public interface FileUploadService {

    /**
     * Uploads an image file
     * @param type image type
     * @param file image file to upload
     * @return path of uploaded image
     */
    String uploadFile(String type, MultipartFile file);
}