package com.mido.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class StorageUtl {
    public static void saveFile(String uploadDirectory,
                         String fileName,
                         MultipartFile file) throws IOException {

        Path path = Paths.get(uploadDirectory);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }

    }
}
