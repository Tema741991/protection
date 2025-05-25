package ru.top.cinemas.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.Patch;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    @Value("${upload.directory}")
    private String uploadDirectory;

    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Файл пуст");
        }
        Path uploadPath = Paths.get(uploadDirectory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }


        return "/uploads/posters/" + fileName;
    }

    public void deleteFile(String filePath) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            return; // Нет файла — ничего не делаем
        }
        String normalizedFilePath = filePath.startsWith("/uploads/posters/")
                ? filePath.substring("/uploads/posters/".length())
                : filePath;
        Path path = Paths.get(uploadDirectory).resolve(normalizedFilePath).normalize();
        Path upload=Paths.get(uploadDirectory).normalize();
        if (!path.startsWith(upload)) {
            throw new IOException("Недопустимый путь к файлу!" + path);
        }
        Files.deleteIfExists(path);

    }
}
