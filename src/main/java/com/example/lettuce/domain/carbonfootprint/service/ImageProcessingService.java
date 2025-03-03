package com.example.lettuce.domain.carbonfootprint.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for processing images using OpenCV before sending to GPT-4 Vision.
 * This improves the accuracy of carbon footprint calculations.
 */
@Slf4j
@Service
public class ImageProcessingService {

    /**
     * Preprocesses an image for better analysis by GPT-4 Vision.
     * 
     * @param imageFile The image file to process
     * @return Base64 encoded string of the processed image
     * @throws IOException If there's an error processing the image
     */
    public String preprocessImage(MultipartFile imageFile) throws IOException {
        // Convert MultipartFile to Mat
        byte[] imageBytes = imageFile.getBytes();
        Mat originalImage = bytesToMat(imageBytes);
        
        // Apply preprocessing steps
        Mat processedImage = enhanceImage(originalImage);
        
        // Convert back to base64 for API consumption
        return matToBase64(processedImage);
    }
    
    /**
     * Enhances an image for better object recognition.
     * 
     * @param originalImage The original image as a Mat
     * @return The enhanced image as a Mat
     */
    private Mat enhanceImage(Mat originalImage) {
        Mat enhancedImage = new Mat();
        
        // Resize if needed (maintain aspect ratio)
        if (originalImage.width() > 1024 || originalImage.height() > 1024) {
            double scale = Math.min(1024.0 / originalImage.width(), 1024.0 / originalImage.height());
            Size newSize = new Size(originalImage.width() * scale, originalImage.height() * scale);
            Imgproc.resize(originalImage, enhancedImage, newSize, 0, 0, Imgproc.INTER_AREA);
        } else {
            originalImage.copyTo(enhancedImage);
        }
        
        // Convert to RGB if needed
        if (enhancedImage.channels() == 4) {
            Mat rgbImage = new Mat();
            Imgproc.cvtColor(enhancedImage, rgbImage, Imgproc.COLOR_BGRA2BGR);
            enhancedImage = rgbImage;
        }
        
        // Apply noise reduction
        Mat denoisedImage = new Mat();
        Imgproc.GaussianBlur(enhancedImage, denoisedImage, new Size(3, 3), 0);
        
        // Enhance contrast
        Mat contrastEnhanced = new Mat();
        Core.normalize(denoisedImage, contrastEnhanced, 0, 255, Core.NORM_MINMAX);
        
        // Sharpen the image
        Mat sharpenedImage = new Mat();
        Mat kernel = new Mat(3, 3, CvType.CV_32F);
        kernel.put(0, 0, 0, -1, 0, -1, 5, -1, 0, -1, 0);
        Imgproc.filter2D(contrastEnhanced, sharpenedImage, -1, kernel);
        
        return sharpenedImage;
    }
    
    /**
     * Converts a byte array to an OpenCV Mat.
     */
    private Mat bytesToMat(byte[] imageBytes) {
        MatOfByte matOfByte = new MatOfByte(imageBytes);
        return Imgcodecs.imdecode(matOfByte, Imgcodecs.IMREAD_UNCHANGED);
    }
    
    /**
     * Converts an OpenCV Mat to a Base64 encoded string.
     */
    private String matToBase64(Mat mat) throws IOException {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(byteArray));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
} 