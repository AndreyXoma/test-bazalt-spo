package org.example.testbazaltspo.model;

import java.util.List;

public class FileInfo {
    private String filePath;
    private long fileSize;
    private List<Integer> damageOffsets;

    public FileInfo(String filePath, long fileSize, List<Integer> damageOffsets) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.damageOffsets = damageOffsets;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public List<Integer> getDamageOffsets() {
        return damageOffsets;
    }
}
