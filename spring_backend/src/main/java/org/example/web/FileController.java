package org.example.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * PUBLIC_INTERFACE
 * File upload and download endpoints.
 */
@RestController
@RequestMapping("/api/files")
@Tag(name = "Files", description = "File upload and download")
public class FileController {

    private final FileStorageService storage;

    public FileController(FileStorageService storage) {
        this.storage = storage;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload file")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file) throws IOException {
        String path = storage.store(file);
        return ResponseEntity.ok().body(java.util.Map.of("filename", file.getOriginalFilename(), "path", path));
    }

    @GetMapping("/{filename}")
    @Operation(summary = "Download file")
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        Resource resource = storage.load(filename);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .body(resource);
    }
}
