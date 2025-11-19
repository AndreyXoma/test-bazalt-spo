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
import org.springframework.ui.Model;
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


    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String fileName, Model model) throws IOException {
        FileInfo info = directoryComparison.getFileResult(fileName);

        if (info == null) {
            model.addAttribute("message", "File not found: " + fileName);
            return ResponseEntity.notFound().build();
        }

        Path file = info.getAbsolutePath();
        if (!Files.exists(file)) {
            model.addAttribute("message", "File does not exist on disk: " + file);
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file.toFile());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentLength(Files.size(file))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
