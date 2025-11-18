package org.example.testbazaltspo.api;

import org.example.testbazaltspo.model.FileInfo;
import org.example.testbazaltspo.service.DirectoryComparison;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping("/api")
public class ComparisonController {

    private final DirectoryComparison directoryComparison;

    public ComparisonController(DirectoryComparison directoryComparison) {
        this.directoryComparison = directoryComparison;
    }

    @GetMapping("/results")
    public Collection<FileInfo> results() {
        return directoryComparison.getResults();
    }

    @GetMapping("/results/{file}")
    public FileInfo fileInfo(@PathVariable String file) {
        return directoryComparison.getFileResult(file);
    }

}
