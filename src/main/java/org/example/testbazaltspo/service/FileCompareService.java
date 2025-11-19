package org.example.testbazaltspo.service;

import org.example.testbazaltspo.model.ComparisonResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileCompareService {

    public ComparisonResult compareFiles(Path file1, Path file2) throws IOException {
        byte[] buffer1 = Files.readAllBytes(file1);
        byte[] buffer2 = Files.readAllBytes(file2);

        ComparisonResult result = new ComparisonResult(true);

        int min = Math.min(buffer1.length, buffer2.length);

        for(int i = 0; i < min; i++) {
            if(buffer1[i] != buffer2[i]) {
                result.addDamageOffset(i);
            }
        }

        if(buffer1.length != buffer2.length) {
            result.setResult(false);
        }

        return result;
    }
}
