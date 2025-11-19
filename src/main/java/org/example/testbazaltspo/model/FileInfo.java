package org.example.testbazaltspo.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileInfo {

    private Path absolutePath;   // полный путь для скачивания
    private String relativePath; // для отображения в UI
    private long fileSize;
    private List<Integer> damageOffsets;

    public FileInfo(Path absolutePath, Path baseDir, List<Integer> damageOffsets) throws IOException {
        this.absolutePath = absolutePath;
        this.relativePath = baseDir.relativize(absolutePath).toString();
        this.fileSize = Files.size(absolutePath);
        this.damageOffsets = damageOffsets;
    }

    public Path getAbsolutePath() {
        return absolutePath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public List<Integer> getDamageOffsets() {
        return damageOffsets;
    }
}
