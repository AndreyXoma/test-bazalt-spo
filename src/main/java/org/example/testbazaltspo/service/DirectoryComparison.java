package org.example.testbazaltspo.service;

import org.example.testbazaltspo.model.ComparisonResult;
import org.example.testbazaltspo.model.FileInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class DirectoryComparison {

    private final FileCompareService fileCompareService;

    private final Map<String, FileInfo> lastResults =  new HashMap<>();

    public DirectoryComparison(FileCompareService fileCompareService) {
        this.fileCompareService = fileCompareService;
    }

    public void comparison(Path directory1, Path directory2) throws IOException {
        lastResults.clear();

        Files.walk(directory1).forEach(path1 -> {
            try {
                if(Files.isDirectory(path1)) {
                    return;
                }

                Path relativePath1 = directory1.relativize(path1);
                Path path2 = directory2.resolve(relativePath1);

                if(!Files.exists(path2)) {
                    return;
                }

                ComparisonResult result = fileCompareService.compareFiles(path1, path2);

                if(!result.isResult()) {
                    System.out.println("Found difference in: " + relativePath1);
                    FileInfo info = new FileInfo(
                            relativePath1.toString(),
                            Files.size(path1),
                            result.getDamageOffsets()

                    );
                    lastResults.put(relativePath1.toString(), info);
                } else {
                    System.out.println("No difference in: " + relativePath1);
                }
            } catch (Exception ignored) {

            }
        });
    }

    public Collection<FileInfo> getResults() {
        return lastResults.values();
    }

    public FileInfo getFileResult(String name) {
        return lastResults.get(name);
    }
}
