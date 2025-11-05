package org.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

/**
 * PUBLIC_INTERFACE
 * Stores and retrieves files from a configurable local directory.
 */
@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) throws IOException {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.root);
    }

    public String store(MultipartFile file) throws IOException {
        String filename = Path.of(file.getOriginalFilename()).getFileName().toString();
        Path dest = this.root.resolve(filename);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
        return dest.toString();
    }

    public Resource load(String filename) {
        Path file = root.resolve(filename).normalize();
        return new FileSystemResource(file);
    }

    public void deleteAll() throws IOException {
        FileSystemUtils.deleteRecursively(root);
        Files.createDirectories(root);
    }
}
