package org.example.testbazaltspo.model;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


public class ComparisonResult {
    private boolean result;
    private List<Integer> damageOffsets = new ArrayList<>();

    public ComparisonResult(boolean result) {
        this.result = result;
    }

    public void addDamageOffset(int damageOffset) {
        damageOffsets.add(damageOffset);
        this.result = false;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<Integer> getDamageOffsets() {
        return damageOffsets;
    }
}
