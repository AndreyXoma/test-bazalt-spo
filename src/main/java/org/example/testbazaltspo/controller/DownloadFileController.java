package org.example.testbazaltspo.controller;

import org.example.testbazaltspo.exception.NotFoundException;
import org.example.testbazaltspo.model.FileInfo;
import org.example.testbazaltspo.service.DirectoryComparison;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class DownloadFileController {

    private final DirectoryComparison directoryComparison;
    public DownloadFileController(DirectoryComparison directoryComparison) {
        this.directoryComparison = directoryComparison;
    }


    @GetMapping("/download/{relativePath:.+}")
    public ResponseEntity<Resource> download(@PathVariable String relativePath) throws IOException {
        FileInfo info = directoryComparison.getFileResult(relativePath);

        if (info == null) {
            throw new RuntimeException("File not found: " + relativePath);
        }

        Path file = info.getAbsolutePath();
        if (!Files.exists(file)) {
            throw new RuntimeException("File does not exist on disk: " + file);
        }

        Resource resource = new FileSystemResource(file.toFile());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName().toString() + "\"")
                .contentLength(Files.size(file))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
