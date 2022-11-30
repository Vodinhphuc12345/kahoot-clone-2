package com.group2.kahootclone.service.Implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.group2.kahootclone.service.Interface.ICloudinaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class CloudinaryService implements ICloudinaryService {
    @Autowired
    Cloudinary cloudinary;

    @Override
    public Map upload(MultipartFile image, Map options) {
        try {
            return cloudinary.uploader().upload(image.getBytes(), options);
        } catch (IOException e) {
            log.error(e.getMessage(), e.getCause());
            return null;
        }
    }

    @Override
    public String createUrl(String name, int width,
                            int height, String action) {
        return cloudinary.url()
                .transformation(new Transformation()
                        .width(width).height(height)
                        .border("2px_solid_black").crop(action))
                .imageTag(name);
    }
}
