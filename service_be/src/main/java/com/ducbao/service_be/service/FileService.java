package com.ducbao.service_be.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ducbao.common.model.constant.FileConstant;
import com.ducbao.common.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final Cloudinary cloudinary;

    public String upload(MultipartFile file, String userId, String typeUpload) {
        try {
            String filePathKey = getFilePathKey(file.getOriginalFilename(), userId, typeUpload);
            byte[] bytes = resizeImage(file.getBytes());
            log.info("Image format: {}", getImageFormat(bytes));
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            Map<String, Object> uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.asMap("public_id", filePathKey));
            log.info("Upload result: {}", uploadResult);
            String link = (String) uploadResult.get("url"); // Lấy link bảo mật

            return link;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @SneakyThrows
    private byte[] resizeImage(byte[] bytes) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            int resizeDimension = FileConstant.FILE_IMAGE_RESIZE_DIMENSION_MAX;
            if (Math.max(originalWidth, originalHeight) > resizeDimension) {
                String formatName = getImageFormat(bytes);
                if (!Util.isNullOrEmpty(formatName)) {
                    // Resize the image with max of with or height is 480px
                    int newWidth;
                    int newHeight;
                    if (originalWidth > originalHeight) {
                        newWidth = FileConstant.FILE_IMAGE_RESIZE_DIMENSION_MAX;
                        newHeight = (int) (((double) originalHeight / originalWidth) * newWidth);
                    } else {
                        newHeight = FileConstant.FILE_IMAGE_RESIZE_DIMENSION_MAX;
                        newWidth = (int) (((double) originalWidth / originalHeight) * newHeight);
                    }
                    image = getBufferedImageResize(image, newWidth, newHeight);
                    ImageIO.write(image, formatName, outputStream);
                    bytes = outputStream.toByteArray();
                    outputStream.reset();
                }
            }
            return bytes;

        }catch (IOException e){
            log.error(e.getMessage());
            throw e;
        }
    }
    private static BufferedImage getBufferedImageResize(BufferedImage originalImage, int width, int height) {
        int imageType = originalImage.getType();
        if (imageType == BufferedImage.TYPE_CUSTOM) {
            imageType = BufferedImage.TYPE_INT_ARGB; // Avoid error case that BufferedImage does not accept TYPE_CUSTOM
        }

        BufferedImage resizedImage = new BufferedImage(width, height, imageType);

        Graphics2D graphics = resizedImage.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); // for better image quality
        graphics.drawImage(originalImage, 0, 0, width, height, null);
        graphics.dispose();

        return resizedImage;
    }


    private String getFilePathKey(String originalFilename, String userId, String typeUpload) {
        // Generate new filename
        Date current = new Date();

        String newFileName = String.valueOf(current.getTime());
        String modifiedFilename = modifyFilename(originalFilename, newFileName);

        return Util.generateFileDirectory(typeUpload, userId, modifiedFilename);
    }

    private String getFilePathKey(String userId, String typeUpload) {
        return Util.generateFileDirectory(typeUpload, userId);
    }
    private String modifyFilename(String originalFilename, String newFilename) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        return newFilename + fileExtension;
    }
    @SneakyThrows
    private static String getImageFormat(byte[] imageData) {
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(imageData));

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

            if (imageReaders.hasNext()) {
                ImageReader reader = imageReaders.next();
                return reader.getFormatName();
            }
        } catch (IOException e) {
            log.error("Get image format failed " + e.getMessage(), e);

            return null;
        }

        return null;
    }
    public void deleteImage(String url) {
        try {
            String publicId = extractPublicId(url);
            Map<String, Object> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Delete result: {}", deleteResult);
        } catch (Exception e) {
            log.error("Error during delete: {}", e.getMessage());
        }
    }
    public String extractPublicId(String url) {
        // Tách phần mở rộng
        String[] urlParts = url.split("\\.");
        String publicIdWithFolder = urlParts[0]; // Lấy phần không có mở rộng

        // Tách theo dấu '/'
        String[] pathParts = publicIdWithFolder.split("/");
        String publicId = pathParts[pathParts.length - 2] + "/" + pathParts[pathParts.length - 1]; // Lấy phần cuối cùng

        return publicId;
    }
}
