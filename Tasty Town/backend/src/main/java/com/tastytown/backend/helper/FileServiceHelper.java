package com.tastytown.backend.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileServiceHelper {

    @Value("${upload.file.dir}") // Spring Expression Language
	private String FILE_DIR;
    
    public String uploadFile(MultipartFile foodImage) throws IOException{
        if(!foodImage.isEmpty()){
            var fileName = foodImage.getOriginalFilename(); // It extracts full name including extension
            var newFileName = generateFileName(fileName);

            var fos = new FileOutputStream(FILE_DIR + File.separator +newFileName);
            fos.write(foodImage.getBytes());
            fos.close();
            return newFileName;
        }

        throw new FileNotFoundException("Food Image not found");
    }

    public String generateFileName(String fileName){
        var extensionName = fileName.substring(fileName.lastIndexOf("."));
        var newFileName = UUID.randomUUID().toString();
        return newFileName +  extensionName;
    }

    public void deleteFoodImage(String foodImageName) throws IOException{
        var file = new File(FILE_DIR + File.separator + foodImageName);
        
        if(!file.exists()){
            throw new FileNotFoundException("Food Image not found");
        }

        file.delete();
    }

}
