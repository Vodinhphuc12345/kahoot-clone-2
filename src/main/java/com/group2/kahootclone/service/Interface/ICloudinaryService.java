package com.group2.kahootclone.service.Interface;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ICloudinaryService {
    public Map upload(MultipartFile file, Map options);

    public String createUrl(String name, int width,
                            int height, String action);
}
