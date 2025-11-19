package org.example.testbazaltspo.controller;

import org.example.testbazaltspo.exception.NotFoundException;
import org.example.testbazaltspo.model.FileInfo;
import org.example.testbazaltspo.service.DirectoryComparison;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@Controller
public class AnalyzeController {

    private final DirectoryComparison directoryComparison;

    public AnalyzeController(DirectoryComparison directoryComparison) {
        this.directoryComparison = directoryComparison;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/comparison")
    public String comparison(@RequestParam String directory1,
                             @RequestParam String directory2,
                             Model model) throws IOException {

        Path dir1 = Path.of(directory1);
        Path dir2 = Path.of(directory2);

        if (!Files.exists(dir1) || !Files.isDirectory(dir1)) {
            model.addAttribute("message", "Directory 1 does not exist: " + directory1);
            return "file-not-found";
        }
        if (!Files.exists(dir2) || !Files.isDirectory(dir2)) {
            model.addAttribute("message", "Directory 2 does not exist: " + directory2);
            return "file-not-found";
        }

        directoryComparison.comparison(dir1, dir2);

        Collection<FileInfo> results = directoryComparison.getResults();
        model.addAttribute("results", results);

        if(results.isEmpty()) {
            model.addAttribute("message", "No damaged files found");
        }

        return "index";
    }

    @GetMapping("/file/{fileName}")
    public String viewFile(@PathVariable String fileName, Model model) {
        FileInfo info = directoryComparison.getFileResult(fileName);
        if (info == null) {
            model.addAttribute("message", "No file found");
            return "file-not-found";
        }
        model.addAttribute("fileInfo", info);
        return "file-details";
    }
}
