package org.example.testbazaltspo.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileInfo {

    private Path absolutePath;
    private String fileName;
    private long fileSize;
    private List<Integer> damageOffsets;

    public FileInfo(Path path, long fileSize, List<Integer> damageOffsets) {
        this.absolutePath = path;
        this.fileName = path.getFileName().toString();
        this.fileSize = fileSize;
        this.damageOffsets = damageOffsets;
    }

    public Path getAbsolutePath() {
        return absolutePath;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public List<Integer> getDamageOffsets() {
        return damageOffsets;
    }
}
