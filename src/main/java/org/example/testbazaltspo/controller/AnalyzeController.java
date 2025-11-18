package org.example.testbazaltspo.controller;

import org.example.testbazaltspo.model.FileInfo;
import org.example.testbazaltspo.service.DirectoryComparison;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
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
        directoryComparison.comparison(Path.of(directory1), Path.of(directory2));

        Collection<FileInfo> results = directoryComparison.getResults();
        model.addAttribute("results", results);
        return "index";
    }

    @GetMapping("/file/{file}")
    public String viewFile(@PathVariable String file, Model model) {
        FileInfo info = directoryComparison.getFileResult(file);
        if (info == null) {
            return "file-not-found";
        }
        model.addAttribute("fileInfo", info);
        return "file-datails";
    }
}
